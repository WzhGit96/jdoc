package com.wzh.jdoc.service.impl;

import com.wzh.jdoc.controller.JdocController;
import com.wzh.jdoc.entity.*;
import com.wzh.jdoc.service.JdocService;
import com.wzh.jdoc.util.Cache;
import com.wzh.jdoc.util.ClassUtil;
import com.wzh.jdoc.util.MappingUtil;
import com.wzh.jdoc.util.SuperApplicationContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * @author wzh
 * @since 2021/7/26
 */
@Service
public class JdocServiceImpl implements JdocService {
    private static final String INPUT_PARAMETER_REGEX = "DTO";

    private static final String RETURN_CLASS_NAME = "GenericRspDTO";

    private static final String NOBODY = "NoBody";

    @Resource
    private SuperApplicationContext superApplicationContext;

    @Override
    public Model getInterfaceInfoList() throws IOException {
        if (Cache.get() != null) {
            return Cache.get();
        }
        Map<String, Object> controllers = getSuperControllers();
        String packageName = getPackageName(controllers);
        // 可以通过spring 拿到所有controller.class,getClasses方法只是让你更好的理解什么是spring ioc
        List<Class<?>> classes = ClassUtil.getClasses(packageName);
        List<ModelProperty> modelProperties = new ArrayList<>();
        List<FunctionTrade> functionTradeList = new ArrayList<>();
        AtomicInteger modelId = new AtomicInteger();
        AtomicInteger infId = new AtomicInteger();
        classes.forEach(clz -> {
            ModelProperty modelProperty = new ModelProperty();
            RequestMapping headAnnotation = clz.getAnnotation(RequestMapping.class);
            String urlPrefix = headAnnotation == null ? "" : headAnnotation.value()[0];
            Method[] methods = clz.getDeclaredMethods();
            List<InterfaceInfo> interfaceInfos = new ArrayList<>();
            Stream.of(methods).forEach(method -> {
                // 获取接口信息
                this.dealInterfaceInfo(method, interfaceInfos, urlPrefix, infId);
                // 获取模块清单
                this.dealFunctionTradeInfo(method, functionTradeList, clz);
            });
            Api annoApi = clz.getAnnotation(Api.class);
            if (annoApi != null) {
                String value = annoApi.value();
                String tag = annoApi.value();
                modelProperty.setModelDesc(value.trim().length() > 0 ? value : tag);
            }
            modelProperty.setInterfaceInfoList(interfaceInfos);
            modelProperty.setModelName(clz.getSimpleName());
            modelProperty.setModelId(String.valueOf(modelId.addAndGet(1)));
            modelProperties.add(modelProperty);
        });
        Model model = Model.newInstance(modelProperties, functionTradeList);
        // 放入缓存
        Cache.put(model);
        return model;
    }

    private boolean isMappingAnnotation(Annotation annotation) {
        return annotation instanceof RequestMapping || annotation instanceof GetMapping || annotation instanceof PostMapping || annotation instanceof PutMapping || annotation instanceof DeleteMapping;
    }

    private Input getInputProperty(Method method) {
        Class<?>[] inputClzs = method.getParameterTypes();
        Class<?> inputClz = Stream.of(inputClzs).filter(item -> item.getName().contains(INPUT_PARAMETER_REGEX)).findFirst().orElse(null);
        Input input = new Input();
        if (inputClz == null) {
            return null;
        }
        Field[] fields = inputClz.getDeclaredFields();
        input.setFields(getFieldsList(fields));
        return input;
    }

    private Output getOutputProperty(Method method) {
        Class<?> outputClz = method.getReturnType();
        if (outputClz == null || outputClz == Void.class) {
            return null;
        }
        Output output = new Output();
        Class<?> fieldClz = outputClz;
        // 如果是标准的GenericRspDTO接口
        if (RETURN_CLASS_NAME.equals(outputClz.getSimpleName())) {
            Type type = method.getGenericReturnType();
            ParameterizedType parameterizedType = (ParameterizedType) type;
            fieldClz = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        }
        if (NOBODY.equalsIgnoreCase(fieldClz.getSimpleName()) || Void.class.getSimpleName().equalsIgnoreCase(fieldClz.getSimpleName())) {
            return null;
        }
        Field[] fields = fieldClz.getDeclaredFields();
        output.setFields(getFieldsList(fields));
        return output;
    }

    private List<FieldProperty> getFieldsList(Field[] fields) {
        List<FieldProperty> fieldPropertyList = new ArrayList<>();
        Stream.of(fields).forEach(item -> {
            FieldProperty fieldProperty = new FieldProperty();
            Annotation fieldAnno = item.getAnnotation(ApiModelProperty.class);
            if (fieldAnno != null) {
                String value = ((ApiModelProperty) fieldAnno).value();
                String name = ((ApiModelProperty) fieldAnno).name();
                fieldProperty.setFieldDesc(value.trim().length() > 0 ? value : name);
            }
            fieldProperty.setFieldName(item.getName());
            fieldProperty.setFieldType(item.getType().getSimpleName());
            fieldPropertyList.add(fieldProperty);
        });
        return fieldPropertyList;
    }

    private Map<String, Object> getSuperControllers() {
        ApplicationContext applicationContext = superApplicationContext.getApplicationContext();
        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(RestController.class);
        Map<String, Object> filterMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(applicationContext.getBeansWithAnnotation(Controller.class))) {
            controllers.putAll(applicationContext.getBeansWithAnnotation(Controller.class));
        }
        controllers.forEach((k, v) -> {
            // 能取到包名即可
            if (!CollectionUtils.isEmpty(filterMap)) {
                return;
            }
            if (!(v instanceof JdocController)) {
                filterMap.put(k, v);
            }
        });
        Assert.isTrue(!CollectionUtils.isEmpty(filterMap), "没有匹配的controller");
        return filterMap;
    }

    private String getPackageName(Map<String, Object> controllers) {
        List<String> key = new ArrayList<>(controllers.keySet());
        String firstKey = key.get(0);
        Object packObject = controllers.get(firstKey);
        return packObject.getClass().getPackage().getName();
    }

    private void dealInterfaceInfo(Method method, List<InterfaceInfo> interfaceInfos, String urlPrefix, AtomicInteger infId) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        Annotation[] annotations = method.getAnnotations();
        Stream.of(annotations).forEach(anno -> {
            if (isMappingAnnotation(anno)) {
                MappingInfo mappingInfo = MappingUtil.getMappingInfo(anno);
                interfaceInfo.setUrl(urlPrefix + mappingInfo.getUrl());
                interfaceInfo.setRequestMethod(mappingInfo.getRequestMethod());
            }
            if (anno instanceof ApiOperation) {
                interfaceInfo.setFunctionDesc(((ApiOperation) anno).value());
            }
        });
        interfaceInfo.setFunctionName(method.getName());
        // 标准接口 reqDTO代码规范
        Input input = getInputProperty(method);
        Output output = getOutputProperty(method);
        interfaceInfo.setInput(input);
        interfaceInfo.setOutput(output);
        interfaceInfo.setId(String.valueOf(infId.addAndGet(1)));
        interfaceInfos.add(interfaceInfo);
    }

    private void dealFunctionTradeInfo(Method method, List<FunctionTrade> functionTradeList, Class<?> clz) {
        FunctionTrade functionTrade = new FunctionTrade();
        functionTrade.setModelName(clz.getSimpleName());
        functionTrade.setFunctionName(method.getName());
        Annotation annotation = method.getAnnotation(ApiOperation.class);
        functionTrade.setFunctionDesc(annotation == null ? "" : ((ApiOperation) annotation).value());
        // 默认新增
        functionTrade.setRemark("新增");
        functionTradeList.add(functionTrade);
    }
}

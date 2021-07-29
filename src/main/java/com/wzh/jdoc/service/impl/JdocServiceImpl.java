package com.wzh.jdoc.service.impl;

import com.wzh.jdoc.controller.JdocController;
import com.wzh.jdoc.entity.*;
import com.wzh.jdoc.service.JdocService;
import com.wzh.jdoc.util.ClassUtil;
import com.wzh.jdoc.util.MappingUtil;
import com.wzh.jdoc.util.SuperApplicationContext;
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
    public List<InterfaceInfoList> getInterfaceInfoList() throws IOException {
        Map<String, Object> controllers = getSuperControllers();
        String packageName = getPackageName(controllers);
        List<Class<?>> classes = ClassUtil.getClasses(packageName);
        List<InterfaceInfoList> result = new ArrayList<>();
        classes.forEach(clz -> {
            InterfaceInfoList interfaceInfoList = new InterfaceInfoList();
            RequestMapping headAnnotation = clz.getAnnotation(RequestMapping.class);
            String urlPrefix = headAnnotation == null ? "" : headAnnotation.value()[0];
            Method[] methods = clz.getDeclaredMethods();
            List<InterfaceInfo> interfaceInfos = new ArrayList<>();
            Stream.of(methods).forEach(method -> {
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
                interfaceInfos.add(interfaceInfo);
            });
            interfaceInfoList.setInterfaceInfoList(interfaceInfos);
            result.add(interfaceInfoList);
        });
        return result;
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
            Type type = outputClz.getSuperclass().getSuperclass().getGenericSuperclass();
            if (!(type instanceof ParameterizedType)) {
                fieldClz = Object.class;
            } else {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type fieldType =  parameterizedType.getActualTypeArguments()[0];
                if (!(fieldType instanceof Class)) {
                    fieldClz = Object.class;
                } else {
                    fieldClz = (Class) fieldType;
                }
            }
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
        if (CollectionUtils.isEmpty(controllers)) {
            controllers = applicationContext.getBeansWithAnnotation(Controller.class);
        }
        controllers.forEach((k, v) -> {
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
}

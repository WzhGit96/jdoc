package com.wzh.jdoc.service.impl;

import com.wzh.jdoc.entity.*;
import com.wzh.jdoc.service.JdocService;
import com.wzh.jdoc.util.ClassUtil;
import com.wzh.jdoc.util.MappingUtil;
import freemarker.template.utility.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author wzh
 * @since 2021/7/26
 */
@Service
public class JdocServiceImpl implements JdocService {
    private static final String BASE_PACKAGE = "com.cmpay.*.controller";

    private static final String INPUT_PARAMETER_REGEX = "DTO";

    private static final String RETURN_CLASS_NAME = "GenericRspDTO";

    @Override
    public List<InterfaceInfoList> getInterfaceInfoList() throws IOException {
        List<Class<?>> classes = ClassUtil.getClasses(BASE_PACKAGE);
        List<InterfaceInfoList> result = new ArrayList<>();
        classes.forEach(clz -> {
            InterfaceInfoList interfaceInfoList = new InterfaceInfoList();
            RequestMapping headAnnotation = clz.getAnnotation(RequestMapping.class);
            String urlPrefix = headAnnotation == null ? "" : headAnnotation.value()[0];
            Method[] methods = clz.getMethods();
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
        return null;
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
            Type type = outputClz.getGenericSuperclass();
            ParameterizedType parameterizedType = (ParameterizedType) type;
            fieldClz = (Class<?>) parameterizedType.getActualTypeArguments()[0];
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
}

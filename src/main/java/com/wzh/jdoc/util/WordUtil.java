package com.wzh.jdoc.util;

import com.wzh.jdoc.entity.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 咋瓦鲁多
 *
 * @author wzh
 * @since 2021/7/30
 */
public final class WordUtil {
    private WordUtil() {
        super();
    }

    private static final String FILE_SUFFIX = "_V1.0（后台）.docx";

    /**
     * 创建word文档
     *
     * @param model model
     * @param reqName request name
     * @return OK
     */
    public static String createWord(Model model, String reqName) throws FileNotFoundException {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("reqName", reqName);
        dataMap.put("year", LocalDate.now().getYear());
        dataMap.put("month", LocalDate.now().getMonthValue());
        dataMap.put("tradeList", getTradeList(model.getFunctionTradeList()));
        dataMap.put("model", getModel(model.getModelProperties()));
        String fileName = reqName + FILE_SUFFIX;
        String filePath = ResourceUtils.getURL("/").getPath() + "static";
        return buildWord(dataMap, "model.ftl", filePath, fileName);
    }

    private static String buildWord(Map<String, Object> dataMap, String modelName, String filePath, String fileName) {
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setClassForTemplateLoading(WordUtil.class,"/template");
        Writer out = null;
        try {
            Template template = configuration.getTemplate(modelName);

            File outFile = new File(filePath + File.separator + fileName);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }

            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8));
            template.process(dataMap, out);
            out.flush();
            return outFile.getCanonicalPath();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "FAIL";
    }

    private static List<Map<String, Object>> getTradeList(List<FunctionTrade> list) {
        List<Map<String, Object>> tradeList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return tradeList;
        }
        list.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            Field[] fields = item.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    map.put(field.getName(), field.get(item));
                    tradeList.add(map);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return tradeList;
    }

    private static List<Map<String, Object>> getModel(List<ModelProperty> modelProperty) {
        List<Map<String, Object>> model = new ArrayList<>();
        if (CollectionUtils.isEmpty(modelProperty)) {
            return model;
        }
        modelProperty.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("seqNo", item.getSeqNo());
            map.put("modelName", item.getModelName());
            map.put("modelDesc", item.getModelDesc());
            map.put("infList", getInfList(item.getInterfaceInfoList()));
            model.add(map);
        });
        return model;
    }

    private static List<Map<String, Object>> getInfList(List<InterfaceInfo> interfaceInfos) {
        List<Map<String, Object>> infList = new ArrayList<>();
        if (CollectionUtils.isEmpty(interfaceInfos)) {
            return infList;
        }
        interfaceInfos.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("seqNo", item.getSeqNo());
            map.put("functionName", item.getFunctionName());
            map.put("functionDesc", item.getFunctionDesc());
            map.put("requestMethod", item.getRequestMethod());
            map.put("url", item.getUrl());
            map.put("input", getFieldInputOrOutput(item.getInput() == null ? null : item.getInput().getFields()));
            map.put("output", getFieldInputOrOutput(item.getOutput() == null ? null : item.getOutput().getFields()));
            infList.add(map);
        });
        return infList;
    }

    private static List<Map<String, Object>> getFieldInputOrOutput(List<FieldProperty> fieldProperties) {
        List<Map<String, Object>> io = new ArrayList<>();
        if (CollectionUtils.isEmpty(fieldProperties)) {
            return io;
        }
        fieldProperties.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("fieldName", item.getFieldName());
            map.put("fieldDesc", item.getFieldDesc());
            map.put("fieldType", item.getFieldType());
            map.put("length", item.getLength());
            map.put("necessary", item.isNecessary());
            map.put("remark", item.getRemark());
            io.add(map);
        });
        return io;
    }
}

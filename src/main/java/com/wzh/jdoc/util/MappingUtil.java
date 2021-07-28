package com.wzh.jdoc.util;

import com.wzh.jdoc.entity.MappingInfo;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;

/**
 * @author wzh
 * @since 2021/7/28
 */
public final class MappingUtil {
    private MappingUtil() {
        super();
    }

    public static MappingInfo getMappingInfo(Annotation annotation) {
        MappingInfo mappingInfo = new MappingInfo();
        String url = "";
        String requestMethod = "";
        if (annotation instanceof RequestMapping) {
            url = ((RequestMapping) annotation).value()[0];
            requestMethod = ((RequestMapping) annotation).method()[0].name();
        }
        if (annotation instanceof GetMapping) {
            url = ((GetMapping) annotation).value()[0];
            requestMethod = RequestMethod.GET.name();
        }
        if (annotation instanceof PostMapping) {
            url = ((PostMapping) annotation).value()[0];
            requestMethod = RequestMethod.POST.name();
        }
        if (annotation instanceof PutMapping) {
            url = ((PutMapping) annotation).value()[0];
            requestMethod = RequestMethod.PUT.name();
        }
        if (annotation instanceof DeleteMapping) {
            url = ((DeleteMapping) annotation).value()[0];
            requestMethod = RequestMethod.DELETE.name();
        }
        mappingInfo.setUrl(url);
        mappingInfo.setRequestMethod(requestMethod);
        return mappingInfo;
    }
}

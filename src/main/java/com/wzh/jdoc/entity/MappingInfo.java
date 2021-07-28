package com.wzh.jdoc.entity;

/**
 * @author wzh
 * @since 2021/7/28
 */
public class MappingInfo {
    private String url;

    private String requestMethod;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Override
    public String toString() {
        return "MappingInfo{" +
                "url='" + url + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                '}';
    }
}

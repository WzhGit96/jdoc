package com.wzh.jdoc.entity;

/**
 * @author wzh
 * @since 2021/8/17
 */
public class AppInfo {
    private String applicationName;

    private String env;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "applicationName='" + applicationName + '\'' +
                ", env='" + env + '\'' +
                '}';
    }
}

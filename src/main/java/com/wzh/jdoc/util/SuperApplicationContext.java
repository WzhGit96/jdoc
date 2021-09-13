package com.wzh.jdoc.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author wzh
 * @since 2021/7/29
 */
@Component
public class SuperApplicationContext implements EnvironmentAware,ApplicationContextAware {
    private ApplicationContext applicationContext;
    private Environment environment;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return this.environment;
    }
}

package com.cmpay.lemon.wzh.jdoc.autoconfiguration;

import com.wzh.jdoc.controller.JdocController;
import com.wzh.jdoc.service.JdocService;
import com.wzh.jdoc.service.impl.JdocServiceImpl;
import com.wzh.jdoc.util.SuperApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wzh
 * @since 2021/7/26
 */
@Configuration
public class JdocAutoConfiguration {
    @Bean
    public JdocController getJdocController() {
        return new JdocController();
    }

    @Bean
    public JdocService getJdocService() {
        return new JdocServiceImpl();
    }

    @Bean
    public SuperApplicationContext getSuperApplicationContext() {
        return new SuperApplicationContext();
    }
}

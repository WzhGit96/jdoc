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
    public JdocController jdocController() {
        return new JdocController();
    }

    @Bean
    public JdocService jdocService() {
        return new JdocServiceImpl();
    }

    @Bean
    public SuperApplicationContext superApplicationContext() {
        return new SuperApplicationContext();
    }
}

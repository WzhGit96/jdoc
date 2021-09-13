package com.wzh.jdoc.controller;

import com.wzh.jdoc.entity.AppInfo;
import com.wzh.jdoc.entity.Model;
import com.wzh.jdoc.service.JdocService;
import com.wzh.jdoc.util.SuperApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author wzh
 * @since 2021/7/26
 */
@Controller
public class JdocController {
    @Value("${spring.application.name:}")
    private String applicationName;

    @Resource
    private JdocService jdocService;

    @Resource
    private SuperApplicationContext superApplicationContext;

    @ResponseBody
    @RequestMapping(value = "/jdoc-apis", method = RequestMethod.GET)
    public Model allApis() throws IOException {
        return jdocService.getInterfaceInfoList();
    }

    @ResponseBody
    @RequestMapping(value = "/jdoc-appInfo", method = RequestMethod.GET)
    public AppInfo getAppInfo() {
        AppInfo appInfo = new AppInfo();
        Environment environment = superApplicationContext.getEnvironment();
        appInfo.setApplicationName(applicationName);
        appInfo.setEnv(environment.getProperty("spring.profiles.active"));
        return appInfo;
    }
}

package com.wzh.jdoc.controller;

import com.wzh.jdoc.entity.Model;
import com.wzh.jdoc.service.JdocService;
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
    @Resource
    private JdocService jdocService;

    @ResponseBody
    @RequestMapping(value = "/jdoc-apis", method = RequestMethod.GET)
    public Model allApis() throws IOException {
        return jdocService.getInterfaceInfoList();
    }
}

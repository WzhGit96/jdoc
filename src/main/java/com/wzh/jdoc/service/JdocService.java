package com.wzh.jdoc.service;

import com.wzh.jdoc.entity.Model;
import com.wzh.jdoc.entity.ModelProperty;

import java.io.IOException;

/**
 * @author wzh
 * @since 2021/7/26
 */
public interface JdocService {
    Model getInterfaceInfoList() throws IOException;
}

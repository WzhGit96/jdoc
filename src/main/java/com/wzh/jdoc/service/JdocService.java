package com.wzh.jdoc.service;

import com.wzh.jdoc.entity.InterfaceInfoList;

import java.io.IOException;
import java.util.List;

/**
 * @author wzh
 * @since 2021/7/26
 */
public interface JdocService {
    List<InterfaceInfoList> getInterfaceInfoList() throws IOException;
}

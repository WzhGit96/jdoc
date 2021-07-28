package com.wzh.jdoc.entity;

import java.util.List;

/**
 * @author wzh
 * @since 2021/7/28
 */
public class InterfaceInfoList {
    private List<InterfaceInfo> interfaceInfoList;

    public List<InterfaceInfo> getInterfaceInfoList() {
        return interfaceInfoList;
    }

    public void setInterfaceInfoList(List<InterfaceInfo> interfaceInfoList) {
        this.interfaceInfoList = interfaceInfoList;
    }

    @Override
    public String toString() {
        return "InterfaceInfoList{" +
                "interfaceInfoList=" + interfaceInfoList +
                '}';
    }
}

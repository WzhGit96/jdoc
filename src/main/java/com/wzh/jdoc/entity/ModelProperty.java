package com.wzh.jdoc.entity;

import java.util.List;

/**
 * @author wzh
 * @since 2021-07-29
 */
public class ModelProperty {
    private String seqNo;

    private String modelName;

    private String modelDesc;

    private List<InterfaceInfo> interfaceInfoList;

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelDesc() {
        return modelDesc;
    }

    public void setModelDesc(String modelDesc) {
        this.modelDesc = modelDesc;
    }

    public List<InterfaceInfo> getInterfaceInfoList() {
        return interfaceInfoList;
    }

    public void setInterfaceInfoList(List<InterfaceInfo> interfaceInfoList) {
        this.interfaceInfoList = interfaceInfoList;
    }

    @Override
    public String toString() {
        return "ModelProperty{" +
                "seqNo='" + seqNo + '\'' +
                ", modelName='" + modelName + '\'' +
                ", modelDesc='" + modelDesc + '\'' +
                ", interfaceInfoLists=" + interfaceInfoList +
                '}';
    }
}

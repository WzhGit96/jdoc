package com.wzh.jdoc.entity;

import java.io.Serializable;

/**
 * @author wzh
 * @since 2021/7/26
 */
public class FunctionTrade {
    private String modelName;

    private String functionName;

    private String functionDesc;

    private String remark;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionDesc() {
        return functionDesc;
    }

    public void setFunctionDesc(String functionDesc) {
        this.functionDesc = functionDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "FunctionTradeList{" +
                "modelName='" + modelName + '\'' +
                ", functionName='" + functionName + '\'' +
                ", functionDesc='" + functionDesc + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}

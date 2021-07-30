package com.wzh.jdoc.entity;

import java.util.List;

/**
 * @author wzh
 * @since 2021-07-29
 */
public class Model {
    private List<ModelProperty> modelProperties;

    private List<FunctionTrade> functionTradeList;

    public List<ModelProperty> getModelProperties() {
        return modelProperties;
    }

    public void setModelProperties(List<ModelProperty> modelProperties) {
        this.modelProperties = modelProperties;
    }

    public List<FunctionTrade> getFunctionTradeList() {
        return functionTradeList;
    }

    public void setFunctionTradeList(List<FunctionTrade> functionTradeList) {
        this.functionTradeList = functionTradeList;
    }

    /**
     * new instance model
     *
     * @param modelProperties
     * @return
     */
    public static Model newInstance(List<ModelProperty> modelProperties, List<FunctionTrade> functionTradeList) {
        Model model = new Model();
        model.setModelProperties(modelProperties);
        model.setFunctionTradeList(functionTradeList);
        return model;
    }

    @Override
    public String toString() {
        return "Model{" +
                "modelProperties=" + modelProperties +
                ", functionTradeList=" + functionTradeList +
                '}';
    }
}

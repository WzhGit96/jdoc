package com.wzh.jdoc.entity;

import java.util.List;

/**
 * @author wzh
 * @since 2021-07-29
 */
public class Model {
    private List<ModelProperty> modelProperties;

    public List<ModelProperty> getModelProperties() {
        return modelProperties;
    }

    public void setModelProperties(List<ModelProperty> modelProperties) {
        this.modelProperties = modelProperties;
    }

    /**
     * new instance model
     *
     * @param modelProperties
     * @return
     */
    public static Model newInstance(List<ModelProperty> modelProperties) {
        Model model = new Model();
        model.setModelProperties(modelProperties);
        return model;
    }

    @Override
    public String toString() {
        return "Model{" +
                "modelProperties=" + modelProperties +
                '}';
    }
}

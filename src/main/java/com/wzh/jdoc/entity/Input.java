package com.wzh.jdoc.entity;

import java.util.List;

/**
 * @author wzh
 * @since 2021/7/26
 */
public class Input {
    private List<FieldProperty> fields;

    public List<FieldProperty> getFields() {
        return fields;
    }

    public void setFields(List<FieldProperty> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "Input{" +
                "fields=" + fields +
                '}';
    }
}

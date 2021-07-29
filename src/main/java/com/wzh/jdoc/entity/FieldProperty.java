package com.wzh.jdoc.entity;

/**
 * @author wzh
 * @since 2021/7/26
 */
public class FieldProperty {
    private String fieldName;

    private String fieldDesc;

    private String fieldType;

    private Integer length;

    private boolean necessary;

    private String remark;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isNecessary() {
        return necessary;
    }

    public void setNecessary(boolean necessary) {
        this.necessary = necessary;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "FieldProperty{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldDesc='" + fieldDesc + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", length=" + length +
                ", necessary=" + necessary +
                ", remark='" + remark + '\'' +
                '}';
    }
}

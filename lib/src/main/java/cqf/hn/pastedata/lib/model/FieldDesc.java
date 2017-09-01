package cqf.hn.pastedata.lib.model;

/**
 * Created by cqf on 2017/8/31 11:24
 * 字段的描述：字段的名字和类型、类型所在的包
 */
public class FieldDesc {
    private String fieldName;
    private String fieldType;
    private String fieldTypePackage;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldTypePackage() {
        return fieldTypePackage;
    }

    public void setFieldTypePackage(String fieldTypePackage) {
        this.fieldTypePackage = fieldTypePackage;
    }
}

package cqf.hn.pastedata.lib;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * 被BindView注解标记的字段的模型类
 */
public class DifFieldModel {

    private VariableElement mFieldElement;

    private Class[] classes;
    private final String[] names;

    public DifFieldModel(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.FIELD) {//判断是否是类成员
            throw new IllegalArgumentException(String.format("Only field can be annotated with @%s",
                    DifField.class.getSimpleName()));
        }
        mFieldElement = (VariableElement) element;
        //获取注解和值  
        DifField difField = mFieldElement.getAnnotation(DifField.class);
        classes = difField.value();
        names = difField.name();
    }

    public Name getFieldName() {
        return mFieldElement.getSimpleName();
    }

    public Class[] getClasses() {
        return classes;
    }

    public String[] getNames() {
        return names;
    }

    public TypeMirror getFieldType() {
        return mFieldElement.asType();
    }
}  
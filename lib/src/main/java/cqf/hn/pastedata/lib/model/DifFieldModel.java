package cqf.hn.pastedata.lib.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import cqf.hn.pastedata.lib.annotation.DifField;

/**
 * 被DifField注解标记的字段的模型类
 */
public class DifFieldModel {

    private VariableElement mFieldElement;

    private ArrayList<String> classPaths = new ArrayList<>();
    private String[] names;

    public DifFieldModel(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.FIELD) {//判断是否是类成员
            throw new IllegalArgumentException(String.format("Only field can be annotated with @%s",
                    DifField.class.getSimpleName()));
        }
        mFieldElement = (VariableElement) element;
        //获取注解和值  
        DifField difField = mFieldElement.getAnnotation(DifField.class);
        names = difField.name();
        //Class[] value = difField.value();无法直接获取到Class相关的值
        List<? extends AnnotationMirror> mirrors = mFieldElement.getAnnotationMirrors();
        for (AnnotationMirror mirror : mirrors) {
            Map<? extends ExecutableElement, ? extends AnnotationValue> values = mirror.getElementValues();
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : values.entrySet()) {
                AnnotationValue value = entry.getValue();
                ExecutableElement key = entry.getKey();
                if (key.getSimpleName().toString().equals("value")) {
                    List list = (List) value.getValue();
                    for (int i = 0; i < list.size(); i++) {
                        String s = list.get(i).toString();
                        String classPath = s.substring(0, s.lastIndexOf(".")).trim();
                        classPaths.add(classPath);
                    }
                }
            }
        }
    }

    public Name getFieldName() {
        return mFieldElement.getSimpleName();
    }

    public ArrayList<String> getClassPaths() {
        return classPaths;
    }

    public String[] getNames() {
        return names;
    }

    public TypeMirror getFieldType() {
        return mFieldElement.asType();
    }
}  
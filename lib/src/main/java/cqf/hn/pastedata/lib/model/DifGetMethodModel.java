package cqf.hn.pastedata.lib.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;

import cqf.hn.pastedata.lib.annotation.DifGetMethod;

/**
 * 被DifGetMethod注解标记的字段的模型类
 */
public class DifGetMethodModel {

    private ExecutableElement executableElement;

    /**
     * package.+类名集合
     */
    private ArrayList<String> classPaths = new ArrayList<>();
    /**
     * get方法名
     */
    private String[] names;
    /**
     * key：Src的全路径名
     * value：get方法名
     */
    private Map<String, String> difGetMethodValue = new HashMap<>();

    public DifGetMethodModel(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException(String.format("Only field can be annotated with @%s",
                    DifGetMethod.class.getSimpleName()));
        }
        executableElement = (ExecutableElement) element;
        //获取注解和值  
        DifGetMethod difGetMethod = executableElement.getAnnotation(DifGetMethod.class);
        names = difGetMethod.method_name();
        //Class[] value = difGetMethod.value();无法直接获取到Class相关的值
        List<? extends AnnotationMirror> mirrors = executableElement.getAnnotationMirrors();
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
        for (int i = 0; i < classPaths.size(); i++) {
            if (names != null && i >= names.length) {
                difGetMethodValue.put(classPaths.get(i), "");
            } else {
                difGetMethodValue.put(classPaths.get(i), names[i]);
            }
        }
    }

    public ArrayList<String> getClassPaths() {
        return classPaths;
    }

    public String[] getNames() {
        return names;
    }

    public Map<String, String> getDifGetMethodValue() {
        return difGetMethodValue;
    }

    public void setDifGetMethodValue(Map<String, String> difGetMethodValue) {
        this.difGetMethodValue = difGetMethodValue;
    }
}
package cqf.hn.pastedata.lib;

import com.squareup.javapoet.JavaFile;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by cqf on 2017/8/27 19:01
 */
public class PasteClass {
    /**
     * 类名
     */
    public TypeElement mClassElement;
    /**
     * 成员变量集合
     */
    public List<DifFieldModel> mFiled;
    /**
     * 元素辅助类
     */
    public Elements mElementUtils;

    public PasteClass(TypeElement classElement, Elements elementUtils) {
        this.mClassElement = classElement;
        this.mElementUtils = elementUtils;
        this.mFiled = new ArrayList<>();
    }
    /**
     * 获取当前这个类的全名
     */
    public String getFullClassName() {
        return mClassElement.getQualifiedName().toString();
    }
    /**
     * 添加一个成员
     */
    public void addField(DifFieldModel field) {
        mFiled.add(field);
    }
    /**
     * 输出Java
     */
    public JavaFile generateFinder() {
        return null;
    }
    /**
     * 包名
     */
    public String getPackageName(TypeElement type) {
        return mElementUtils.getPackageOf(type).getQualifiedName().toString();
    }
    /**
     * 类名
     */
    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }
}

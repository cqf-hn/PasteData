package cqf.hn.pastedata.lib;

import com.squareup.javapoet.JavaFile;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import cqf.hn.pastedata.lib.model.DifFieldModel;
import cqf.hn.pastedata.lib.model.SrcClassModel;

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
    public List<DifFieldModel> mDifFiled;
    /**
     * 类集合
     */
    public List<SrcClassModel> mClasses;

    /**
     * 元素辅助类
     */
    public Elements mElementUtils;
    private ArrayList<String> setMethodNames;
    private ArrayList<String> getMethodNames;

    public PasteClass(TypeElement classElement, Elements elementUtils) {
        this.mClassElement = classElement;
        this.mElementUtils = elementUtils;
        this.mDifFiled = new ArrayList<>();
        this.mClasses = new ArrayList<>();
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
    public void addDifField(DifFieldModel difField) {
        mDifFiled.add(difField);
    }

    /**
     * 添加不同的源类
     */
    public void addSrcClass(SrcClassModel srcClass) {
        mClasses.add(srcClass);
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

    public void addSetMethodNames(ArrayList<String> setMethodNames) {
        this.setMethodNames = setMethodNames;
    }

    public void addGetMethodNames(ArrayList<String> getMethodNames) {
        this.getMethodNames = getMethodNames;
    }
}

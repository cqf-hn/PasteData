package cqf.hn.pastedata.lib;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import cqf.hn.pastedata.lib.model.DifGetMethodModel;
import cqf.hn.pastedata.lib.model.MethodDesc;
import cqf.hn.pastedata.lib.model.SrcClassModel;
import cqf.hn.pastedata.lib.util.TypeUtil;

import static cqf.hn.pastedata.lib.PasteDataProcessor.BASE_TYPE;

/**
 * Created by cqf on 2017/8/27 19:01
 */
public class PasteClass {
    /**
     * 类名
     */
    public TypeElement mClassElement;
    /**
     * key：set方法名
     * value：被DifGetMethod注释的方法的
     */
    public Map<String, DifGetMethodModel> mDifGetMethods;
    /**
     * 类集合
     */
    public SrcClassModel srcClassModel;

    /**
     * 元素辅助类
     */
    public Elements mElementUtils;
    private ArrayList<MethodDesc> setMethods;
    private ArrayList<String> getMethodNames;

    public PasteClass(TypeElement classElement, Elements elementUtils) {
        this.mClassElement = classElement;
        this.mElementUtils = elementUtils;
        this.mDifGetMethods = new HashMap<>();
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
    public void addDifGetMethod(String methodName, DifGetMethodModel difGetMethod) {
        mDifGetMethods.put(methodName, difGetMethod);
    }

    /**
     * 添加不同的源类
     */
    public void setSrcClassModel(SrcClassModel srcClassModel) {
        this.srcClassModel = srcClassModel;
    }

    /**
     * 输出Java
     */
    public JavaFile generateFinder() {
        /**
         * 构建方法
         */
        MethodSpec.Builder pasteMethod = MethodSpec.methodBuilder("paste")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mClassElement.asType()), "dstData", Modifier.FINAL)
                .addParameter(TypeName.OBJECT, "srcData");
        for (int i = 0; srcClassModel != null && srcClassModel.getClassPaths() != null && i < srcClassModel.getClassPaths().size(); i++) {
            String classPath = srcClassModel.getClassPaths().get(i);
            String packageName = "";
            String simpleName;
            if (classPath.lastIndexOf(".") != -1) {
                packageName = classPath.substring(0, classPath.lastIndexOf("."));
                simpleName = classPath.substring(classPath.lastIndexOf(".") + 1);
            } else {
                simpleName = classPath;
            }
            if (i == 0) {
                pasteMethod.beginControlFlow("if (srcData instanceof $T)", ClassName.get(packageName, simpleName));
            } else {
                pasteMethod.beginControlFlow("else if (srcData instanceof $T)", ClassName.get(packageName, simpleName));
            }
            pasteMethod.addStatement("$T src = ($T)srcData", ClassName.get(packageName, simpleName)
                    , ClassName.get(packageName, simpleName));//强转
            for (int j = 0; setMethods != null && j < setMethods.size(); j++) {
                String setMethodName = setMethods.get(j).getMethodName();
                DifGetMethodModel difGetMethodModel = mDifGetMethods.get(setMethodName);
                if (difGetMethodModel != null) {
                    Map<String, String> difGetMethodValue = difGetMethodModel.getDifGetMethodValue();
                    String getMehtod = difGetMethodValue.get(classPath);
                    if (getMehtod != null) {
                        if (!getMehtod.equals("")) {
                            pasteMethod.addStatement("dstData.$N(src.$N())", setMethodName, getMehtod);
                        } else {
                            //continue
                        }
                    } else {
                        pasteMethod.addStatement("dstData.$N(src.$N())", setMethodName, getMethodNames.get(j));
                    }
                } else {
                    pasteMethod.addStatement("dstData.$N(src.$N())", setMethodName, getMethodNames.get(j));
                }
            }
            pasteMethod.endControlFlow();
        }

        MethodSpec.Builder unPasteMethod = MethodSpec.methodBuilder("unpaste")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mClassElement.asType()), "dstData", Modifier.FINAL);
        for (int j = 0; setMethods != null && j < setMethods.size(); j++) {
            MethodDesc methodDesc = setMethods.get(j);
            String setMethodName = methodDesc.getMethodName();
            DifGetMethodModel difGetMethodModel = mDifGetMethods.get(setMethodName);
            if (difGetMethodModel != null) {
                Map<String, String> difGetMethodValue = difGetMethodModel.getDifGetMethodValue();
                if (!BASE_TYPE.contains(methodDesc.getParamType())) {
                    unPasteMethod.addStatement("dstData.$N(null)", setMethodName);
                } else if (methodDesc.getParamType().equals("byte")
                        || methodDesc.getParamType().equals("java.lang.Byte")
                        || methodDesc.getParamType().equals("short")
                        || methodDesc.getParamType().equals("java.lang.Short")
                        || methodDesc.getParamType().equals("int")
                        || methodDesc.getParamType().equals("java.lang.Integer")) {
                    unPasteMethod.addStatement("dstData.$N(0)", setMethodName);
                } else if (methodDesc.getParamType().equals("long")
                        || methodDesc.getParamType().equals("java.lang.Long")
                        ) {
                    unPasteMethod.addStatement("dstData.$N(0L)", setMethodName);
                } else if (methodDesc.getParamType().equals("float")
                        || methodDesc.getParamType().equals("java.lang.Float")
                        ) {
                    unPasteMethod.addStatement("dstData.$N(0F)", setMethodName);
                } else if (methodDesc.getParamType().equals("double")
                        || methodDesc.getParamType().equals("java.lang.Double")) {
                    unPasteMethod.addStatement("dstData.$N(0D)", setMethodName);
                } else if (methodDesc.getParamType().equals("boolean")
                        || methodDesc.getParamType().equals("java.lang.Boolean")) {
                    unPasteMethod.addStatement("dstData.$N(false)", setMethodName);
                } else if (methodDesc.getParamType().equals("Char")
                        || methodDesc.getParamType().equals("java.lang.Character")) {
                    unPasteMethod.addStatement("dstData.$N('\\u0000')", setMethodName);
                }
            }
        }

        /**
         * 构建类
         */
        String packageName = getPackageName(mClassElement);
        String className = getClassName(mClassElement, packageName);
        ClassName pasteClassName = ClassName.get(packageName, className);
        TypeSpec pasteClass = TypeSpec.classBuilder(pasteClassName.simpleName().concat(PasteDataProcessor.SUFFIX))
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.DATA_PASTER, TypeName.get(mClassElement.asType())))
                .addMethod(pasteMethod.build())
                .addMethod(unPasteMethod.build())
                .build();
        return JavaFile.builder(packageName, pasteClass).

                build();

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

    public void addSetMethods(ArrayList<MethodDesc> setMethods) {
        this.setMethods = setMethods;
    }

    public void addGetMethodNames(ArrayList<String> getMethodNames) {
        this.getMethodNames = getMethodNames;
    }
}

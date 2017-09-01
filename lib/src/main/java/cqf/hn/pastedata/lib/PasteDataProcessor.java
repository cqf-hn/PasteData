package cqf.hn.pastedata.lib;

import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import cqf.hn.pastedata.lib.annotation.DifField;
import cqf.hn.pastedata.lib.annotation.SrcClass;
import cqf.hn.pastedata.lib.model.DifFieldModel;
import cqf.hn.pastedata.lib.model.FieldDesc;
import cqf.hn.pastedata.lib.model.SrcClassModel;


/**
 * Created by cqf on 2017/8/27 17:51
 */

@AutoService(Processor.class)
public class PasteDataProcessor extends AbstractProcessor {

    /**
     * 元素操作的辅助类
     */
    private Elements elementUtils;
    private Types typeUtils;
    /**
     * 文件相关的辅助类
     */
    private Filer filer;
    /**
     * 日记相关的辅助类
     */
    private Messager messager;
    /**
     * 解析的目标注解集合
     */
    private Map<TypeElement, PasteClass> targetClassMap = new HashMap<>();


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        targetClassMap.clear();
        try {
            processSrcClass(roundEnvironment);
            processDifField(roundEnvironment);

        } catch (IllegalArgumentException e) {
            error(e.getMessage());
            return true;
        }

        try {
            for (Map.Entry<TypeElement, PasteClass> entry : targetClassMap.entrySet()) {
                info("generating file for %s", entry.getValue().getFullClassName());
                //entry.getValue().generateFinder().writeTo(filer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            error("Generate file failed,reason:%s", e.getMessage());
        }
        return true;
    }

    /**
     * 处理SrcClass
     * 用SrcClassModel保存字段Dst的字段
     * 用PasteClass保存Dst的set的方法
     * 通过Dst的set的方法转换get的方法
     * 用PasteClass保存get的方法
     */
    private void processSrcClass(RoundEnvironment roundEnv) {
        for (Element ele : roundEnv.getElementsAnnotatedWith(SrcClass.class)/*获得被该注解声明的元素合集*/) {
            TypeElement typeElement = (TypeElement) ele;
            PasteClass pasteClass = getPasteClass(typeElement);
            SrcClassModel classModel = new SrcClassModel(ele);
            pasteClass.addSrcClass(classModel);
            println("p_element=" + ele.getSimpleName() + ",p_set=" + ele.getModifiers());

            //遍历类的内容
            List<? extends Element> elements = typeElement.getEnclosedElements();
            ArrayList<FieldDesc> fieldDescs = new ArrayList<>();
            ArrayList<String> setMethodNames = new ArrayList<>();
            ArrayList<String> getMethodNames = new ArrayList<>();
            for (Element element : elements) {
                if (element.getKind().equals(ElementKind.FIELD)) {//字段->VariableElement
                    VariableElement varElement = (VariableElement) element;
                    TypeMirror typeMirror = varElement.asType();
                    String fieldName = varElement.getSimpleName().toString();//字段名称
                    String fieldType = typeMirror.toString();//字段类型
                    String fieldTypePackage = "";
                    if (fieldType.contains(".")) {//不是8个基础类型
                        fieldTypePackage = fieldType;
                        fieldType = fieldType.substring(fieldType.lastIndexOf(".") + 1);
                    }
                    FieldDesc fieldDesc = new FieldDesc();
                    fieldDesc.setFieldName(fieldName);
                    fieldDesc.setFieldType(fieldType);
                    fieldDesc.setFieldTypePackage(fieldTypePackage);
                    fieldDescs.add(fieldDesc);
                } else if (element.getKind().equals(ElementKind.CLASS)) {//内部类->TypeElement
                    System.out.println(element);
                } else if (element.getKind().equals(ElementKind.CONSTRUCTOR)) {//构造器->ExecutableElement

                } else if (element.getKind().equals(ElementKind.METHOD)) {//方法->ExecutableElement
                    ExecutableElement exeElement = (ExecutableElement) element;
                    //List<? extends TypeMirror> thrownTypes = exeElement.getThrownTypes();//异常类型
                    //List<? extends VariableElement> params = exeElement.getParameters();//参数
                    //TypeMirror typeMirror = exeElement.getReturnType();//返回类型
                    List<? extends VariableElement> params = exeElement.getParameters();
                    Set<Modifier> modifiers = exeElement.getModifiers();//修饰符public/static/final...
                    if (params.size() == 1 && modifiers.size() == 1 && modifiers.iterator().next().toString().equals("public")) {//只有一个修饰符：public
                        String methodName = exeElement.getSimpleName().toString();//方法名
                        String getMethodName;
                        if (methodName.startsWith("set")) {//以set方法为基准
                            setMethodNames.add(methodName);
                            //转换对应的get方法
                            VariableElement varElement = params.get(0);//获取set方法参数类型
                            TypeMirror typeMirror = varElement.asType();
                            String type = typeMirror.toString();
                            if (type.equals("Boolean") || type.equals("boolean")) {//判断参数类型
                                String str = methodName.replace("set", "");
                                if (str.toLowerCase().equals("is")) {//只有is
                                    if (str.startsWith("Is")) {
                                        getMethodName = str.toLowerCase();
                                    } else {
                                        getMethodName = str;
                                    }
                                } else {//is与其他单词拼接或没有is
                                    if (str.startsWith("iS") || str.startsWith("IS")) {
                                        getMethodName = str;
                                    } else if (str.startsWith("Is")) {
                                        getMethodName = str.replace("Is","is");
                                    } else if (str.startsWith("is")) {
                                        getMethodName = str;
                                    } else {
                                        getMethodName = "is".concat(str);
                                    }
                                }
                            } else {
                                getMethodName = methodName.replace("set", "get");
                            }
                            getMethodNames.add(getMethodName);
                        }
                    }
                } else if (element.getKind().equals(ElementKind.ENUM)) {//枚举->TypeElement
                    System.out.println(element);
                }
            }
            classModel.addFieldDescs(fieldDescs);
            pasteClass.addSetMethodNames(setMethodNames);
            pasteClass.addGetMethodNames(getMethodNames);
        }
    }

    /**
     * 处理DifField
     */
    private void processDifField(RoundEnvironment roundEnv) {
        for (Element ele : roundEnv.getElementsAnnotatedWith(DifField.class)/*获得被该注解声明的元素合集*/) {
            PasteClass pasteClass = getPasteClass((TypeElement) ele.getEnclosingElement());
            DifFieldModel fieldModel = new DifFieldModel(ele);
            pasteClass.addDifField(fieldModel);
        }
    }

    private PasteClass getPasteClass(TypeElement typeElement) {
        // 声明类元素
        String fullClassName = typeElement.getQualifiedName().toString();
        println("fullClassName:" + fullClassName);
        PasteClass pasteClass = targetClassMap.get(typeElement);
        if (pasteClass == null) {
            pasteClass = new PasteClass(typeElement, elementUtils);
            targetClassMap.put(typeElement, pasteClass);
        }
        return pasteClass;
    }

    /**
     * 注册注解（想要处理的注解类型的合法全称）
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<String>();
        types.add(SrcClass.class.getCanonicalName());
        types.add(DifField.class.getCanonicalName());
        return types;
    }

    /**
     * 指定Java版本：通常返回SourceVersion.latestSupported()；
     */
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    private void error(String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void info(String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }

    private void println(String key) {
        System.out.print("shan:" + key);
    }


}

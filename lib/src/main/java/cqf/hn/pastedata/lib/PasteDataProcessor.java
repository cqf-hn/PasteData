package cqf.hn.pastedata.lib;

import com.google.auto.service.AutoService;

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
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import cqf.hn.pastedata.lib.annotation.DifField;
import cqf.hn.pastedata.lib.annotation.SrcClass;
import cqf.hn.pastedata.lib.model.DifFieldModel;
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
    ;

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

    private void processSrcClass(RoundEnvironment roundEnv) {
        for (Element ele : roundEnv.getElementsAnnotatedWith(SrcClass.class)/*获得被该注解声明的元素合集*/) {
            TypeElement typeElement = (TypeElement) ele;
            PasteClass pasteClass = getPasteClass(typeElement);
            SrcClassModel classModel = new SrcClassModel(ele);
            pasteClass.addSrcClass(classModel);
            println("p_element=" + ele.getSimpleName() + ",p_set=" + ele.getModifiers());
            //遍历类的内容
            List<? extends Element> elements = typeElement.getEnclosedElements();
            for (Element element : elements) {
                if (element.getKind().equals(ElementKind.FIELD)) {//字段->VariableElement
                    VariableElement varElement = (VariableElement) element;
                    TypeMirror typeMirror = varElement.asType();
                    Name name = varElement.getSimpleName();
                    String fieldName = name.toString();//字段名称
                    String fieldType = typeMirror.toString();//字段类型
                } else if (element.getKind().equals(ElementKind.CLASS)) {//内部类->TypeElement
                    System.out.println(element);
                } else if (element.getKind().equals(ElementKind.CONSTRUCTOR)) {//构造器->ExecutableElement
                    System.out.println(element);
                } else if (element.getKind().equals(ElementKind.METHOD)) {//方法->ExecutableElement
                    System.out.println(element);
                } else if (element.getKind().equals(ElementKind.ENUM)) {//枚举->TypeElement
                    System.out.println(element);
                }
            }

        }
    }

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

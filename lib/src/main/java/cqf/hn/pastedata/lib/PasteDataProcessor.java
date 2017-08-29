package cqf.hn.pastedata.lib;

import com.google.auto.service.AutoService;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;
import static javax.tools.Diagnostic.Kind.OTHER;
import static javax.tools.Diagnostic.Kind.WARNING;

/**
 * Created by cqf on 2017/8/27 17:51
 */

@AutoService(Processor.class)
public class PasteDataProcessor extends AbstractProcessor {

    // 元素操作的辅助类
    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //Map<TypeElement, PasteClass> targetClassMap = findAndParseTargets(roundEnvironment);

        // 获得被该注解声明的元素
        Set<? extends Element> elements1 = roundEnvironment.getElementsAnnotatedWith(DifField.class);
        Set<? extends Element> elements2 = roundEnvironment.getElementsAnnotatedWith(SrcClass.class);
        TypeElement classElement = null;// 声明类元素
        List<VariableElement> fields = null;// 声明一个存放成员变量的列表
        Map<String, List<VariableElement>> maps = new HashMap<>();
        for (Element element : elements1) {
            // 判断该元素是否为类

            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            String key = enclosingElement.getQualifiedName().toString();
        }
        return true;
    }

    private Map<TypeElement, PasteClass> findAndParseTargets(RoundEnvironment environment) {

        return null;
    }

    private void println(String key) {
        System.out.print("shan:" + key);
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

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(ERROR, message, element);
    }

    private void warning(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(WARNING, message, element);
    }

    private void note(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(NOTE, message, element);
    }

    private void other(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(OTHER, message, element);
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }
}

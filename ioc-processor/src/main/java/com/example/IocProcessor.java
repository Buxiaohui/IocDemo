package com.example;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;


/**
 * Created by buxiaohui on 6/15/17.
 */

@AutoService(Processor.class)
public class IocProcessor extends AbstractProcessor {

    /**
     * supportedOptions用来表示所支持的附加选项。在运行apt命令行工具的时候，
     * 可以通过-A来传递额外的参数给注解处理器，如-Averbose=true。
     * 当工厂通过 supportedOptions方法声明了所能识别的附加选项之后，
     * 注解处理器就可以在运行时刻通过AnnotationProcessorEnvironment的getOptions方法获取到选项的实际值。
     * */
    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    /**
     * supportedAnnotationTypes是返回该工厂生成的注解处理器所能支持的注解类型；
     * */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //return Collections.singleton(IocBindView.class.getCanonicalName());
        //return super.getSupportedAnnotationTypes();
        //Collections.emptySet() 返回的set不允许修改
        //Set<String> set = Collections.emptySet();
        Set<String> set = new LinkedHashSet<>();
        set.add(IocBindView.class.getCanonicalName());
        set.add(IocBindClick.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
        //return super.getSupportedSourceVersion();
    }
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        messager = processingEnvironment.getMessager();
        /**
         * Kind是enum:
         * ERROR,
         * WARNING,
         * MANDATORY_WARNING,
         * NOTE,
         * OTHER;
         * */
        messager.printMessage(Diagnostic.Kind.NOTE,"---IocProcessor init---");
        super.init(processingEnvironment);
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotationMirror, ExecutableElement executableElement, String s) {
        return super.getCompletions(element, annotationMirror, executableElement, s);
    }

    @Override
    protected synchronized boolean isInitialized() {
        return super.isInitialized();
    }

    /**
     * javapoet:https://github.com/square/javapoet
     * 一个用于生成java文件的开元库
     * */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> iocBindViewElements = roundEnvironment.getElementsAnnotatedWith(IocBindView.class);
        Set<? extends Element> iocBindClickElements = roundEnvironment.getElementsAnnotatedWith(IocBindClick.class);
        for (Element  element:  iocBindViewElements) {
            if(checkAnnotationValid(element,IocBindView.class)){
                error(element,"invalid element");
                continue;
            }
            messager.printMessage(Diagnostic.Kind.NOTE,"process -- element.getSimpleName()="+element.getSimpleName());
            VariableElement variableElement = ((VariableElement) element);
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();

            // 全路径名
            String qualifiedName = typeElement.getQualifiedName().toString();
            messager.printMessage(Diagnostic.Kind.NOTE,"process -- element.getQualifiedName()="+qualifiedName);
            MethodSpec main = MethodSpec.methodBuilder("main")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(void.class)
                    .addParameter(String[].class, "args")
                    .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                    .build();

            TypeSpec classType = TypeSpec.classBuilder("HelloWorld")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(main)
                    .build();

            JavaFile javaFile = JavaFile.builder("com.songwenju.aptproject", classType)
                    .build();

            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 检查查找的元素是否合法
     */
    private boolean checkAnnotationValid(Element annotatedElement, Class clazz) {
        if (annotatedElement.getKind() != ElementKind.FIELD) {
            error(annotatedElement, "%s must be declared on field.", clazz.getSimpleName());
            return false;
        }
        if (ClassValidator.isPrivate(annotatedElement)) {
            error(annotatedElement, "%s() must can not be private.", annotatedElement.getSimpleName());
            return false;
        }
        return true;
    }

    // 展示错误信息
    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        messager.printMessage(Diagnostic.Kind.NOTE, message, element);
    }
}

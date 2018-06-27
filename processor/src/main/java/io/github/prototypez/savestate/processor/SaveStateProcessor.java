package io.github.prototypez.savestate.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import io.github.prototypez.savestate.core.annotation.AutoRestore;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SaveStateProcessor extends AbstractProcessor {

    private String serializer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(
                Collections.singletonList(
                        AutoRestore.class.getCanonicalName()
                )
        );
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Map<String, String> options = processingEnv.getOptions();
        if (options != null && options.size() > 0) {
            serializer = options.get(Constant.KEY_SERIALIZER);
        }

        Set<? extends Element> autoRestoreClasses = roundEnvironment
                .getElementsAnnotatedWith(AutoRestore.class);

        Set<Element> saveStateClasses = new HashSet<>();
        if (autoRestoreClasses != null) {
            for (Element element : autoRestoreClasses) {
                TypeElement te = (TypeElement) element.getEnclosingElement();
                if (!saveStateClasses.contains(te)) {
                    saveStateClasses.add(te);
                }
            }
        }


        for (Element element : saveStateClasses) {
            if (element.getKind() != ElementKind.CLASS) {
                continue;
            }

            boolean isKotlinClass = false;
            List<? extends AnnotationMirror> list = processingEnv.getElementUtils()
                    .getAllAnnotationMirrors(element);
            for (int i = 0; list != null && i < list.size(); i++) {
                if ("kotlin.Metadata".equals(((TypeElement)list.get(i).getAnnotationType().asElement()).getQualifiedName().toString())) {
                    isKotlinClass = true;
                    break;
                }
            }

            Generator generator;
            if (checkIsSubClassOf(
                    element,
                    Constant.CLASS_ACTIVITY, Constant.CLASS_FRAGMENT_ACTIVITY,
                    Constant.CLASS_FRAGMENT, Constant.CLASS_V4_FRAGMENT)) {
                generator = new CommonSaveStateGenerator(isKotlinClass, element, serializer);
            } else if (checkIsSubClassOf(element, Constant.CLASS_VIEW)) {
                generator = new ViewSaveStateGenerator(isKotlinClass, element, serializer);
            } else {
                continue;
            }


            JavaFile javaFile;
            javaFile = generator.createSaveStateSourceFile();

            // Finally, write the source to file
            try {
                if (javaFile != null) {
                    javaFile.writeTo(processingEnv.getFiler());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private boolean checkIsSubClassOf(Element element, String... superClasses) {
        Elements elementUtils = processingEnv.getElementUtils();
        Types typeUtils = processingEnv.getTypeUtils();
        for (String clazz : superClasses) {
            boolean isSubType = typeUtils.isSubtype(
                    element.asType(),
                    elementUtils.getTypeElement(clazz).asType()
            );
            if (isSubType) return true;
        }
        return false;
    }

    private void error(String error) {
        messager.printMessage(Diagnostic.Kind.ERROR, error);
    }

    private void debug(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

}

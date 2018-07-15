package io.github.prototypez.savestate.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;

import io.github.prototypez.savestate.core.annotation.AutoRestore;

import static io.github.prototypez.savestate.processor.Constant.BUNDLE_CLASS;
import static io.github.prototypez.savestate.processor.Constant.SERIALIZER_FASTJSON;
import static io.github.prototypez.savestate.processor.Constant.SERIALIZER_GSON;
import static io.github.prototypez.savestate.processor.Constant.SERIALIZER_JACKSON;

public class CommonSaveStateGenerator implements Generator {

    protected Element element;
    protected String serializer;
    private boolean isKotlinClass;
    private ProcessingEnvironment processingEnv;

    public CommonSaveStateGenerator(ProcessingEnvironment processingEnv, boolean isKotlinClass, Element element, String serializer) {
        this.element = element;
        this.serializer = serializer;
        this.isKotlinClass = isKotlinClass;
        this.processingEnv = processingEnv;
    }

    @Override
    public JavaFile createSaveStateSourceFile() {
        String className = element.getSimpleName().toString();
        String packageName = ((PackageElement) element.getEnclosingElement()).getQualifiedName()
                .toString();

        List<? extends Element> enclosedElementsInside =
                element.getEnclosedElements();

        List<Element> autoRestoreFields = new ArrayList<>();

        for (Element testField : enclosedElementsInside) {
            if (testField.getKind() == ElementKind.FIELD && testField.getAnnotation(AutoRestore.class) != null) {
                autoRestoreFields.add(testField);
            }
        }

        if (autoRestoreFields.size() == 0) return null;

        TypeSpec.Builder saveStateClass = TypeSpec.classBuilder(className + "AutoSaveState")
                .addModifiers(Modifier.PUBLIC);

        if (serializer != null) {
            FieldSpec serializerField;
            switch (serializer) {
                case SERIALIZER_GSON:
                    serializerField = FieldSpec.builder(
                            Gson.class,
                            "serializer",
                            Modifier.STATIC, Modifier.FINAL
                    )
                            .initializer("new $T()", Gson.class)
                            .build();
                    saveStateClass.addField(serializerField);
                    break;
                case SERIALIZER_FASTJSON:
//                serializerType
                    break;
                case SERIALIZER_JACKSON:
                    serializerField = FieldSpec.builder(
                            ObjectMapper.class,
                            "serializer",
                            Modifier.STATIC, Modifier.FINAL
                    )
                            .initializer("new $T()", ObjectMapper.class)
                            .build();
                    saveStateClass.addField(serializerField);
                    break;
            }
        }


        addMethodsAndFields(saveStateClass, autoRestoreFields);

        // Create
        TypeSpec saveState = saveStateClass.build();

        return JavaFile.builder(
                packageName,
                saveState
        )
                .build();
    }


    protected void addMethodsAndFields(TypeSpec.Builder saveStateClass, List<Element> autoRestoreFields) {
        MethodSpec.Builder saveStateMethodBuilder = MethodSpec
                .methodBuilder("onSaveInstanceState")
                .addParameter(TypeName.get(element.asType()), "instance")
                .addParameter(BUNDLE_CLASS, "outState")
                .addModifiers(Modifier.STATIC);

        for (Element field : autoRestoreFields) {
            BundleStateHelper.statementSaveValueIntoBundle(processingEnv, isKotlinClass, saveStateMethodBuilder,
                    field, "instance", "outState", serializer);

        }

        saveStateClass.addMethod(saveStateMethodBuilder.build());

        MethodSpec.Builder restoreStateMethodBuilder = MethodSpec
                .methodBuilder("onRestoreInstanceState")
                .addParameter(TypeName.get(element.asType()), "instance")
                .addParameter(BUNDLE_CLASS, "savedInstanceState")
                .addModifiers(Modifier.STATIC);

        for (Element field : autoRestoreFields) {
            BundleStateHelper.statementGetValueFromBundle(processingEnv, isKotlinClass, restoreStateMethodBuilder,
                    field, "instance", "savedInstanceState", serializer);
        }

        saveStateClass.addMethod(restoreStateMethodBuilder.build());
    }

}

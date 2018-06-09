package io.github.prototypez.savestate.processor;

import com.google.gson.Gson;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;

import io.github.prototypez.savestate.core.annotation.AutoRestore;

import static io.github.prototypez.savestate.processor.Constant.BUNDLE_CLASS;
import static io.github.prototypez.savestate.processor.Constant.SERIALIZER_FASTJSON;
import static io.github.prototypez.savestate.processor.Constant.SERIALIZER_GSON;

public class CommonSaveStateGenerator implements Generator {

    protected Element element;
    protected String serializer;

    public CommonSaveStateGenerator(Element element, String serializer) {
        this.element = element;
        this.serializer = serializer;
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

        switch (serializer) {
            case SERIALIZER_GSON:
                FieldSpec serializerField = FieldSpec.builder(
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
            BundleStateHelper.statementSaveValueIntoBundle(saveStateMethodBuilder,
                    field, "instance", "outState", serializer);

        }

        saveStateClass.addMethod(saveStateMethodBuilder.build());

        MethodSpec.Builder restoreStateMethodBuilder = MethodSpec
                .methodBuilder("onRestoreInstanceState")
                .addParameter(TypeName.get(element.asType()), "instance")
                .addParameter(BUNDLE_CLASS, "savedInstanceState")
                .addModifiers(Modifier.STATIC);

        for (Element field : autoRestoreFields) {
            BundleStateHelper.statementGetValueFromBundle(restoreStateMethodBuilder,
                    field, "instance", "savedInstanceState", serializer);
        }

        saveStateClass.addMethod(restoreStateMethodBuilder.build());
    }

}

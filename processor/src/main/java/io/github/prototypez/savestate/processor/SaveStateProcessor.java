package io.github.prototypez.savestate.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import io.github.prototypez.savestate.core.annotation.AutoRestore;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SaveStateProcessor extends AbstractProcessor {

    private static final ClassName BUNDLE_CLASS = ClassName.get("android.os", "Bundle");

    private static final String KEY_SERIALIZER = "serializer";
    private static final String SERIALIZER_GSON = "gson";
    private static final String SERIALIZER_FASTJSON = "fastjson";


    private String serializer;

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
            serializer = options.get(KEY_SERIALIZER);
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
            createSaveStateClassForActivity(element);
        }

        return true;
    }


    private void createSaveStateClassForActivity(Element element) {
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

        if (autoRestoreFields.size() == 0) return;

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

        MethodSpec.Builder saveStateMethodBuilder = MethodSpec
                .methodBuilder("onSaveInstanceState")
                .addParameter(TypeName.get(element.asType()), "instance")
                .addParameter(BUNDLE_CLASS, "outState")
                .addModifiers(Modifier.STATIC);

        for (Element field : autoRestoreFields) {
            statementSaveValueIntoBundle(saveStateMethodBuilder, field, "instance", "outState");

        }

        saveStateClass.addMethod(saveStateMethodBuilder.build());

        MethodSpec.Builder restoreStateMethodBuilder = MethodSpec
                .methodBuilder("onRestoreInstanceState")
                .addParameter(TypeName.get(element.asType()), "instance")
                .addParameter(BUNDLE_CLASS, "savedInstanceState")
                .addModifiers(Modifier.STATIC);

        for (Element field : autoRestoreFields) {
            statementGetValueFromBundle(restoreStateMethodBuilder, field, "instance", "savedInstanceState");
        }

        saveStateClass.addMethod(restoreStateMethodBuilder.build());


        // Create
        TypeSpec saveState = saveStateClass.build();
        JavaFile javaFile = JavaFile.builder(
                packageName,
                saveState
        ).build();
        // Finally, write the source to file
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private MethodSpec.Builder statementSaveValueIntoBundle(MethodSpec.Builder methodBuilder, Element element, String instance, String bundleName) {
        String statement;
        String varName = element.getSimpleName().toString();
        switch (element.asType().toString()) {
            case "int":
                statement = String.format("%s.putInt(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                methodBuilder.addStatement(statement);
                break;
            case "java.lang.String":
                statement = String.format("%s.putString(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                methodBuilder.addStatement(statement);
                break;
            default:
                if (SERIALIZER_GSON.equals(serializer)) {
                    statement = String.format("%s.putString(%s, %s)", bundleName, "\"" + varName + "\"", "serializer.toJson(" + instance + "." + varName + ")");
                    methodBuilder.addStatement(statement);
                } else if (SERIALIZER_FASTJSON.equals(serializer)) {
                    statement = String.format("%s.putString(%s, %s)", bundleName, "\"" + varName + "\"", "$T.toJSONString(" + instance + "." + varName + ")");
                    methodBuilder.addStatement(statement, JSON.class);
                }
        }
        return methodBuilder;
    }

    private MethodSpec.Builder statementGetValueFromBundle(MethodSpec.Builder methodBuilder, Element element, String instance, String bundleName) {
        String statement;
        String varName = element.getSimpleName().toString();
        switch (element.asType().toString()) {
            case "int":
                statement = String.format("%s = %s.getInt(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                methodBuilder.addStatement(statement);
                break;
            case "java.lang.String":
                statement = String.format("%s = %s.getString(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                methodBuilder.addStatement(statement);
                break;
            default:
                if (SERIALIZER_GSON.equals(serializer)) {
                    statement = String.format("%s = serializer.fromJson(%s.getString(%s), new $T<%s>(){}.getType())", instance + "." + varName, bundleName, "\"" + varName + "\"", element.asType().toString());
                    methodBuilder.addStatement(statement, TypeToken.class);
                } else if (SERIALIZER_FASTJSON.equals(serializer)) {
                    statement = String.format("%s = $T.parseObject(%s.getString(%s), new $T<%s>(){}.getType())", instance + "." + varName, bundleName, "\"" + varName + "\"", element.asType().toString());
                    methodBuilder.addStatement(statement, JSON.class, TypeReference.class);
                }
        }
        return methodBuilder;
    }
}

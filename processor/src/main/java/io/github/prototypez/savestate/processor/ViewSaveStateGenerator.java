package io.github.prototypez.savestate.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import static io.github.prototypez.savestate.processor.Constant.PARCELABLE_CLASS;
import static io.github.prototypez.savestate.processor.Constant.SUPER_STATE;

public class ViewSaveStateGenerator extends CommonSaveStateGenerator {

    public ViewSaveStateGenerator(boolean isKotlinClass, Element element, String serializer) {
        super(isKotlinClass, element, serializer);
    }

    @Override
    protected void addMethodsAndFields(TypeSpec.Builder saveStateClass, List<Element> autoRestoreFields) {
        super.addMethodsAndFields(saveStateClass, autoRestoreFields);

        MethodSpec.Builder saveStateMethodBuilder = MethodSpec
                .methodBuilder("onSaveInstanceState")
                .addParameter(TypeName.get(element.asType()), "instance")
                .addParameter(PARCELABLE_CLASS, "superState")
                .addModifiers(Modifier.STATIC)
                .returns(PARCELABLE_CLASS);

        saveStateMethodBuilder
                .addStatement("Bundle bundle = new Bundle()")
                .addStatement(String.format("bundle.putParcelable(\"%s\", superState)", SUPER_STATE))
                .addStatement("onSaveInstanceState(instance, bundle)")
                .addStatement("return bundle");



        saveStateClass.addMethod(saveStateMethodBuilder.build());

        MethodSpec.Builder restoreStateMethodBuilder = MethodSpec
                .methodBuilder("onRestoreInstanceState")
                .addParameter(TypeName.get(element.asType()), "instance")
                .addParameter(PARCELABLE_CLASS, "state")
                .returns(PARCELABLE_CLASS)
                .addModifiers(Modifier.STATIC);

        restoreStateMethodBuilder
                .addCode(
                        CodeBlock.builder()
                                .beginControlFlow("if (state instanceof Bundle)")
                                .addStatement("Bundle bundle = (Bundle) state")
                                .addStatement(String.format("state = bundle.getParcelable(\"%s\")", SUPER_STATE))
                                .addStatement("onRestoreInstanceState(instance, bundle)")
                                .endControlFlow()
                                .addStatement("return state")
                                .build()
                );

        saveStateClass.addMethod(restoreStateMethodBuilder.build());


    }
}

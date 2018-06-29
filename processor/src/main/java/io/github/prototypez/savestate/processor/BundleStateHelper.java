package io.github.prototypez.savestate.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.reflect.TypeToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;

import java.io.IOException;

import javax.lang.model.element.Element;

import static io.github.prototypez.savestate.processor.Constant.SERIALIZER_FASTJSON;
import static io.github.prototypez.savestate.processor.Constant.SERIALIZER_GSON;
import static io.github.prototypez.savestate.processor.Constant.SERIALIZER_JACKSON;

class BundleStateHelper {

    private static ClassName androidLogClassName = ClassName.get("android.util", "Log");

    static MethodSpec.Builder statementSaveValueIntoBundle(boolean isKotlinClass, MethodSpec.Builder methodBuilder, Element element, String instance, String bundleName, String serializer) {
        String statement = null;
        String varName = element.getSimpleName().toString();
        switch (element.asType().toString()) {
            case "int":
                statement = String.format("%s.putInt(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "long":
                statement = String.format("%s.putLong(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "char":
                statement = String.format("%s.putChar(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "short":
                statement = String.format("%s.putShort(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "byte":
                statement = String.format("%s.putByte(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "float":
                statement = String.format("%s.putFloat(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "double":
                statement = String.format("%s.putDouble(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "boolean":
                statement = String.format("%s.putBoolean(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "java.lang.Integer":
            case "java.lang.Long":
            case "java.lang.Character":
            case "java.lang.Short":
            case "java.lang.Byte":
            case "java.lang.Float":
            case "java.lang.Double":
            case "java.lang.Boolean":
                statement = String.format("%s.putSerializable(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "java.lang.String":
                statement = String.format("%s.putString(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "java.io.Serializable":
                statement = String.format("%s.putSerializable(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "android.os.IBinder":
                statement = String.format("%s.putBinder(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "android.os.Bundle":
                statement = String.format("%s.putBundle(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "java.lang.CharSequence":
                statement = String.format("%s.putCharSequence(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "android.os.Parcelable":
                statement = String.format("%s.putParcelable(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "android.util.Size":
                statement = String.format("%s.putSize(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "android.util.SizeF":
                statement = String.format("%s.putSizeF(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "android.os.Parcelable[]":
                statement = String.format("%s.putParcelableArray(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "byte[]":
                statement = String.format("%s.putByteArray(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "short[]":
                statement = String.format("%s.putShortArray(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "char[]":
                statement = String.format("%s.putCharArray(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "float[]":
                statement = String.format("%s.putFloatArray(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            case "java.lang.CharSequence[]":
                statement = String.format("%s.putCharSequenceArray(%s, %s)", bundleName, "\"" + varName + "\"",
                        getStatement(isKotlinClass, instance, varName));
                break;
            default:
                if (SERIALIZER_GSON.equals(serializer)) {
                    statement = String.format(
                            "%s.putString(%s, %s)",
                            bundleName,
                            "\"" + varName + "\"",
                            "serializer.toJson(" + getStatement(isKotlinClass, instance, varName) + ")"
                    );
                    methodBuilder.addStatement(statement);
                    statement = null;
                } else if (SERIALIZER_FASTJSON.equals(serializer)) {
                    statement = String.format(
                            "%s.putString(%s, %s)",
                            bundleName,
                            "\"" + varName + "\"",
                            "$T.toJSONString(" + getStatement(isKotlinClass, instance, varName) + ")"
                    );
                    methodBuilder.addStatement(statement, JSON.class);
                    statement = null;
                } else if (SERIALIZER_JACKSON.equals(serializer)) {
                    methodBuilder.addCode(
                            CodeBlock.builder()
                                    .beginControlFlow("try")
                                    .addStatement(
                                            String.format(
                                                    "%s.putString(%s, %s)",
                                                    bundleName,
                                                    "\"" + varName + "\"",
                                                    "serializer.writeValueAsString(" + getStatement(isKotlinClass, instance, varName) + ")"
                                            )
                                    )
                                    .nextControlFlow("catch($T e)", JsonProcessingException.class)
                                    .addStatement(String.format("$T.e(\"SaveState\", \"Error in saving field %s\", e)", varName), androidLogClassName)
                                    .endControlFlow()
                                    .build()
                    );
                }
        }
        if (statement != null) {
            methodBuilder.addStatement(statement);
        }
        return methodBuilder;
    }

    static MethodSpec.Builder statementGetValueFromBundle(boolean isKotlinClass, MethodSpec.Builder methodBuilder, Element element, String instance, String bundleName, String serializer) {
        String statement = null;
        String varName = element.getSimpleName().toString();
        switch (element.asType().toString()) {
            case "int":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getInt(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "long":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getLong(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "char":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getChar(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "short":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getShort(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "byte":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getByte(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "float":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getFloat(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "double":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getDouble(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "boolean":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getBoolean(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "java.lang.Integer":
            case "java.lang.Long":
            case "java.lang.Character":
            case "java.lang.Short":
            case "java.lang.Byte":
            case "java.lang.Float":
            case "java.lang.Double":
            case "java.lang.Boolean":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format(
                                "(%s)%s.getSerializable(%s)",
                                element.asType().toString(),
                                bundleName, "\"" + varName + "\""
                        )
                );
                break;
            case "java.lang.String":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getString(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "java.io.Serializable":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getSerializable(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "android.os.IBinder":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getBinder(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "android.os.Bundle":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getBundle(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "java.lang.CharSequence":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getCharSequence(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "android.os.Parcelable":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getParcelable(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "android.util.Size":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getSize(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "android.util.SizeF":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getSizeF(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "android.os.Parcelable[]":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getParcelableArray(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "byte[]":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getByteArray(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "short[]":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getShortArray(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "char[]":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getCharArray(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "float[]":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getFloatArray(%s)", bundleName, "\"" + varName + "\""));
                break;
            case "java.lang.CharSequence[]":
                statement = assignStatement(isKotlinClass, instance, varName,
                        String.format("%s.getCharSequenceArray(%s)", bundleName, "\"" + varName + "\""));
                break;
            default:
                if (SERIALIZER_GSON.equals(serializer)) {
                    statement = assignStatement(isKotlinClass, instance, varName, String.format(
                            "serializer.<%s>fromJson(%s.getString(%s), new $T<%s>(){}.getType())",
                            element.asType().toString(),
                            bundleName,
                            "\"" + varName + "\"",
                            element.asType().toString()
                    ));
                    methodBuilder.addStatement(statement, TypeToken.class);
                    statement = null;
                } else if (SERIALIZER_FASTJSON.equals(serializer)) {
                    statement = assignStatement(isKotlinClass, instance, varName, String.format(
                            "$T.<%s>parseObject(%s.getString(%s), new $T<%s>(){}.getType())",
                            element.asType().toString(),
                            bundleName,
                            "\"" + varName + "\"",
                            element.asType().toString()
                    ));
                    methodBuilder.addStatement(statement, JSON.class, TypeReference.class);
                    statement = null;
                } else if (SERIALIZER_JACKSON.equals(serializer)) {
                    methodBuilder.addCode(
                            CodeBlock.builder()
                                    .beginControlFlow("try")
                                    .addStatement(
                                            assignStatement(isKotlinClass, instance, varName,
                                                    String.format(
                                                            "serializer.<%s>readValue(%s.getString(%s), new $T<%s>(){})",
                                                            element.asType().toString(),
                                                            bundleName,
                                                            "\"" + varName + "\"",
                                                            element.asType().toString()
                                                    )),
                                            com.fasterxml.jackson.core.type.TypeReference.class
                                    )
                                    .nextControlFlow("catch($T e)", IOException.class)
                                    .addStatement(String.format("$T.e(\"SaveState\", \"Error in restoring field %s\", e)", varName), androidLogClassName)
                                    .endControlFlow()
                                    .build()
                    );
                }
        }
        if (statement != null) {
            methodBuilder.addStatement(statement);
        }
        return methodBuilder;
    }

    private static String kotlinSetterForVar(String var) {
        if (var.startsWith("is")) {
            return var.replaceFirst("is", "set");
        } else {
            return "set" + var.substring(0, 1).toUpperCase() +
                    (var.length() > 1 ? var.substring(1) : "");
        }
    }

    private static String kotlinGetterForVar(String var) {
        if (var.startsWith("is")) {
            return var;
        } else {
            return "get" + var.substring(0, 1).toUpperCase() +
                    (var.length() > 1 ? var.substring(1) : "");
        }
    }

    private static String javaAssignStatement(String instance, String field, String value) {
        return String.format("%s.%s = %s", instance, field, value);
    }

    private static String kotlinAssignStatement(String instance, String field, String value) {
        return String.format("%s.%s(%s)", instance, kotlinSetterForVar(field), value);
    }

    private static String assignStatement(boolean isKotlinClass, String instance, String field, String value) {
        return isKotlinClass ? kotlinAssignStatement(instance, field, value)
                : javaAssignStatement(instance, field, value);
    }

    private static String javaGetStatement(String instance, String field) {
        return String.format("%s.%s", instance, field);
    }

    private static String kotlinGetStatement(String instance, String field) {
        return String.format("%s.%s()", instance, kotlinGetterForVar(field));
    }

    private static String getStatement(boolean isKotlinClass, String instance, String field) {
        return isKotlinClass ? kotlinGetStatement(instance, field) : javaGetStatement(instance, field);
    }
}

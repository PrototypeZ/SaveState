package io.github.prototypez.savestate.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.reflect.TypeToken;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Element;

import static io.github.prototypez.savestate.processor.Constant.SERIALIZER_FASTJSON;
import static io.github.prototypez.savestate.processor.Constant.SERIALIZER_GSON;

class BundleStateHelper {

    static MethodSpec.Builder statementSaveValueIntoBundle(MethodSpec.Builder methodBuilder, Element element, String instance, String bundleName, String serializer) {
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

    static MethodSpec.Builder statementGetValueFromBundle(MethodSpec.Builder methodBuilder, Element element, String instance, String bundleName, String serializer) {
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

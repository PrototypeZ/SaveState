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
        String statement = null;
        String varName = element.getSimpleName().toString();
        switch (element.asType().toString()) {
            case "int":
            case "java.lang.Integer":
                statement = String.format("%s.putInt(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            case "long":
            case "java.lang.Long":
                statement = String.format("%s.putLong(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            case "char":
            case "java.lang.Character":
                statement = String.format("%s.putChar(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            case "short":
            case "java.lang.Short":
                statement = String.format("%s.putShort(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            case "byte":
            case "java.lang.Byte":
                statement = String.format("%s.putByte(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            case "float":
            case "java.lang.Float":
                statement = String.format("%s.putFloat(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            case "double":
            case "java.lang.Double":
                statement = String.format("%s.putDouble(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            case "boolean":
            case "java.lang.Boolean":
                statement = String.format("%s.putBoolean(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            case "java.lang.String":
                statement = String.format("%s.putString(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            case "java.io.Serializable":
                statement = String.format("%s.putSerializable(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            case "android.os.IBinder":
                statement = String.format("%s.putBinder(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            case "android.os.Bundle":
                statement = String.format("%s.putBundle(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            case "java.lang.CharSequence":
                statement = String.format("%s.putCharSequence(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            case "android.os.Parcelable":
                statement = String.format("%s.putParcelable(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            case "android.util.Size":
                statement = String.format("%s.putSize(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            case "android.util.SizeF":
                statement = String.format("%s.putSizeF(%s, %s)", bundleName, "\"" + varName + "\"", instance + "." + varName);
                break;
            default:
                if (SERIALIZER_GSON.equals(serializer)) {
                    statement = String.format("%s.putString(%s, %s)", bundleName, "\"" + varName + "\"", "serializer.toJson(" + instance + "." + varName + ")");
                } else if (SERIALIZER_FASTJSON.equals(serializer)) {
                    statement = String.format("%s.putString(%s, %s)", bundleName, "\"" + varName + "\"", "$T.toJSONString(" + instance + "." + varName + ")");
                }
        }
        if (statement != null) {
            methodBuilder.addStatement(statement, JSON.class);
        }
        return methodBuilder;
    }

    static MethodSpec.Builder statementGetValueFromBundle(MethodSpec.Builder methodBuilder, Element element, String instance, String bundleName, String serializer) {
        String statement = null;
        String varName = element.getSimpleName().toString();
        switch (element.asType().toString()) {
            case "int":
            case "java.lang.Integer":
                statement = String.format("%s = %s.getInt(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            case "long":
            case "java.lang.Long":
                statement = String.format("%s = %s.getLong(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            case "char":
            case "java.lang.Character":
                statement = String.format("%s = %s.getChar(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            case "short":
            case "java.lang.Short":
                statement = String.format("%s = %s.getShort(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            case "byte":
            case "java.lang.Byte":
                statement = String.format("%s = %s.getByte(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            case "float":
            case "java.lang.Float":
                statement = String.format("%s = %s.getFloat(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            case "double":
            case "java.lang.Double":
                statement = String.format("%s = %s.getDouble(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            case "boolean":
            case "java.lang.Boolean":
                statement = String.format("%s = %s.getBoolean(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            case "java.lang.String":
                statement = String.format("%s = %s.getString(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            case "java.io.Serializable":
                statement = String.format("%s = %s.getSerializable(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            case "android.os.IBinder":
                statement = String.format("%s = %s.getBinder(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            case "android.os.Bundle":
                statement = String.format("%s = %s.getBundle(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            case "java.lang.CharSequence":
                statement = String.format("%s = %s.getCharSequence(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            case "android.os.Parcelable":
                statement = String.format("%s = %s.getParcelable(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            case "android.util.Size":
                statement = String.format("%s = %s.getSize(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            case "android.util.SizeF":
                statement = String.format("%s = %s.getSizeF(%s)", instance + "." + varName, bundleName, "\"" + varName + "\"");
                break;
            default:
                if (SERIALIZER_GSON.equals(serializer)) {
                    statement = String.format("%s = serializer.fromJson(%s.getString(%s), new $T<%s>(){}.getType())", instance + "." + varName, bundleName, "\"" + varName + "\"", element.asType().toString());
                    methodBuilder.addStatement(statement, TypeToken.class);
                } else if (SERIALIZER_FASTJSON.equals(serializer)) {
                    statement = String.format("%s = $T.parseObject(%s.getString(%s), new $T<%s>(){}.getType())", instance + "." + varName, bundleName, "\"" + varName + "\"", element.asType().toString());
                }
        }
        if (statement != null) {
            methodBuilder.addStatement(statement, JSON.class, TypeReference.class);
        }
        return methodBuilder;
    }
}

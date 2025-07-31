package com.interguess.autoimpl.annotationprocessor.collectors;

import com.interguess.autoimpl.api.field.GeneratedField;
import com.interguess.autoimpl.api.method.MethodType;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.ExecutableElement;
import java.util.*;

public class FieldCollector {

    @NotNull
    private final Map<String, String> fieldNameToType = new LinkedHashMap<>();

    @NotNull
    private final List<String> allMethodCodes = new ArrayList<>();

    public void collect(@NotNull ExecutableElement method, @NotNull MethodType methodType) {
        final String code = methodType.generateCode(method);

        allMethodCodes.add(code);

        for (final GeneratedField field : methodType.generateFields(method)) {
            fieldNameToType.put(field.getName(), field.getType().toString());
        }
    }

    public @NotNull String generateFieldsCode() {
        final Set<String> nonFinalFields = new HashSet<>();

        for (final String fieldName : fieldNameToType.keySet()) {
            final String search = "this." + fieldName + " =";

            for (final String code : allMethodCodes) {
                if (code.contains(search)) {
                    nonFinalFields.add(fieldName);

                    break;
                }
            }
        }

        final Map<String, List<String>> fieldsByType = new LinkedHashMap<>();

        for (final Map.Entry<String, String> entry : fieldNameToType.entrySet()) {
            final String fieldName = entry.getKey();
            final String fieldType = entry.getValue();

            fieldsByType.computeIfAbsent(fieldType, k -> new ArrayList<>()).add(fieldName);
        }

        final StringBuilder builder = new StringBuilder();

        int i = 0, typeCount = fieldsByType.size();

        for (final Map.Entry<String, List<String>> entry : fieldsByType.entrySet()) {
            final String type = entry.getKey();

            final List<String> fieldNames = entry.getValue();

            for (final String fieldName : fieldNames) {
                boolean isFinal = !nonFinalFields.contains(fieldName);

                builder
                        .append("    private ")
                        .append(isFinal ? "final " : "")
                        .append(type)
                        .append(" ")
                        .append(fieldName)
                        .append(";")
                        .append("\n");
            }

            i++;

            if (i < typeCount) {
                builder.append("\n");
            }
        }

        builder.append("\n");

        return builder.toString();
    }

    public String generateCtorCode(String className) {
        final List<String> finalFieldNames = new ArrayList<>();
        final List<String> finalFieldTypes = new ArrayList<>();

        final Set<String> nonFinalFields = new HashSet<>();

        for (final String fieldName : fieldNameToType.keySet()) {
            final String search = "this." + fieldName + " =";

            for (final String code : allMethodCodes) {
                if (code.contains(search)) {
                    nonFinalFields.add(fieldName);

                    break;
                }
            }
        }

        for (final Map.Entry<String, String> entry : fieldNameToType.entrySet()) {
            final String fieldName = entry.getKey();
            final String fieldType = entry.getValue();

            boolean isFinal = !nonFinalFields.contains(fieldName);

            if (isFinal) {
                finalFieldNames.add(fieldName);
                finalFieldTypes.add(fieldType);
            }
        }

        final StringBuilder ctorBuilder = new StringBuilder();

        ctorBuilder.append("    public ").append(className).append("(");

        for (int j = 0; j < finalFieldNames.size(); j++) {
            if (j > 0) {
                ctorBuilder.append(", ");
            }

            ctorBuilder.append(finalFieldTypes.get(j)).append(" ").append(finalFieldNames.get(j));
        }

        ctorBuilder.append(") {\n");

        for (String fieldName : finalFieldNames) {
            ctorBuilder.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
        }

        ctorBuilder.append("    }\n\n");

        return ctorBuilder.toString();
    }
}

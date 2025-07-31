package com.interguess.autoimpl.annotationprocessor.collectors;

import com.interguess.autoimpl.api.field.GeneratedField;
import com.interguess.autoimpl.api.method.MethodType;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.ExecutableElement;
import java.util.*;

public final class FieldCollector {

    @NotNull
    private final Map<String, String> fieldNameToType = new LinkedHashMap<>();

    @NotNull
    private final List<String> allMethodCodes = new ArrayList<>();

    public void collect(final @NotNull ExecutableElement method, final @NotNull MethodType methodType) {
        final String code = methodType.generateCode(method);

        this.allMethodCodes.add(code);

        for (final GeneratedField field : methodType.generateFields(method)) {
            this.fieldNameToType.put(field.getName(), field.getType().toString());
        }
    }

    public @NotNull String generateFieldsCode() {
        final Set<String> nonFinalFields = new HashSet<>();

        for (final String fieldName : this.fieldNameToType.keySet()) {
            final String search = "this." + fieldName + " =";

            for (final String code : this.allMethodCodes) {
                if (code.contains(search)) {
                    nonFinalFields.add(fieldName);

                    break;
                }
            }
        }

        final Map<String, List<String>> fieldsByType = new LinkedHashMap<>();

        for (final Map.Entry<String, String> entry : this.fieldNameToType.entrySet()) {
            final String fieldName = entry.getKey();
            final String fieldType = entry.getValue();

            fieldsByType.computeIfAbsent(fieldType, k -> new ArrayList<>()).add(fieldName);
        }

        final StringBuilder builder = new StringBuilder();

        int i = 0;

        final int typeCount = fieldsByType.size();

        for (final Map.Entry<String, List<String>> entry : fieldsByType.entrySet()) {
            final String type = entry.getKey();

            final List<String> fieldNames = entry.getValue();

            for (final String fieldName : fieldNames) {
                final boolean isFinal = !nonFinalFields.contains(fieldName);

                builder.append("    private ")
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

    public @NotNull String generateCtorCode(final @NotNull String className) {
        final List<String> finalFieldNames = new ArrayList<>();
        final List<String> finalFieldTypes = new ArrayList<>();
        final List<String> nonFinalFieldNames = new ArrayList<>();
        final List<String> nonFinalFieldTypes = new ArrayList<>();

        for (final String fieldName : this.fieldNameToType.keySet()) {
            final String search = "this." + fieldName + " =";

            for (final String code : this.allMethodCodes) {
                if (code.contains(search)) {
                    nonFinalFieldNames.add(fieldName);
                    nonFinalFieldTypes.add(this.fieldNameToType.get(fieldName));

                    break;
                }
            }
        }

        for (final Map.Entry<String, String> entry : this.fieldNameToType.entrySet()) {
            final String fieldName = entry.getKey();
            final String fieldType = entry.getValue();

            final boolean isFinal = !nonFinalFieldNames.contains(fieldName);

            if (isFinal) {
                finalFieldNames.add(fieldName);
                finalFieldTypes.add(fieldType);
            }
        }

        final StringBuilder ctorBuilder = new StringBuilder();

        ctorBuilder
                .append("\n")
                .append("    public ")
                .append(className)
                .append("(");

        for (int j = 0; j < finalFieldNames.size(); j++) {
            if (j > 0) {
                ctorBuilder.append(", ");
            }

            ctorBuilder.append(finalFieldTypes.get(j)).append(" ").append(finalFieldNames.get(j));
        }
        ctorBuilder.append(") {\n");

        for (final String fieldName : finalFieldNames) {
            ctorBuilder.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
        }

        ctorBuilder.append("    }\n\n");

        if (!nonFinalFieldNames.isEmpty()) {
            ctorBuilder.append("    public ").append(className).append("(");

            for (int i = 0; i < finalFieldNames.size(); i++) {
                if (i > 0) {
                    ctorBuilder.append(", ");
                }

                ctorBuilder.append(finalFieldTypes.get(i)).append(" ").append(finalFieldNames.get(i));
            }
            for (int i = 0; i < nonFinalFieldNames.size(); i++) {
                if (i > 0 || !finalFieldNames.isEmpty()) {
                    ctorBuilder.append(", ");
                }

                ctorBuilder.append(nonFinalFieldTypes.get(i)).append(" ").append(nonFinalFieldNames.get(i));
            }

            ctorBuilder.append(") {\n");

            for (final String fieldName : finalFieldNames) {
                ctorBuilder
                        .append("        this.")
                        .append(fieldName)
                        .append(" = ")
                        .append(fieldName)
                        .append(";\n");
            }

            if (!finalFieldNames.isEmpty()) {
                ctorBuilder.append("\n");
            }

            for (final String fieldName : nonFinalFieldNames) {
                ctorBuilder
                        .append("        this.")
                        .append(fieldName)
                        .append(" = ")
                        .append(fieldName)
                        .append(";\n");
            }

            ctorBuilder.append("    }\n");
        }

        return ctorBuilder.toString();
    }
}

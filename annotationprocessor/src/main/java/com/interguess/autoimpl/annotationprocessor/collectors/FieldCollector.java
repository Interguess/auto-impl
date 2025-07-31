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

        splitFieldsByFinality(finalFieldNames, finalFieldTypes, nonFinalFieldNames, nonFinalFieldTypes);

        final StringBuilder ctorBuilder = new StringBuilder();

        ctorBuilder.append("\n");

        ctorBuilder.append(generateConstructor(className, finalFieldNames, finalFieldTypes, new ArrayList<>(), new ArrayList<>()));

        if (!nonFinalFieldNames.isEmpty()) {
            ctorBuilder.append(generateConstructor(
                    className,
                    finalFieldNames, finalFieldTypes,
                    nonFinalFieldNames, nonFinalFieldTypes
            ));
        }

        return ctorBuilder.toString();
    }

    private void splitFieldsByFinality(
            final @NotNull List<String> finalNames,
            final @NotNull List<String> finalTypes,
            final @NotNull List<String> nonFinalNames,
            final @NotNull List<String> nonFinalTypes
    ) {
        for (final String fieldName : this.fieldNameToType.keySet()) {
            if (isNonFinalField(fieldName)) {
                nonFinalNames.add(fieldName);
                nonFinalTypes.add(this.fieldNameToType.get(fieldName));
            }
        }

        for (final Map.Entry<String, String> entry : this.fieldNameToType.entrySet()) {
            final String fieldName = entry.getKey();
            final String fieldType = entry.getValue();

            if (!nonFinalNames.contains(fieldName)) {
                finalNames.add(fieldName);
                finalTypes.add(fieldType);
            }
        }
    }

    private boolean isNonFinalField(String fieldName) {
        final String search = "this." + fieldName + " =";

        for (final String code : this.allMethodCodes) {
            if (code.contains(search)) {
                return true;
            }
        }

        return false;
    }

    private @NotNull String generateConstructor(
            final @NotNull String className,
            final @NotNull List<String> finalNames,
            final @NotNull List<String> finalTypes,
            final @NotNull List<String> nonFinalNames,
            final @NotNull List<String> nonFinalTypes
    ) {
        final StringBuilder builder = new StringBuilder();

        builder
                .append("    public ")
                .append(className)
                .append("(")
                .append(generateParameterList(finalNames, finalTypes));

        if (!nonFinalNames.isEmpty()) {
            if (!finalNames.isEmpty()) {
                builder.append(", ");
            }

            builder.append(generateParameterList(nonFinalNames, nonFinalTypes));
        }

        builder
                .append(") {\n")
                .append(generateAssignmentLines(finalNames));

        if (!nonFinalNames.isEmpty()) {
            if (!finalNames.isEmpty()) {
                builder.append("\n");
            }

            builder.append(generateAssignmentLines(nonFinalNames));
        }

        builder.append("    }\n");

        return builder.toString();
    }

    private @NotNull String generateParameterList(final @NotNull List<String> names, final @NotNull List<String> types) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < names.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }

            builder
                    .append(types.get(i))
                    .append(" ")
                    .append(names.get(i));
        }

        return builder.toString();
    }

    private @NotNull String generateAssignmentLines(final @NotNull List<String> fieldNames) {
        final StringBuilder builder = new StringBuilder();

        for (final String fieldName : fieldNames) {
            builder
                    .append("        this.")
                    .append(fieldName)
                    .append(" = ")
                    .append(fieldName)
                    .append(";\n");
        }

        return builder.toString();
    }
}

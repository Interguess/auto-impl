package com.interguess.autoimpl.common.methodtypes;

import com.google.common.base.Preconditions;
import com.interguess.autoimpl.api.annotations.AutoField;
import com.interguess.autoimpl.api.field.GeneratedField;
import com.interguess.autoimpl.api.method.MethodType;
import com.interguess.autoimpl.common.field.GeneratedFieldImpl;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

public class GetMethod implements MethodType {

    @Override
    public @NotNull String generateCode(@NotNull ExecutableElement methodElement) {
        final String template = """
                    @java.lang.Override
                    public %s %s(%s) {
                        return this.%s;
                    }
                """;

        Preconditions.checkState(
                methodElement.getReturnType() != null,
                "Get method '%s' must have a return type.",
                methodElement.getSimpleName().toString()
        );

        final String returnType = methodElement.getReturnType().toString();
        final String methodName = methodElement.getSimpleName().toString();
        final String parameters = methodElement.getParameters().stream()
                .map(param -> param.asType().toString() + " " + param.getSimpleName())
                .reduce((p1, p2) -> p1 + ", " + p2)
                .orElse("");

        final String fieldName = methodElement.getAnnotation(AutoField.class).value();

        return template.formatted(returnType, methodName, parameters, fieldName);
    }

    @Override
    public @NotNull List<GeneratedField> generateFields(@NotNull ExecutableElement methodElement) {
        Preconditions.checkState(
                methodElement.getReturnType() != null,
                "Get method '%s' must have a return type.",
                methodElement.getSimpleName().toString()
        );

        Preconditions.checkState(
                methodElement.getAnnotation(AutoField.class) != null,
                "Get method '%s' must have an @AutoField annotation.",
                methodElement.getSimpleName().toString()
        );

        return List.of(
                GeneratedFieldImpl.builder()
                        .name(methodElement.getAnnotation(AutoField.class).value())
                        .type(methodElement.getReturnType())
                        .build()
        );
    }
}

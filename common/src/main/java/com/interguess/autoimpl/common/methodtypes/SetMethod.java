package com.interguess.autoimpl.common.methodtypes;

import com.google.common.base.Preconditions;
import com.interguess.autoimpl.api.annotations.AutoField;
import com.interguess.autoimpl.api.field.GeneratedField;
import com.interguess.autoimpl.api.method.MethodType;
import com.interguess.autoimpl.common.field.GeneratedFieldImpl;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

public class SetMethod implements MethodType {

    @Override
    public @NotNull String generateCode(@NotNull ExecutableElement methodElement) {
        final String template = """
                    @java.lang.Override
                    public void %s(final %s %s) {
                        this.%s = %s;
                    }
                """;

        final String methodName = methodElement.getSimpleName().toString();

        Preconditions.checkState(
                methodElement.getParameters().size() == 1,
                "Set method '%s' must have exactly one parameter.",
                methodName
        );

        Preconditions.checkState(
                methodElement.getReturnType().toString().equals("void"),
                "Set method '%s' must have a return type of void.",
                methodName
        );

        final String parameterType = methodElement.getParameters().getFirst().asType().toString();
        final String parameterName = methodElement.getParameters().getFirst().getSimpleName().toString();

        Preconditions.checkState(
                methodElement.getAnnotation(AutoField.class) != null,
                "Set method '%s' must have an @AutoField annotation.",
                methodName
        );

        final String fieldName = methodElement.getAnnotation(AutoField.class).value();

        return template.formatted(methodName, parameterType, parameterName, fieldName, fieldName);
    }

    @Override
    public @NotNull List<GeneratedField> generateFields(@NotNull ExecutableElement methodElement) {
        Preconditions.checkState(
                methodElement.getParameters().size() == 1,
                "Set method '%s' must have exactly one parameter.",
                methodElement.getSimpleName().toString()
        );

        Preconditions.checkState(
                methodElement.getAnnotation(AutoField.class) != null,
                "Set method '%s' must have an @AutoField annotation.",
                methodElement.getSimpleName().toString()
        );

        return List.of(
                GeneratedFieldImpl.builder()
                        .name(methodElement.getAnnotation(AutoField.class).value())
                        .type(methodElement.getParameters().getFirst().asType())
                        .build()
        );
    }
}

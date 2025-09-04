/*
 * MIT License
 *
 * Copyright (c) 2025 Interguess.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.interguess.autoimpl.common.methodtypes;

import com.google.common.base.Preconditions;
import com.interguess.autoimpl.api.annotations.AutoField;
import com.interguess.autoimpl.api.annotations.RegisterMethod;
import com.interguess.autoimpl.api.field.GeneratedField;
import com.interguess.autoimpl.api.method.MethodType;
import com.interguess.autoimpl.common.field.GeneratedFieldImpl;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

@RegisterMethod("get")
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

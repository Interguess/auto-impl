package com.interguess.autoimpl.annotationprocessor.collectors;

import com.interguess.autoimpl.api.method.MethodType;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.ExecutableElement;
import java.util.ArrayList;
import java.util.List;

public class MethodCollector {

    @NotNull
    private final List<String> allMethodCodes = new ArrayList<>();

    public void collect(final @NotNull ExecutableElement method, final @NotNull MethodType methodType) {
        final String code = methodType.generateCode(method);

        this.allMethodCodes.add(code);
    }

    public @NotNull String generateMethodsCode() {
        final StringBuilder builder = new StringBuilder();

        for (final String code : this.allMethodCodes) {
            builder.append(code);

            if (!code.equals(this.allMethodCodes.getLast())) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }
}

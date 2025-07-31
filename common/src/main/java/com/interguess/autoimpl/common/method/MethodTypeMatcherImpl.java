package com.interguess.autoimpl.common.method;

import com.interguess.autoimpl.api.exception.MethodTypeRegistrationException;
import com.interguess.autoimpl.api.method.MethodType;
import com.interguess.autoimpl.api.method.MethodTypeMatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.ExecutableElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodTypeMatcherImpl extends MethodTypeMatcher {

    @NotNull
    private final Map<String, MethodType> types;

    public MethodTypeMatcherImpl() {
        this.types = new HashMap<>();

        MethodTypeMatcher.setInstance(this);
    }

    @Override
    public @NotNull List<MethodType> getRegisteredTypes() {
        return this.types.values().stream().toList();
    }

    @Override
    public void registerType(@NotNull String methodNameRegex, @NotNull MethodType type) {
        if (this.types.containsKey(methodNameRegex)) {
            throw new MethodTypeRegistrationException("Method type with name regex '" + methodNameRegex + "' is already registered.");
        }

        this.types.put(methodNameRegex, type);
    }

    @Override
    public void unregisterType(@NotNull MethodType type) {
        this.types.values().removeIf(existingType -> existingType.equals(type));
    }

    @Override
    public @Nullable MethodType match(@NotNull ExecutableElement method) {
        for (final Map.Entry<String, MethodType> entry : this.types.entrySet()) {
            final String methodNameRegex = entry.getKey();
            final MethodType methodType = entry.getValue();

            if (method.getSimpleName().toString().matches(methodNameRegex)) {
                return methodType;
            }
        }

        return null;
    }
}

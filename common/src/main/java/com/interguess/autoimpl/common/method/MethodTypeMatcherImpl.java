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

package com.interguess.autoimpl.common.method;

import com.interguess.autoimpl.api.annotations.AutoMethod;
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
    public void registerType(@NotNull String methodTypeId, @NotNull MethodType type) {
        if (this.types.containsKey(methodTypeId)) {
            throw new MethodTypeRegistrationException("Method type with the id '" + methodTypeId + "' is already registered.");
        }

        this.types.put(methodTypeId, type);
    }

    @Override
    public void unregisterType(@NotNull String methodTypeId) {
        this.types.remove(methodTypeId);
    }

    @Override
    public @Nullable MethodType match(@NotNull ExecutableElement method) {
        final AutoMethod annotation = method.getAnnotation(AutoMethod.class);

        if (annotation == null) {
            throw new IllegalStateException("Method " + method.getSimpleName() + " is not annotated with @AutoMethod");
        }

        final MethodType type = this.types.get(annotation.value());

        if (type == null) {
            throw new IllegalStateException("No method type registered with the id '" + annotation.value() + "'");
        }

        return type;
    }
}

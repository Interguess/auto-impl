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

package com.interguess.autoimpl.api.method;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

/**
 * Abstract class for matching method types based on method names.
 * This class provides methods to register, unregister, and match method types based on a regex pattern
 * of the method name of an {@link ExecutableElement}.
 */
public abstract class MethodTypeMatcher {

    @Getter
    @Setter
    private static MethodTypeMatcher instance;

    /**
     * Checks if the MethodTypeMatcher is initialized.
     *
     * @return true if the MethodTypeMatcher instance is initialized, false otherwise
     */
    public static boolean isInitialized() {
        return instance != null;
    }

    /**
     * Get all registered method types.
     *
     * @return a list of all registered MethodType instances
     */
    public abstract @NotNull List<MethodType> getRegisteredTypes();

    /**
     * Registers a method type with a specific id.
     *
     * @param methodTypeId the id of the MethodType to register, as specified in the AutoMethod annotation
     * @param type         the MethodType to register
     */
    public abstract void registerType(@NotNull String methodTypeId, @NotNull MethodType type);

    /**
     * Unregisters a method type by its id.
     *
     * @param methodTypeId the id of the MethodType to unregister
     */
    public abstract void unregisterType(@NotNull String methodTypeId);

    /**
     * Matches a method type based on the @AutoMethod annotation of the given ExecutableElement.
     *
     * @param method the ExecutableElement representing the method to match
     * @return the matched MethodType, or null if no match is found
     */
    public abstract @Nullable MethodType match(@NotNull ExecutableElement method);
}

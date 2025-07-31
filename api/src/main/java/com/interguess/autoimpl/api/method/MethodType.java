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

import com.interguess.autoimpl.api.field.GeneratedField;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

/**
 * Interface representing a method type for code generation.
 * This interface defines methods to generate code and fields for a given method element.
 * Examples of method types could include getters, setters, registration methods etc.
 */
public interface MethodType {

    /**
     * Generates code for the given method element.
     *
     * @param methodElement the method element for which to generate code
     * @return the generated code as a String
     */
    @NotNull String generateCode(@NotNull ExecutableElement methodElement);

    /**
     * Generates fields for the given method element.
     *
     * @param methodElement the method element for which to generate fields
     * @return a list of generated fields
     */
    @NotNull List<GeneratedField> generateFields(@NotNull ExecutableElement methodElement);
}
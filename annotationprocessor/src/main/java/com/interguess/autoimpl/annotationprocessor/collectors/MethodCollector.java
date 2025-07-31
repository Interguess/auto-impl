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

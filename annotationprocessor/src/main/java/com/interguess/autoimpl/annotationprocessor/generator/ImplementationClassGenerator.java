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

package com.interguess.autoimpl.annotationprocessor.generator;

import com.interguess.autoimpl.annotationprocessor.collectors.FieldCollector;
import com.interguess.autoimpl.annotationprocessor.collectors.MethodCollector;
import com.interguess.autoimpl.annotationprocessor.utils.ResourceFileLoaderUtil;
import com.interguess.autoimpl.api.method.MethodType;
import com.interguess.autoimpl.api.method.MethodTypeMatcher;
import com.interguess.autoimpl.common.method.MethodTypeMatcherImpl;
import com.interguess.autoimpl.common.methodtypes.GetMethod;
import com.interguess.autoimpl.common.methodtypes.SetMethod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.Writer;

public class ImplementationClassGenerator {

    @NotNull
    private final ProcessingEnvironment processingEnv;

    @NotNull
    private final MethodTypeMatcher methodTypeMatcher;

    public ImplementationClassGenerator(final @NotNull ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;

        this.methodTypeMatcher = new MethodTypeMatcherImpl();

        this.methodTypeMatcher.registerType("get[a-zA-Z0-9_]+", new GetMethod());
        this.methodTypeMatcher.registerType("set[a-zA-Z0-9_]+", new SetMethod());
    }

    public void generateForInterface(final @NotNull TypeElement interfaceElement) {
        final String interfaceName = interfaceElement.getSimpleName().toString();
        final String packageName = this.processingEnv.getElementUtils().getPackageOf(interfaceElement).getQualifiedName().toString();
        final String className = interfaceName + "Impl";
        final String qualifiedClassName = packageName + "." + className;

        final FieldCollector fieldCollector = new FieldCollector();
        final MethodCollector methodCollector = new MethodCollector();

        for (final Element enclosed : interfaceElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.METHOD) {
                final ExecutableElement method = (ExecutableElement) enclosed;

                final MethodType methodType = this.methodTypeMatcher.match(method);

                if (methodType != null) {
                    methodCollector.collect(method, methodType);
                    fieldCollector.collect(method, methodType);
                }
            }
        }

        final String fieldsCode = fieldCollector.generateFieldsCode();
        final String ctorCode = fieldCollector.generateCtorCode(className);
        final String methodsCode = methodCollector.generateMethodsCode();

        final String classHeader = ResourceFileLoaderUtil.load("/class_header.txt");
        final String classStructure = ResourceFileLoaderUtil.load("/class_structure.txt");

        final String classSource = classStructure.formatted(
                packageName,
                classHeader,
                className,
                interfaceName,
                fieldsCode.stripTrailing(),
                ctorCode,
                methodsCode
        );

        try {
            final JavaFileObject fileObject = this.processingEnv.getFiler().createSourceFile(qualifiedClassName, interfaceElement);

            try (final Writer writer = fileObject.openWriter()) {
                writer.write(classSource);
            }
        } catch (Exception e) {
            this.processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "Failed to generate implementation class: " + e.getMessage(), interfaceElement
            );

            throw new RuntimeException("Failed to generate implementation class for " + interfaceName, e);
        }
    }
}

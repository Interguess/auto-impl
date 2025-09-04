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

package com.interguess.autoimpl.annotationprocessor.processors;

import com.google.auto.service.AutoService;
import com.interguess.autoimpl.annotationprocessor.generator.ImplementationClassGenerator;
import com.interguess.autoimpl.api.annotations.AutoImpl;
import com.interguess.autoimpl.api.annotations.RegisterMethod;
import com.interguess.autoimpl.api.method.MethodType;
import com.interguess.autoimpl.api.method.MethodTypeMatcher;
import com.interguess.autoimpl.common.method.MethodTypeMatcherImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.ServiceLoader;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.interguess.autoimpl.api.annotations.AutoImpl")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class AutoImplProcessor extends AbstractProcessor {

    @Nullable
    private ImplementationClassGenerator classGenerator;

    @Override
    public synchronized void init(final @NotNull ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        MethodTypeMatcher.setInstance(new MethodTypeMatcherImpl());

        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Loading MethodType implementations...");

        ServiceLoader.load(MethodType.class, MethodType.class.getClassLoader()).forEach(methodType -> {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE,
                    "Registering MethodType: " + methodType.getClass().getName()
            );

            final RegisterMethod registerAnnotation = methodType.getClass().getAnnotation(RegisterMethod.class);

            if (registerAnnotation == null) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "MethodType " + methodType.getClass().getName() + " is missing @RegisterMethod annotation."
                );
                return;
            }

            System.out.println(methodType.getClass().getName());

            MethodTypeMatcher.getInstance().registerType(
                    registerAnnotation.value(),
                    methodType
            );
        });

        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.NOTE,
                "MethodType implementations loaded successfully."
        );

        this.classGenerator = new ImplementationClassGenerator(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (this.classGenerator == null) {
            this.processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "AutoImplProcessor is not initialized properly. " +
                            "Please ensure that the processing environment is set up correctly."
            );

            return false;
        }

        for (final Element element : roundEnv.getElementsAnnotatedWith(AutoImpl.class)) {
            if (element.getKind() == ElementKind.INTERFACE) {
                this.classGenerator.generateForInterface((TypeElement) element);
            } else {
                this.processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "@AutoImpl can only be applied to interfaces. " +
                                "Invalid element: " + element.getSimpleName(),
                        element
                );
            }
        }

        return true;
    }
}

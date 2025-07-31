package com.interguess.autoimpl.annotationprocessor.processors;

import com.google.auto.service.AutoService;
import com.interguess.autoimpl.annotationprocessor.generator.ImplementationClassGenerator;
import com.interguess.autoimpl.api.annotations.AutoImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
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

        this.classGenerator = new ImplementationClassGenerator(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (classGenerator == null) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "AutoImplProcessor is not initialized properly. " +
                            "Please ensure that the processing environment is set up correctly."
            );

            return false;
        }

        for (final Element element : roundEnv.getElementsAnnotatedWith(AutoImpl.class)) {
            if (element.getKind() == ElementKind.INTERFACE) {
                classGenerator.generateForInterface((TypeElement) element);
            }
        }

        return true;
    }
}

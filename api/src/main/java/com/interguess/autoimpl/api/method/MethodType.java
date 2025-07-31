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
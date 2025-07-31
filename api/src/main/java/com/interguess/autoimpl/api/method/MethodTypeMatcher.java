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
     * Registers a method type with a regex pattern for matching method names.
     *
     * @param methodNameRegex the regex pattern to match method names
     * @param type            the MethodType to register
     */
    public abstract void registerType(@NotNull String methodNameRegex, @NotNull MethodType type);

    /**
     * Unregisters a method type.
     *
     * @param type the MethodType to unregister
     */
    public abstract void unregisterType(@NotNull MethodType type);

    /**
     * Matches a method type based on the method name of the provided ExecutableElement.
     *
     * @param method the ExecutableElement representing the method to match
     * @return the matched MethodType, or null if no match is found
     */
    public abstract @Nullable MethodType match(@NotNull ExecutableElement method);
}

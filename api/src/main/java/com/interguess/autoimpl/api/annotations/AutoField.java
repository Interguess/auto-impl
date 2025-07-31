package com.interguess.autoimpl.api.annotations;

import org.jetbrains.annotations.NotNull;

/**
 * Annotation to mark a method that's implementation should reference to a specific field.
 * E.g. if you have a getter and a setter you set their field name with this annotation to the same value,
 * auto-impl will generate getter and setter implementations for their common field.
 */
public @interface AutoField {

    /**
     * The name of the field this method should be applied to.
     *
     * @return the name of the field
     */
    @NotNull String value();
}

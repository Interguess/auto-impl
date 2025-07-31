package com.interguess.autoimpl.api.annotations;

import java.lang.annotation.Target;

/**
 * Marker annotation for classes that should be automatically implemented.
 */
@Target({java.lang.annotation.ElementType.TYPE})
public @interface AutoImpl {
}

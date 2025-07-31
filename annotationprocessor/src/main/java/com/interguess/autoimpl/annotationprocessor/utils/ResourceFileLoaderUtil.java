package com.interguess.autoimpl.annotationprocessor.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

@UtilityClass
public class ResourceFileLoaderUtil {

    public static @NotNull String load(final @NotNull String resourceFileName) {
        try (final InputStream inputStream = ResourceFileLoaderUtil.class.getResourceAsStream(resourceFileName)) {
            if (inputStream == null) {
                throw new RuntimeException("Failed to load resource file: " + resourceFileName);
            }

            return new String(inputStream.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("Error reading resource file: " + resourceFileName, e);
        }
    }
}

package com.example.webshop.utility;

import java.lang.reflect.Field;

public class ObjectUpdater {
    public static <T> void updateNonNullFields(T source, T target) {
        if (source == null || target == null) {
            return;
        }
        try {
            // Get all declared fields from the source class
            Field[] fields = source.getClass().getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true); // Make private fields accessible

                Object value = field.get(source); // Get the value from the source object

                if (value != null) { // Check if the value is non-null
                    field.set(target, value); // Set the value in the target object
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to update fields", e);
        }
    }
}

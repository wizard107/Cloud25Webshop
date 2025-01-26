package com.example.webshop.utility;

import java.lang.reflect.Field;

public class ObjectDTOMapper {

    public static <T> void updateNonNullFields(T source, T target) {
        if (source == null || target == null) {
            return;
        }
        try {
            // Get all declared fields from the source class
            Field[] sourceFields = source.getClass().getDeclaredFields();
            // Get all declared fields from the target class
            Field[] targetFields = target.getClass().getDeclaredFields();

            // Iterate over source fields and match with target fields
            for (Field sourceField : sourceFields) {
                sourceField.setAccessible(true); // Make private fields accessible
                Object value = sourceField.get(source); // Get value from source object

                if (value != null) { // Check if value is non-null
                    // Find the corresponding field in the target class
                    for (Field targetField : targetFields) {
                        if (sourceField.getName().equals(targetField.getName())) {
                            targetField.setAccessible(true);
                            targetField.set(target, value); // Set the value in target object
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to update fields", e);
        }
    }
}

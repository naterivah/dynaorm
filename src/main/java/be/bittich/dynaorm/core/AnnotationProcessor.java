/*
 * Copyright 2014 Nordine.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.bittich.dynaorm.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nordine
 */
public class AnnotationProcessor {

    public static <T> String getFieldValue(String fieldName, T obj) {
        String fieldValue = "";
        try {
            Class clazz = obj.getClass();
            Field privateStringField = clazz.getDeclaredField(fieldName);

            privateStringField.setAccessible(true);
            fieldValue = privateStringField.get(obj) == null ? null : privateStringField.get(obj).toString();;

        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException | NullPointerException ex) {
            Logger.getLogger(DynaUtils.class.getName()).log(Level.SEVERE, "Empty value, did you forget to set the value on your object?", ex);
        }
        return fieldValue;
    }

    @SuppressWarnings("empty-statement")
    public static <T, E extends Annotation> Map<Field, E> getAnnotedFields(T annotedObj, Class<E> annotation) {

        Field[] fields = annotedObj.getClass().getDeclaredFields();
        Map<Field, E> filteredFields = new HashMap();
        for (Field field : fields) {
            E annotationX = field.getAnnotation(annotation);
            if (annotationX != null) {
                filteredFields.put(field, annotationX);
            }
        }
        return filteredFields;
    }

    public static <T, E extends Annotation> E getAnnotationType(Class<T> clazz, Class<E> annotation) {
        return clazz.getAnnotation(annotation);
    }

}

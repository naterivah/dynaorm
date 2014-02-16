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
package be.bittich.dynaorm.entity;

import be.bittich.dynaorm.annotation.PrimaryKey;
import static be.bittich.dynaorm.core.AnnotationProcessor.getAnnotedFields;
import static be.bittich.dynaorm.core.AnnotationProcessor.getFieldValue;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author Nordine
 */
public class TestCity {

    @Test
    public void testAnnotation() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

        City city = new City(12, "lol", "mdr");
        Map<Field,PrimaryKey> fields = getAnnotedFields(city, PrimaryKey.class);
        for (Field f : fields.keySet()) {
            String name = f.getName();
            String fieldValue = getFieldValue(name, city);
            System.out.println(fieldValue);
            String label= fields.get(f).label();
            System.out.println(label);
        }

        //assertTrue(City.class.getAnnotations().length!=0);
    }
}

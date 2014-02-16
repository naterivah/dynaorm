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
package be.bittich.dynaorm.dialect;

import be.bittich.dynaorm.annotation.PrimaryKey;
import be.bittich.dynaorm.core.AnnotationProcessor;
import be.bittich.dynaorm.exception.RequestInvalidException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

/**
 *
 * @author Nordine
 */
public class StringQueryBuilder {

    public static <T> KeyValue<String, List<String>> conditionPrimaryKeysBuilder(T t, Map<Field, PrimaryKey> fieldPrimary, Dialect dialect) throws RequestInvalidException {
        boolean firstIteration = true;
        String req = "";
        List<String> parameters = new LinkedList();
        for (Field field : fieldPrimary.keySet()) {
            String label = "".equals(fieldPrimary.get(field).label()) ? field.getName() : fieldPrimary.get(field).label();
            String value = AnnotationProcessor.getFieldValue(field.getName(), t);
            if (firstIteration) {
                req = dialect.where(req);
                firstIteration = false;
            } else {
                req = dialect.andWhere(req);
            }
            req = dialect.equalTo(req, label);
            parameters.add(value);
        }
        return new DefaultKeyValue(req, parameters);
    }
}

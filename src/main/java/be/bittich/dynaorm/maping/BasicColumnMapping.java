/*
 * Copyright 2014 Evoliris.
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
package be.bittich.dynaorm.maping;

import be.bittich.dynaorm.annotation.MetaColumn;
import be.bittich.dynaorm.repository.TableColumn;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 *
 * @author Nordine Bittich
 */
public class BasicColumnMapping implements ColumnMapping {

    @Override
    public <T> Map<String, Field> mapToSQLColumns(T obj, TableColumn tableColumn) {
        //get the fields from the object
        Field[] fields = obj.getClass().getDeclaredFields();
        Set<String> columnsFromDB = tableColumn.getColumns().keySet();
        //map the column name with the field
        Map<String, Field> filteredFields = new HashMap();
        for (Field field : fields) {
            MetaColumn annotationX = field.getAnnotation(MetaColumn.class);
            //by default, columnName=field name
            String columnName = field.getName();
            if (annotationX != null && !isEmpty(annotationX.columnName())) {
                columnName = annotationX.columnName();
            }
            //map only the field present in the table
            if (columnsFromDB.contains(columnName)) {
                filteredFields.put(columnName, field);
            }
        }

        return filteredFields;
    }

}

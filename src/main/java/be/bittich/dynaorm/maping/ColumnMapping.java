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

import be.bittich.dynaorm.repository.TableColumn;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

/**
 *
 * @author Nordine Bittich
 */
public interface ColumnMapping extends Serializable{
    /**
     * take an object and a table column then return a map with the actual columnName 
     * and the field related
     * @param <T>
     * @param obj
     * @param tableColumn
     * @return 
     */
    <T>Map<String,Field> mapToSQLColumns(T obj,TableColumn tableColumn);
}

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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nordine
 */
public class TableColumn implements Serializable {

    private static final long serialVersionUID = 2442984702019905389L;
    private final String tableName;
    //type constant from Types class
    private final Map<String, Integer> columns = new HashMap();

    public String getTableName() {

        return tableName;
    }

    public TableColumn(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, Integer> getColumns() {
        return columns;
    }

    public void addColumn(String columnName, Integer type) {
        columns.put(columnName, type);
    }
    /**
     * shortcut method. return the type of a given column. or null if it doesnt exist
     * @param columnName
     * @return 
     */
    public Integer getTypeOfColumn(String columnName){
        return this.getColumns().get(columnName);
    }
}

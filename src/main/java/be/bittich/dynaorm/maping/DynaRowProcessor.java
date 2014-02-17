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

import be.bittich.dynaorm.ioc.BasicContainer;
import be.bittich.dynaorm.repository.TableColumn;
import be.bittich.dynaorm.sql.SQLTypeMap;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.BasicRowProcessor;

/**
 * Implementation of BasicRowProcessor
 *
 * @author Nordine
 */
public class DynaRowProcessor extends BasicRowProcessor {
    
    private final TableColumn tableColumn;
    
    public DynaRowProcessor(TableColumn tableColumn) {
        this.tableColumn = tableColumn;
    }
    
    @Override
    public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
        T bean = null;
        try {
            bean = type.newInstance();
            ColumnMapping mapping = BasicContainer.getContainer().injectSafely("columnMapping");
            Map<String, Field> mapper = mapping.mapToSQLColumns(bean, tableColumn);
            for (String columnName : mapper.keySet()) {
                Field field = mapper.get(columnName);
                field.setAccessible(true);
                //get java Type
                String javaType = SQLTypeMap.convert(tableColumn.getColumns().get(columnName));
                Class<?> typeForValue = Class.forName(javaType);
                Object value = rs.getObject(columnName, typeForValue);
                field.set(bean, value);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            Logger.getLogger(DynaRowProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
        
    }
    
    @Override
    public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException {
        try {
            List newlist = new LinkedList();
            while (rs.next()) {
                newlist.add(toBean(rs, type));
            }
            return newlist;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}

/*
 * Copyright 2014 Nordine Bittich.
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
package be.bittich.dynaorm.swing;

import be.bittich.dynaorm.swing.BeanTableModel.EditMode;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class BeanTableModel<T> extends AbstractTableModel {

    private static final long serialVersionUID = -2807189182145729669L;

    private final List<T> rows = new ArrayList();
    private final List<BeanColumn> columns = new ArrayList();
    private final Class<T> beanClass;

    public void clearRows() {
        rows.clear();
    }

    public BeanTableModel(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    public void addColumn(String columnGUIName, String beanAttribute,
            EditMode editable) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(beanAttribute,
                    beanClass);
            columns.add(new BeanColumn(columnGUIName, editable, descriptor));
        } catch (IntrospectionException e) {
        }
    }

    public void addColumn(String columnGUIName, String beanAttribute) {
        addColumn(columnGUIName, beanAttribute, EditMode.NON_EDITABLE);
    }

    public void addRow(T row) {
        rows.add(row);
    }

    public void addRows(List<T> rows) {
        for (T row : rows) {
            addRow(row);
        }
        this.fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BeanColumn column = columns.get(columnIndex);
        T row = rows.get(rowIndex);

        Object result = null;
        try {
            result = column.descriptor.getReadMethod().invoke(row);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        }
        return result;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        T row = rows.get(rowIndex);
        BeanColumn column = columns.get(columnIndex);

        try {
            column.descriptor.getWriteMethod().invoke(row, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        BeanColumn column = columns.get(columnIndex);
        Class<?> returnType = column.descriptor.getReadMethod().getReturnType();
        return returnType;
    }

    @Override
    public String getColumnName(int column) {
        return columns.get(column).columnGUIName;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columns.get(columnIndex).editable == EditMode.EDITABLE;
    }

    public List<T> getRows() {
        return rows;
    }

    public enum EditMode {

        NON_EDITABLE,
        EDITABLE;
    }

    /**
     * One column in the table
     */
    private static class BeanColumn {

        private String columnGUIName;
        private EditMode editable;
        private PropertyDescriptor descriptor;

        public BeanColumn(String columnGUIName, EditMode editable,
                PropertyDescriptor descriptor) {
            this.columnGUIName = columnGUIName;
            this.editable = editable;
            this.descriptor = descriptor;
        }
    }
}

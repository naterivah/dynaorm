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
package be.bitich.dynaorm.test.integration;

import be.bitich.dynaorm.test.integration.BeanTableModel.EditMode;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * A table model where each row represents one instance of a Java bean. When the
 * user edits a cell the model is updated.
 *
 * @author Lennart Schedin
 *
 * @param <M> The type of model
 */
@SuppressWarnings("serial")
public class BeanTableModel<M> extends AbstractTableModel {

    private List<M> rows = new ArrayList<M>();
    private List<BeanColumn> columns = new ArrayList<BeanColumn>();
    private Class<?> beanClass;
    public void clearRows(){
        rows.clear();
    }
    public BeanTableModel(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public void addColumn(String columnGUIName, String beanAttribute,
            EditMode editable) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(beanAttribute,
                    beanClass);
            columns.add(new BeanColumn(columnGUIName, editable, descriptor));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addColumn(String columnGUIName, String beanAttribute) {
        addColumn(columnGUIName, beanAttribute, EditMode.NON_EDITABLE);
    }

    public void addRow(M row) {
        rows.add(row);
    }

    public void addRows(List<M> rows) {
        for (M row : rows) {
            addRow(row);
        }
        this.fireTableDataChanged();
    }

    public int getColumnCount() {
        return columns.size();
    }

    public int getRowCount() {
        return rows.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        BeanColumn column = columns.get(columnIndex);
        M row = rows.get(rowIndex);

        Object result = null;
        try {
            result = column.descriptor.getReadMethod().invoke(row);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        M row = rows.get(rowIndex);
        BeanColumn column = columns.get(columnIndex);

        try {
            column.descriptor.getWriteMethod().invoke(row, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Class<?> getColumnClass(int columnIndex) {
        BeanColumn column = columns.get(columnIndex);
        Class<?> returnType = column.descriptor.getReadMethod().getReturnType();
        return returnType;
    }

    public String getColumnName(int column) {
        return columns.get(column).columnGUIName;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columns.get(columnIndex).editable == EditMode.EDITABLE;
    }

    public List<M> getRows() {
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

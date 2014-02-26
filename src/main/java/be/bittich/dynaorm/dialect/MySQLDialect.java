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

import static be.bittich.dynaorm.core.DynaUtils.getDateOrNull;
import be.bittich.dynaorm.exception.RequestInvalidException;
import com.google.common.base.Joiner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 *
 * @author Nordine
 */
public class MySQLDialect implements Dialect {

    public static final String SELECT = "SELECT ";
    public static final String FROM = "FROM ";
    public static final String WHERE = "WHERE ";
    public static final String ALL = "* ";
    public static final String AND = "AND ";
    public static final String LIKE = "LIKE ";
    public static final String STARTLIKE = "LIKE % ";
    public static final String ENDLIKE = "LIKE %{} ";
    public static final String EXACTLYLIKE = "LIKE % % ";
    public static final String EQUALITY = " = ";
    public static final String DELETE = "DELETE ";
    public static final String REQ_FOR_TABLE_COLUMNS = "SELECT * FROM %s WHERE 1=0";
    public static final String ORDER_BY = "ORDER BY ";
    public static final String INSERT = "INSERT INTO %s (%s) VALUES (%s)";
    public static final String UPDATE = "UPDATE %s SET %s %s";
    public static final String QUOTES = "`%s`";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public String selectAll(String tableName) {
        return SELECT.concat(ALL).concat(this.from(tableName));
    }

    @Override
    public String where(String request) {
        return request.concat(WHERE);
    }

    @Override
    public String andWhere(String request) {
        return request.concat(AND);
    }

    @Override
    public String equalTo(String request, String label) throws RequestInvalidException {
        if (label == null || isEmpty(label)) {
            throw new RequestInvalidException("Label or Value for the request is empty or null");
        }
        return request.concat(label).concat(" ").concat(EQUALITY).concat(REPLACEMENT_VALUE);
    }

    @Override
    public String insert(String tableName, List<String> columns, List<String> values) {
        List<String> replacementValues = new LinkedList();
        List<String> replacementColumns = new LinkedList();
        for (String value : values) {
            replacementValues.add(REPLACEMENT_VALUE);
        }
        for (String column : columns) {
            replacementColumns.add(String.format(QUOTES, column));
        }
        String cols = Joiner.on(',').join(replacementColumns);
        String vals = Joiner.on(",").join(replacementValues);
        return String.format(INSERT, tableName, cols, vals);
    }

    @Override
    public String update(String tableName, List<String> columns, List<String> values, String condition) {
        List<String> replacementValues = new LinkedList();
        for (String column : columns) {
            column = String.format(QUOTES, column);
            replacementValues.add(column.concat(EQUALITY).concat(REPLACEMENT_VALUE));
        }
        String req = Joiner.on(",").join(replacementValues);
        return String.format(UPDATE, tableName, req, condition);
    }

    @Override
    public String type() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String from(String tableName) {
        return FROM.concat(tableName).concat(" ");
    }

    @Override
    public String delete(String tableName) {
        return DELETE.concat(this.from(tableName));

    }

    @Override
    public String requestForTableColumns(String tableName) {
        return String.format(REQ_FOR_TABLE_COLUMNS, tableName);
    }

    @Override
    /**
     * chain ?
     */
    public String doFilterValue(String fieldVal) {
        //Boolean filter
        if (fieldVal.equalsIgnoreCase("true") || fieldVal.equalsIgnoreCase("false")) {
            fieldVal = Boolean.parseBoolean(fieldVal) ? "1" : "0";
            return fieldVal;
        }

        Date date;
        //Date filter
        if ((date = getDateOrNull(fieldVal)) != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            return sdf.format(date);
        }
        return fieldVal;
    }
}

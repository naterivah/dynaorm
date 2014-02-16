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

import be.bittich.dynaorm.exception.RequestInvalidException;
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
    public static final String STARTLIKE = "LIKE %{} ";
    public static final String ENDLIKE = "LIKE %{} ";
    public static final String EXACTLYLIKE = "LIKE %{}% ";
    public static final String EQUALITY = "= ";
    public static final String DELETE = "DELETE ";

    public final String orderBy = "ORDER BY ";

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
    public String insert() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

}

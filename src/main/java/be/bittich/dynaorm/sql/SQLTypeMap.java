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
package be.bittich.dynaorm.sql;

/**
 *
 * @author Nordine
 */
import java.sql.Types;

public class SQLTypeMap {

    public static String convert(int type) {
        String result = "java.lang.Object";

        switch (type) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                result = "java.lang.String";
                break;

            case Types.NUMERIC:
            case Types.DECIMAL:
                result = "java.math.BigDecimal";
                break;

            case Types.BIT:
                result = "java.lang.Boolean";
                break;

            case Types.TINYINT:
                result = "java.lang.Byte";
                break;

            case Types.SMALLINT:
                result = "java.lang.Short";
                break;

            case Types.INTEGER:
                result = "java.lang.Integer";
                break;

            case Types.BIGINT:
                result = "java.lang.Long";
                break;

            case Types.REAL:
                result = "java.lang.Real";
                break;

            case Types.FLOAT:
            case Types.DOUBLE:
                result = "java.lang.Double";
                break;

            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                result = "java.lang.Byte[]";
                break;

            case Types.DATE:
                result = "java.sql.Date";
                break;

            case Types.TIME:
                result = "java.sql.Time";
                break;

            case Types.TIMESTAMP:
                result = "java.sql.Timestamp";
                break;
        }

        return result;
    }
}

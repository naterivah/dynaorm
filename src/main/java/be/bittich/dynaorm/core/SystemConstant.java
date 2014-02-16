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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nordine
 */
public interface SystemConstant {

    /**
     * *
     * Drivers
     */
    public static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
    public static final String DRIVER_HSQL = "org.hsqldb.jdbcDriver";

    /**
     * Supported Drivers
     */
    public static final String SUPPORTED_MYSQL = "mysql";
    public static final String SUPPORTED_HSQL = "hsql";

    public static final Map<String, String> DRIVER_NAME = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put(SUPPORTED_MYSQL, DRIVER_MYSQL);
            put(SUPPORTED_HSQL, DRIVER_HSQL);

        }

    };
    public static final Map<String, String> DIALECT = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put(SUPPORTED_MYSQL, "be.bittich.dynaorm.dialect.MySQLDialect");
            put(SUPPORTED_HSQL, "be.bittich.dynaorm.dialect.HSQLDialect");

        }
    };

}

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
package be.bittich.dynaorm.facad;

import be.bittich.dyanorm.connection.ConnectionDB;
import be.bittich.dynaorm.dialect.Dialect;
import static be.bittich.dynaorm.facad.DynaContainerFacad.getContainer;
import be.bittich.dynaorm.ioc.Container;
import be.bittich.dynaorm.maping.ColumnMapping;
import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author Nordine
 */
public class IOCFacadGet {

    public static QueryRunner getQueryRunner() {
        return getContainer().injectSafely("queryRunner");
    }

    public static ColumnMapping getColumnMapping() {
        return getContainer().injectSafely("columnMapping");
    }

    public static ConnectionDB getConnectionDB() {
        return getContainer().injectSafely("connectionDB");
    }

    public static Dialect getDialect() {
        return getContainer().injectSafely("dialect");
    }

    public static Container getContainer() {
        return DynaContainerFacad.getContainer();
    }
}

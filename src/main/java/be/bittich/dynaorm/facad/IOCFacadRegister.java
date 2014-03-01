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
import static be.bittich.dynaorm.facad.DynaContainerFacad.setContainer;
import be.bittich.dynaorm.ioc.Container;
import be.bittich.dynaorm.maping.ColumnMapping;
import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author Nordine
 */
public final class IOCFacadRegister {

    public static void registerQueryRunner(QueryRunner runner) {
        getContainer().registerBean("queryRunner", runner);
    }

    public static void registerConnectionDB(ConnectionDB conn) {
        getContainer().registerBean("connectionDB", conn);
    }

    public static void registerDialect(Dialect dialect) {
        getContainer().registerBean("dialect", dialect);
    }

    public static void registerColumnMapping(ColumnMapping columnMapping) {
        getContainer().registerBean("columnMapping", columnMapping);
    }

    public static void registerContainer(Container container) {
        setContainer(container);
    }

}

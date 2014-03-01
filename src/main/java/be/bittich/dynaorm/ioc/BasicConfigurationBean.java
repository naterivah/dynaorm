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
package be.bittich.dynaorm.ioc;

import be.bittich.dynaorm.facad.IOCFacadGet;
import be.bittich.dyanorm.connection.ConnectionDB;
import static be.bittich.dynaorm.facad.IOCFacadRegister.registerColumnMapping;
import static be.bittich.dynaorm.facad.IOCFacadRegister.registerQueryRunner;
import be.bittich.dynaorm.maping.BasicColumnMapping;
import java.io.Serializable;
import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author Nordine
 */
public class BasicConfigurationBean implements Serializable {

    private static final long serialVersionUID = -4279720585191618511L;

    /**
     * Build the container with the mininmum required configuration bean. if you
     * want to add new Bean to the container, utilize the registerBean method.
     *
     */
    public static void buildContainer() {

        configureQueryRunner();

        registerColumnMapping(new BasicColumnMapping());
    }

    /**
     * configure a QueryRunner bean
     */
    private static void configureQueryRunner() {

        ConnectionDB bean = IOCFacadGet.getConnectionDB();
        QueryRunner run = new QueryRunner(bean.getDataSource());
        registerQueryRunner(run);

    }

}

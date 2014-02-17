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
package be.bittich.dynaorm.repository;

import static be.bittich.dynaorm.core.DynaUtils.loadProperties;
import be.bittich.dynaorm.dialect.MySQLDialect;
import be.bittich.dynaorm.entity.City;
import be.bittich.dynaorm.ioc.BasicConfigurationBean;
import static be.bittich.dynaorm.ioc.BasicContainer.getContainer;
import be.bittich.dynaorm.maping.ColumnMapping;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Evoliris
 */
public class testMapping {

    @Before
    public void setup() {
        //get an inputStream
        InputStream input = getClass().getClassLoader().getResourceAsStream("dbconfig.properties");

        //load the properties
        Properties dbProperties = loadProperties(input);

        //initialize the container
        BasicConfigurationBean.buildContainer(dbProperties, new MySQLDialect());
        //register the dao's to the basic container
        BasicConfigurationBean.registerBean("daoCity", new DAOCity());

    }

    @Test
    public void testColumnMapping() {
        ColumnMapping columnMapping = getContainer().injectSafely("columnMapping");
        DynaRepository dao = getContainer().injectSafely("daoCity");
        Map<String, Field> map = columnMapping.mapToSQLColumns(new City(), dao.getTableColumn());
        for (String s : map.keySet()) {
            System.out.println(s);
        }
    }
}

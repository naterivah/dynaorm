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
package be.bittich.dynaorm.repository;

import static be.bittich.dynaorm.core.DynaUtils.loadProperties;
import be.bittich.dynaorm.entity.City;
import be.bittich.dynaorm.exception.BeanNotFoundException;
import be.bittich.dynaorm.exception.EntityDoesNotExistException;
import be.bittich.dynaorm.ioc.BasicConfigurationBean;
import static be.bittich.dynaorm.ioc.BasicContainer.getContainer;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Nordine
 */
public class TestDao {

    @Before
    public void setup() {
        InputStream input = getClass().getClassLoader().getResourceAsStream("dbconfig.properties");
        Properties dbProperties = loadProperties(input);
        BasicConfigurationBean.builder(dbProperties);
        BasicConfigurationBean.registerBean("daoCity", new DAOCity());
    }

    @Test
    @Ignore
    public void testDao() throws BeanNotFoundException {
        DynaRepository<City> repository = getContainer().inject("daoCity");
        List<City> list = repository.findAll();
        assertNotNull(list);
        City city2 = new City();
        city2.setId(4);
        city2.setZip("1090");
        for (City c : list) {
            System.out.println(c.getId() + ":" + c.getZip());
        }
        city2 = repository.findById(city2);
        System.out.println(city2.getId() + ":" + city2.getZip());

    }

    @Test
    @Ignore
    public void testDelete() throws BeanNotFoundException, EntityDoesNotExistException {
        DynaRepository<City> repository = getContainer().inject("daoCity");
        City city2 = new City();
        city2.setId(3);
        boolean result=repository.delete(city2);
        assertTrue(result);
        List<City> list = repository.findAll();
        for (City c : list) {
            System.out.println(c.getId() + ":" + c.getZip());
        }
    }
}

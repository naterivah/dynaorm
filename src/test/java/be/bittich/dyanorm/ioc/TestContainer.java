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
package be.bittich.dyanorm.ioc;

import be.bittich.dyanorm.connection.ConnectionDB;
import static be.bittich.dynaorm.core.DynaUtils.loadProperties;
import be.bittich.dynaorm.dialect.Dialect;
import be.bittich.dynaorm.dialect.MySQLDialect;
import be.bittich.dynaorm.exception.BeanNotFoundException;
import be.bittich.dynaorm.ioc.BasicConfigurationBean;
import be.bittich.dynaorm.ioc.BasicContainer;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Nordine
 */
public class TestContainer {

    @Before
    public void setup() {
        InputStream input = getClass().getClassLoader().getResourceAsStream("dbconfig.properties");
        Properties dbProperties = loadProperties(input);
        BasicConfigurationBean.buildContainer(dbProperties);
    }

    @Test
    public void testConfigurationBean() throws SQLException {

        try {
            ConnectionDB o = BasicContainer.getContainer().inject("connectionDB");
            Connection conn = o.getConnection();
            String req = "SELECT * FROM country_t";
            PreparedStatement stmt = conn.prepareStatement(req);
            ResultSet rs = stmt.executeQuery();
            assertNotNull(rs);
            while (rs.next()) {
                System.out.println(rs.getInt(1) + ":" + rs.getString(2));
            }
            Dialect dialect = BasicContainer.getContainer().inject("dialect");
            assertNotNull(dialect);
            assertTrue(dialect instanceof MySQLDialect);
        } catch (BeanNotFoundException ex) {
            Logger.getLogger(TestContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

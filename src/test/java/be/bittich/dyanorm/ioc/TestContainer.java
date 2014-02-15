/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        BasicConfigurationBean.builder(dbProperties);
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

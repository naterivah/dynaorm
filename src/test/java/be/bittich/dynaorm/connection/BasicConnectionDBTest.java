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
package be.bittich.dynaorm.connection;
import be.bittich.dyanorm.connection.BasicConnectionDB;
import be.bittich.dynaorm.connection.impl.BasicConnectionDBImpl;
import be.bittich.dynaorm.core.SystemConstant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static junit.framework.TestCase.assertNotNull;
import org.junit.Test;

/**
 *
 * @author Nordine
 */
public class BasicConnectionDBTest {

    @Test
    public void getInstance() throws SQLException {
        BasicConnectionDB conn = BasicConnectionDBImpl.getInstance();
        conn.setDriver(SystemConstant.DRIVER_HSQL)
                .setLogin("sa")
                .setPassword("")
                .setUrl("jdbc:hsqldb:mem:test")
                .setInitialSize(5);
        Connection connection = conn.getConnection();
        assertNotNull(connection);

        String req = "CREATE TABLE TEST (\n"
                + "         id INT NOT NULL IDENTITY,\n"
                + "         data VARCHAR(100)\n"
                + "       );";
        PreparedStatement stmt = connection.prepareStatement(req);
        stmt.executeUpdate();
        req = "INSERT INTO TEST(data) VALUES('kikoo')";
        stmt = connection.prepareStatement(req);
        stmt.executeUpdate();
                req = "INSERT INTO TEST(data) VALUES('lolmdr')";
        stmt = connection.prepareStatement(req);
        stmt.executeUpdate();
        req = "SELECT * FROM TEST";
        stmt = connection.prepareStatement(req);
        ResultSet rs = stmt.executeQuery();
        assertNotNull(rs);
        while (rs.next()) {
            System.out.println(rs.getInt(1) + ":" + rs.getString(2));
        }

    }
}

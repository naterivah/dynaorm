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

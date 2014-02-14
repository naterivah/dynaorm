

package be.bittich.dynaorm.connection;

import be.bittich.dyanorm.connection.BasicConnectionDB;
import be.bittich.dynaorm.connection.impl.BasicConnectionDBImpl;
import be.bittich.dynaorm.core.SystemConstant;
import java.sql.SQLException;
import javax.sql.DataSource;
import static junit.framework.TestCase.assertNotNull;
import org.junit.Test;


/**
 *
 * @author Nordine
 */
public class BasicConnectionDBTest {
    @Test
    public void getInstance() throws SQLException{
        BasicConnectionDB conn= BasicConnectionDBImpl.getInstance();
        DataSource ds=conn.setDriver(SystemConstant.DRIVER_MYSQL)
                .setLogin("root")
                .setPassword("")
                .setUrl("jdbc:mysql://localhost/kikoo")
                .setInitialSize(5)
                .getDataSource();
        assertNotNull(ds);
    }
}

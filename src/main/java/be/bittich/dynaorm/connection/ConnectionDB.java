package be.bittich.dynaorm.connection;

import java.io.Serializable;
import javax.sql.DataSource;

/**
 *
 * @author Nordine Bittich
 */
public interface ConnectionDB extends Serializable {

    DataSource getDataSource();

    String getDriver();

    String getUrl();

    String getPassword();

    String getLogin();
    
    Integer getInitialSize();

    ConnectionDB setDriver(String driver);
    
    ConnectionDB setInitialSize(Integer initialSize);

    ConnectionDB setUrl(String url);

    ConnectionDB setPassword(String password);

    ConnectionDB setLogin(String login);
    
}

package be.bittich.dynaorm.connection;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author Nordine
 */
public final class BasicConnectionDB implements ConnectionDB {

    private static BasicConnectionDB connectionDB;
    private static final long serialVersionUID = -7339131302708696365L;
    private String login;
    private String password;
    private String url;
    private String driver;
    private Integer initialSize;
    private BasicDataSource ds;

    private BasicConnectionDB() {
    }

    @Override
    public DataSource getDataSource() {
        if (ds == null) {
            ds = new BasicDataSource();
            ds.setDriverClassName(driver);
            ds.setUrl(url);
            ds.setUsername(login);
            ds.setPassword(password);
            ds.setInitialSize(initialSize);
            ds.setTestOnBorrow(false);
            ds.setTestWhileIdle(true);
        }
        return ds;
    }

    @Override
    public String getDriver() {
        return this.driver;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getLogin() {
        return this.login;
    }

    @Override
    public ConnectionDB setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    @Override
    public ConnectionDB setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public ConnectionDB setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public ConnectionDB setLogin(String login) {
        this.login = login;
        return this;
    }

    public static final ConnectionDB getInstance() {
        if (connectionDB == null) {
            connectionDB = new BasicConnectionDB();
        }
        return connectionDB;
    }

    @Override
    public Integer getInitialSize() {
        return initialSize;
    }

    @Override
    public ConnectionDB setInitialSize(Integer initialSize) {
        this.initialSize=initialSize;
        return this;
    }

}

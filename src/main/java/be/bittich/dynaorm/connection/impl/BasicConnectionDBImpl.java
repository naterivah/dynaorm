package be.bittich.dynaorm.connection.impl;

import be.bittich.dyanorm.connection.BasicConnectionDB;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author Nordine
 */
public final class BasicConnectionDBImpl implements BasicConnectionDB {

    private static BasicConnectionDBImpl connectionDB;
    private static final long serialVersionUID = -7339131302708696365L;
    private String login;
    private String password;
    private String url;
    private String driver;
    private Integer initialSize;
    private BasicDataSource ds;

    private BasicConnectionDBImpl() {
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
    public BasicConnectionDB setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    @Override
    public BasicConnectionDB setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public BasicConnectionDB setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public BasicConnectionDB setLogin(String login) {
        this.login = login;
        return this;
    }

    public static final BasicConnectionDB getInstance() {
        if (connectionDB == null) {
            connectionDB = new BasicConnectionDBImpl();
        }
        return connectionDB;
    }

    @Override
    public Integer getInitialSize() {
        return initialSize;
    }

    @Override
    public BasicConnectionDB setInitialSize(Integer initialSize) {
        this.initialSize=initialSize;
        return this;
    }

}

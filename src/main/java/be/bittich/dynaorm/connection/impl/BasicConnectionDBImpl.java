package be.bittich.dynaorm.connection.impl;

import be.bittich.dyanorm.connection.BasicConnectionDB;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author Nordine
 *
 */
public final class BasicConnectionDBImpl implements BasicConnectionDB {

    private static final long serialVersionUID = -7339131302708696365L;
    private static final Logger LOG = Logger.getLogger(BasicConnectionDBImpl.class.getName());
    private static BasicConnectionDBImpl connectionDB;
    private String login;
    private String password;
    private String url;
    private String driver;
    private Integer initialSize;
    private BasicDataSource ds;
    private boolean autoCommit;

    private BasicConnectionDBImpl() {
        this.autoCommit = true;
        this.initialSize = 5;
    }

    private void buildDataSource() {
        ds = new BasicDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(login);
        ds.setPassword(password);
        ds.setInitialSize(initialSize);
        ds.setTestOnBorrow(false);
        ds.setTestWhileIdle(true);
        ds.setDefaultAutoCommit(autoCommit);
    }

    @Override
    public DataSource getDataSource() {
        if (ds == null) {
            buildDataSource();
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
        this.initialSize = initialSize;
        return this;
    }

    @Override
    public Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Attempt to get a connection from the datasource failed. Error : \n" + ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public BasicConnectionDB setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
        return this;
    }

    @Override
    public Boolean isAutoCommit() {
        return autoCommit;
    }

    @Override
    public void close() {
        try {
            this.ds.close();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Cannot close the connections, Error: \n" + ex.getMessage(), ex);
        }

    }

}

package be.bittich.dynaorm.connection.impl;

import be.bittich.dyanorm.connection.BasicConnectionDB;
import com.jolbox.bonecp.BoneCPDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Nordine
 *
 */
public final class BasicConnectionDBImpl implements BasicConnectionDB {

    private static final long serialVersionUID = -7339131302708696365L;
    private static final Logger LOG = Logger.getLogger(BasicConnectionDBImpl.class.getName());
    private static BasicConnectionDBImpl connectionDB;

    private final BoneCPDataSource ds;

    private BasicConnectionDBImpl() {
        ds = new BoneCPDataSource();
    }

    @Override
    public DataSource getDataSource() {

        return ds;
    }

    @Override
    public String getDriver() {
        return this.ds.getDriverClass();
    }

    @Override
    public String getUrl() {
        return this.ds.getJdbcUrl();
    }

    @Override
    public String getPassword() {
        return this.ds.getPassword();
    }

    @Override
    public String getLogin() {
        return this.ds.getUsername();
    }

    @Override
    public BasicConnectionDB setDriver(String driver) {
        this.ds.setDriverClass(driver);
        return this;
    }

    @Override
    public BasicConnectionDB setUrl(String url) {
        this.ds.setJdbcUrl(url);
        return this;
    }

    @Override
    public BasicConnectionDB setPassword(String password) {
        this.ds.setPassword(password);
        return this;
    }

    @Override
    public BasicConnectionDB setLogin(String login) {
        this.ds.setUsername(login);
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
        return this.ds.getPoolAvailabilityThreshold();
    }

    @Override
    public BasicConnectionDB setInitialSize(Integer initialSize) {
        this.ds.setPoolAvailabilityThreshold(initialSize);
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
    public void close() {
        this.ds.close();

    }

}
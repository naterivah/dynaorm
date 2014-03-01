/*
 * Copyright 2014 user.
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
package be.bittich.dynaorm.setup;

import be.bittich.dyanorm.connection.ConnectionDB;
import be.bittich.dynaorm.connection.impl.BasicConnectionDBImpl;
import static be.bittich.dynaorm.core.SystemConstant.DRIVER_NAME;
import be.bittich.dynaorm.dialect.Dialect;
import static be.bittich.dynaorm.ioc.BasicConfigurationBean.buildContainer;
import be.bittich.dynaorm.ioc.Container;

import static be.bittich.dynaorm.facad.IOCFacadRegister.registerConnectionDB;
import static be.bittich.dynaorm.facad.IOCFacadRegister.registerContainer;
import static be.bittich.dynaorm.facad.IOCFacadRegister.registerDialect;
import java.util.Properties;

/**
 *
 * @author Nordine Bittich
 */
public abstract class AbstractSetup implements Setup {

    private static final long serialVersionUID = 2619664551764003392L;

    private Properties dbProperties;
    private Dialect dialect;

    public Properties getDbProperties() {
        return dbProperties;
    }

    public AbstractSetup setDbProperties(Properties dbProperties) {
        this.dbProperties = dbProperties;
        return this;
    }

    public AbstractSetup(Properties dbProperties, Dialect dialect) {
        this.dbProperties = dbProperties;
        this.dialect = dialect;
    }

    private AbstractSetup() {
    }

    /**
     * Configure the bean for the connection to the database
     *
     */
    @Override
    public void configureConnection() {
        //before creation of the connection. In case if the user have implemented his own dialect and driver
        registerDriver();
        if (dbProperties == null) {
            throw new RuntimeException("DB Properties should'nt be empty!");

        }
        String driver = dbProperties.getProperty("driver");

        Boolean autoCommit = Boolean.parseBoolean(dbProperties.getProperty("autocommit"));
        ConnectionDB conn = new BasicConnectionDBImpl().
                setDriver(DRIVER_NAME.get(driver))
                .setLogin(dbProperties.getProperty("user"))
                .setAutoCommit(autoCommit)
                .setPassword(dbProperties.getProperty("password"))
                .setUrl(dbProperties.getProperty("url"))
                .setInitialSize(Integer.parseInt(dbProperties.getProperty("initialSize")));
        registerConnectionDB(conn);
    }

    @Override
    public void configureDialect() {
        if (dialect == null) {
            throw new RuntimeException("Dialect should'nt be empty!");
        }
        registerDialect(dialect);
    }

    @Override
    public final void setup(Container container) {
        registerContainer(container);
        configureConnection();
        configureDialect();
        buildContainer();
        doSetup();
    }

    protected abstract void doSetup();

    /**
     * if you want to register a driver BEFORE the connection is made, you
     * should override that method. You can add multiple driver i.e :
     * SystemConstant.addDriver("mssql","sun.jdbc.odbc.JdbcOdbcDriver");
     */
    protected void registerDriver() {
    }

}

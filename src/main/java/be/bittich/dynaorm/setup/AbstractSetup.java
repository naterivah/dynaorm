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
import static be.bittich.dynaorm.ioc.BasicConfigurationBean.registerBean;
import java.util.Properties;

/**
 *
 * @author Nordine Bittich
 */
public abstract class AbstractSetup implements Setup {

    private static final long serialVersionUID = 2619664551764003392L;
    private Properties dbProperties;

    public Properties getDbProperties() {
        return dbProperties;
    }

    public AbstractSetup setDbProperties(Properties dbProperties) {
        this.dbProperties = dbProperties;
        return this;
    }

    public AbstractSetup(Properties dbProperties) {
        this.dbProperties = dbProperties;
    }

    public AbstractSetup() {
    }

    /**
     * Configure the bean for the connection to the database
     *
     */
    @Override
    public void configureConnection() {
        String driver = dbProperties.getProperty("driver");
        Boolean autoCommit = Boolean.parseBoolean(dbProperties.getProperty("autocommit"));
        ConnectionDB conn = new BasicConnectionDBImpl().
                setDriver(DRIVER_NAME.get(driver))
                .setLogin(dbProperties.getProperty("user"))
                .setAutoCommit(autoCommit)
                .setPassword(dbProperties.getProperty("password"))
                .setUrl(dbProperties.getProperty("url"))
                .setInitialSize(Integer.parseInt(dbProperties.getProperty("initialSize")));
        registerBean("connectionDB", conn);
    }

    @Override
    public void setup() {
        configureConnection();
        buildContainer();
        doSetup();
    }



    protected abstract void doSetup();

}

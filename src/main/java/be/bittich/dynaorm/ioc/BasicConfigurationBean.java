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
package be.bittich.dynaorm.ioc;

import be.bittich.dyanorm.connection.ConnectionDB;
import be.bittich.dynaorm.connection.impl.BasicConnectionDBImpl;
import static be.bittich.dynaorm.core.SystemConstant.DIALECT;
import static be.bittich.dynaorm.core.SystemConstant.DRIVER_NAME;
import be.bittich.dynaorm.dialect.Dialect;
import be.bittich.dynaorm.exception.BeanAlreadyExistException;
import be.bittich.dynaorm.exception.BeanNotFoundException;
import be.bittich.dynaorm.exception.IOCContainerException;
import static be.bittich.dynaorm.ioc.BasicContainer.getContainer;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author Nordine
 */
public class BasicConfigurationBean implements Serializable {

    private static final long serialVersionUID = -4279720585191618511L;

    /**
     * Build the container with the mininmum required configuration bean.
     * if you want to add new Bean to the container, utilize the registerBean
     * method.

     * @param dbProperties
     */
    public static void buildContainer(Properties dbProperties) {
        configureConn(dbProperties);
        configureQueryRunner();
    }

    /**
     * Configure the bean for the connection to the database
     *
     * @param dbProperties
     */
    private static void configureConn(Properties dbProperties) {
        String driver = dbProperties.getProperty("driver");
        ConnectionDB conn = BasicConnectionDBImpl.getInstance()
                .setDriver(DRIVER_NAME.get(driver))
                .setLogin(dbProperties.getProperty("user"))
                .setPassword(dbProperties.getProperty("password"))
                .setUrl(dbProperties.getProperty("url"))
                .setInitialSize(Integer.parseInt(dbProperties.getProperty("initialSize")));
        registerBean("connectionDB", conn);

        //configure dialect
        configureDialect(driver);
    }

    /**
     * configure a QueryRunner bean
     */
    private static void configureQueryRunner() {

        ConnectionDB bean;
        try {
            bean = getContainer().inject("connectionDB");
            QueryRunner run = new QueryRunner(bean.getDataSource());
            registerBean("queryRunner", run);
        } catch (BeanNotFoundException ex) {
            Logger.getLogger(BasicConfigurationBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void configureDialect(String dialect) {
        Dialect dialectB;
        try {
            dialectB = (Dialect) Class.forName(DIALECT.get(dialect)).newInstance();
            registerBean("dialect", dialectB);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            Logger.getLogger(BasicConfigurationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Register a new bean
     *
     * @param <T>
     * @param t
     * @param label
     */
    public static <T> void registerBean(String label, T t) {
        Bean<T> bean = new Bean(label, t);
        try {
            getContainer().addBean(bean);
        } catch (BeanAlreadyExistException | IOCContainerException ex) {
            Logger.getLogger(BasicConfigurationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.bittich.dynaorm.ioc;

import be.bittich.dyanorm.connection.ConnectionDB;
import be.bittich.dynaorm.connection.impl.BasicConnectionDBImpl;
import be.bittich.dynaorm.core.SystemConstant;
import be.bittich.dynaorm.exception.BeanAlreadyExistException;
import be.bittich.dynaorm.exception.IOCContainerException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nordine
 */
public class BasicConfigurationBean implements Serializable {

    private static final long serialVersionUID = -4279720585191618511L;

    public static void configureConn() {
        ConnectionDB conn = BasicConnectionDBImpl.getInstance()
                .setDriver(SystemConstant.DRIVER_MYSQL)
                .setLogin("root")
                .setPassword("")
                .setUrl("jdbc:mysql://localhost/kikoo")
                .setInitialSize(5);
        Bean<ConnectionDB> bean = new Bean("connectionDB", conn);
        try {
            BasicContainer.getContainer().addBean(bean);
        } catch (BeanAlreadyExistException | IOCContainerException ex) {
            Logger.getLogger(BasicConfigurationBean.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}

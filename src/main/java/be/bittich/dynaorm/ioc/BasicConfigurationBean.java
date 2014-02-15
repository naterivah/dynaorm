package be.bittich.dynaorm.ioc;

import be.bittich.dyanorm.connection.ConnectionDB;
import be.bittich.dynaorm.connection.impl.BasicConnectionDBImpl;
import be.bittich.dynaorm.core.SystemConstant;
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
    
    public static void builder(Properties dbProperties) {
        configureConn(dbProperties);
        configureQueryRunner();
    }

    /**
     * Configure the bean for the connection to the database
     *
     * @param dbProperties
     */
    public static void configureConn(Properties dbProperties) {
        ConnectionDB conn = BasicConnectionDBImpl.getInstance()
                .setDriver(SystemConstant.DRIVER_NAME.get(dbProperties.getProperty("driver")))
                .setLogin(dbProperties.getProperty("user"))
                .setPassword(dbProperties.getProperty("password"))
                .setUrl(dbProperties.getProperty("url"))
                .setInitialSize(Integer.parseInt(dbProperties.getProperty("initialSize")));

        Bean<ConnectionDB> bean = new Bean("connectionDB", conn);
        try {
            getContainer().addBean(bean);
        } catch (BeanAlreadyExistException | IOCContainerException ex) {
            Logger.getLogger(BasicConfigurationBean.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * configure a QueryRunner bean
     */
    public static void configureQueryRunner() {
        try {
            ConnectionDB bean = getContainer().inject("connectionDB");
            QueryRunner run = new QueryRunner(bean.getDataSource());
            Bean<QueryRunner> beanR = new Bean("queryRunner", run);
            getContainer().addBean(beanR);
        } catch (BeanNotFoundException | BeanAlreadyExistException | IOCContainerException ex) {
            Logger.getLogger(BasicConfigurationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

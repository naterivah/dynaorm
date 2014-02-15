
package be.bittich.dynaorm.ioc;

import be.bittich.dynaorm.exception.BeanAlreadyExistException;
import be.bittich.dynaorm.exception.BeanNotFoundException;
import be.bittich.dynaorm.exception.IOCContainerException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author Nordine
 */
public class BasicContainer implements Serializable {

    private static final long serialVersionUID = -7812658198232446401L;

    private static BasicContainer container;

    public static QueryRunner getContainer(String queryRunner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private final Map<String, Bean> inject = new HashMap();

    private BasicContainer() {

    }
    
    /**
     * Return the container unique instance
     * @return the container
     */
    public static BasicContainer getContainer() {
        if (container == null) {
            container = new BasicContainer();
        }
        return container;
    }

    /**
     * Add a bean to the container
     * @param <T>
     * @param bean
     * @throws BeanAlreadyExistException
     * @throws IOCContainerException 
     */
    public <T> void addBean(Bean<T> bean) throws BeanAlreadyExistException, IOCContainerException {
        if (inject.containsValue(bean)) {
            throw new BeanAlreadyExistException("IOC exception: bean already exists");
        }
        if (inject.containsKey(bean.getId())) {
            throw new IOCContainerException("IOC exception: the id should be unique!");

        }
        this.inject.put(bean.getId(), bean);
    }

    /**
     * Remove a bean from the container
     * @param <T>
     * @param id
     * @return 
     */
    public <T> Bean<T> releaseBean(String id) {
        return inject.remove(id);

    }

    /**
     * The bean is injected by the container
     * @param <T>
     * @param id
     * @return
     * @throws BeanNotFoundException 
     */
    public <T> T inject(String id) throws BeanNotFoundException {
        if (inject.get(id) == null) {
            throw new BeanNotFoundException("Bean not found");
        }
        Bean<T> bean = inject.get(id);
        Class<T> clazz = bean.getClazz();
        return clazz.cast(inject.get(id).getBean());

    }

}

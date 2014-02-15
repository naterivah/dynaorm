
package be.bittich.dynaorm.ioc;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Representation of a bean. You need to specify an unique id for each bean
 * @author Nordine
 * @param <T>
 */
public class Bean<T> implements Serializable {

    private static final long serialVersionUID = -7983812596868153275L;
    private final String id;
    private final T bean;
    private final Class<T> clazz;
    
    public Class<T> getClazz() {

        return clazz;
    }

    public T getBean() {
        return bean;
    }

    public Bean(String key, T bean) {
        this.id = key;
        this.bean = bean;
        this.clazz=(Class<T>)bean.getClass();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Bean) {
            Bean<T> objBean = (Bean<T>) obj;
            return new EqualsBuilder().append(this.getBean(), objBean.getBean()).append(objBean.getId(), this.getId()).isEquals();

        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(bean).append(id).hashCode();
    }

    public String getId() {
        return id;
    }

}

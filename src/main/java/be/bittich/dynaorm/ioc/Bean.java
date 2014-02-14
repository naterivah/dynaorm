/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.bittich.dynaorm.ioc;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author Nordine
 * @param <T>
 */
public class Bean<T> implements Serializable {

    private static final long serialVersionUID = -7983812596868153275L;
    private final String id;
    private final T bean;

    Class<T> getClazz() {
        Class<T> clazz = (Class<T>) ((ParameterizedType) (getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0];

        return clazz;
    }

    public T getBean() {
        return bean;
    }

    public Bean(String key, T bean) {
        this.id = key;
        this.bean = bean;
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

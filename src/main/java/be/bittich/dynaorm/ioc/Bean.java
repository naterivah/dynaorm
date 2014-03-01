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

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Representation of a bean. You need to specify an unique id for each bean
 *
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
        this.clazz = (Class<T>) bean.getClass();
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

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    

}

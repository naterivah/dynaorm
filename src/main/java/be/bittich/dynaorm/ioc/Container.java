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

import be.bittich.dynaorm.exception.BeanAlreadyExistException;
import be.bittich.dynaorm.exception.BeanNotFoundException;
import be.bittich.dynaorm.exception.IOCContainerException;
import java.io.Serializable;

/**
 *
 * @author Nordine
 */
public interface Container extends Serializable {

    /**
     * Add a bean to the container
     *
     * @param <T>
     * @param bean
     * @throws BeanAlreadyExistException
     * @throws IOCContainerException
     */
    <T> void addBean(Bean<T> bean) throws BeanAlreadyExistException, IOCContainerException;

    /**
     * The bean is injected by the container
     *
     * @param <T>
     * @param id
     * @return
     * @throws BeanNotFoundException
     */
    <T> T inject(String id) throws BeanNotFoundException;

    /**
     * Safely inject a bean from the container. If the bean doesn't
     * exist, it returns null
     *
     * @param <T>
     * @param id
     * @return bean
     */
    <T> T injectSafely(String id);

    /**
     * Create a new instance of t. the bean is never saved in the container
     * @param <T>
     * @param tClazz
     * @return
     */
    <T> T newInstance(Class<T> tClazz);

    /**
     * Remove a bean from the container
     *
     * @param <T>
     * @param id
     * @return
     */
    <T> Bean<T> releaseBean(String id);
    
}

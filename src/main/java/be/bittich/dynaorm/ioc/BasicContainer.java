/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.bittich.dynaorm.ioc;

import be.bittich.dynaorm.exception.BeanAlreadyExistException;
import be.bittich.dynaorm.exception.BeanNotFoundException;
import be.bittich.dynaorm.exception.IOCContainerException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nordine
 */
public class BasicContainer implements Serializable {

    private static final long serialVersionUID = -7812658198232446401L;

    private static BasicContainer container;

    private final Map<String, Bean> inject = new HashMap();

    private BasicContainer() {

    }

    public static BasicContainer getContainer() {
        if (container == null) {
            container = new BasicContainer();
        }
        return container;
    }

    public void addBean(Bean bean) throws BeanAlreadyExistException, IOCContainerException {
        if (inject.containsValue(bean)) {
            throw new BeanAlreadyExistException("IOC exception: bean already exists");
        }
        if (inject.containsKey(bean.getId())) {
            throw new IOCContainerException("IOC exception: the id should be unique!");

        }
        this.inject.put(bean.getId(), bean);
    }

    public Bean releaseBean(String id) {
        return inject.remove(id);

    }

    public Bean inject(String id) throws BeanNotFoundException {
        if (inject.get(id) == null) {
            throw new BeanNotFoundException("Bean not found");
        }
        return inject.get(id);
    }

}

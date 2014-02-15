/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.bittich.dynaorm.repository;

import be.bittich.dynaorm.entity.Entity;
import be.bittich.dynaorm.entity.PrimaryKey;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.dbutils.ResultSetHandler;

/**
 *
 * @author Nordine
 * @param <T>
 */
public interface DynaRepository<T> extends Serializable {

    List<T> findAll();

    T findById(PrimaryKey... pk);

    T update(T t);

    T delete(T t);

    List<T> findBy(String value, String columnName);

    ResultSetHandler<List<T>> getListHandler();

    ResultSetHandler<T> getHandler();
    
}

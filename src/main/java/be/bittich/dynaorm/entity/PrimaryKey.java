/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package be.bittich.dynaorm.entity;

import java.io.Serializable;

/**
 *
 * @author Nordine
 * @param <T>
 */
public interface PrimaryKey<T> extends Serializable{
    T getIdValue();
    String getIdLabel();
    Class<T> getClazz();
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package be.bittich.dynaorm.exception;

/**
 *
 * @author Nordine
 */
public class BeanNotFoundException extends Exception{
    private static final long serialVersionUID = -5784646061904407507L;

    public BeanNotFoundException(String msg) {
        super(msg);
    }
    
}

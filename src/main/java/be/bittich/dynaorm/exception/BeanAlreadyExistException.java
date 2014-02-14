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
public class BeanAlreadyExistException extends Exception{
    private static final long serialVersionUID = -3310115588598965890L;

    public BeanAlreadyExistException(String msg) {
        super(msg);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package be.bittich.dyanorm.connection;

/**
 *
 * @author user
 */
public interface BasicConnectionDB extends ConnectionDB{
    String getDriver();

    String getUrl();

    String getPassword();

    String getLogin();
    
    Integer getInitialSize();

    BasicConnectionDB setDriver(String driver);
    
    BasicConnectionDB setInitialSize(Integer initialSize);

    BasicConnectionDB setUrl(String url);

    BasicConnectionDB setPassword(String password);

    BasicConnectionDB setLogin(String login);
}

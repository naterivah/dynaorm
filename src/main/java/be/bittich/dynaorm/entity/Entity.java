/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package be.bittich.dynaorm.entity;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Nordine
 */
public interface Entity extends Serializable {
    String getTableName();
    List<PrimaryKey> getPrimaryKeys();
}

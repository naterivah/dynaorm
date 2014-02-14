package be.bittich.dyanorm.connection;

import java.io.Serializable;
import javax.sql.DataSource;

/**
 *
 * @author Nordine Bittich
 */
public interface ConnectionDB extends Serializable {

    DataSource getDataSource();

   
    
}

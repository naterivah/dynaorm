package be.bittich.dynaorm.core;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nordine
 */
public interface SystemConstant {

    //Driver
    public static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
    public static final String DRIVER_HSQL = "org.hsqldb.jdbcDriver";
    //add your own driver here
    public static final Map<String, String> DRIVER_NAME = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put("mysql", DRIVER_MYSQL);
            put("hsqldb", DRIVER_HSQL);

        }
    };
}

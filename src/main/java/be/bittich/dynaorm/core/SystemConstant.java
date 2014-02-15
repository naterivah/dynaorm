package be.bittich.dynaorm.core;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nordine
 */
public interface SystemConstant {

    /**
     * *
     * Drivers
     */
    public static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
    public static final String DRIVER_HSQL = "org.hsqldb.jdbcDriver";

    /**
     * Supported Drivers
     */
    public static final String SUPPORTED_MYSQL = "mysql";
    public static final String SUPPORTED_HSQL = "hsql";

    public static final Map<String, String> DRIVER_NAME = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put(SUPPORTED_MYSQL, DRIVER_MYSQL);
            put(SUPPORTED_HSQL, DRIVER_HSQL);

        }

    };
    public static final Map<String, String> DIALECT = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put(SUPPORTED_MYSQL, "be.bittich.dynaorm.dialect.MySQLDialect");
            put(SUPPORTED_HSQL, "be.bittich.dynaorm.dialect.HSQLDialect");

        }
    };

}

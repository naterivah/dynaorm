/*
 * Copyright 2014 user.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.bittich.dynaorm.core;

import static be.bittich.dynaorm.core.DynaUtils.isNull;
import be.bittich.dynaorm.exception.PropertiesIncompleteException;
import java.util.Properties;

/**
 * Helper that create propertie object
 *
 * @author Nordine
 */
public class DbPropertiesBuilder {

    private final Properties propertie;

    public static final String PROPERTY_URL_KEY = "url";
    public static final String PROPERTY_USER_KEY = "user";
    public static final String PROPERTY_PASSWORD_KEY = "password";
    public static final String PROPERTY_INITIAL_SIZE_KEY = "initialSize";
    public static final String PROPERTY_DRIVER_KEY = "driver";
    public static final String PROPERTY_AUTOCOMMIT_KEY = "autocommit";

    public DbPropertiesBuilder() {
        propertie = new Properties();
    }

    public DbPropertiesBuilder setUrl(String url) {
        propertie.setProperty(PROPERTY_URL_KEY, url);
        return this;
    }

    public DbPropertiesBuilder setUser(String user) {
        propertie.setProperty(PROPERTY_USER_KEY, user);
        return this;
    }

    public DbPropertiesBuilder setPassword(String password) {
        propertie.setProperty(PROPERTY_PASSWORD_KEY, password);
        return this;
    }

    public DbPropertiesBuilder setInitialSize(String initialSize) {
        propertie.setProperty(PROPERTY_INITIAL_SIZE_KEY, initialSize);
        return this;
    }

    public DbPropertiesBuilder setDriver(String driver) {
        propertie.setProperty(PROPERTY_DRIVER_KEY, driver);
        return this;
    }

    public DbPropertiesBuilder setAutoCommit(String autocommit) {
        propertie.setProperty(PROPERTY_AUTOCOMMIT_KEY, autocommit);
        return this;
    }

    public Properties toProperties() {
        if (isNull(
                propertie.get(PROPERTY_URL_KEY),
                propertie.get(PROPERTY_USER_KEY),
                propertie.get(PROPERTY_PASSWORD_KEY),
                propertie.get(PROPERTY_INITIAL_SIZE_KEY),
                propertie.get(PROPERTY_DRIVER_KEY),
                propertie.get(PROPERTY_AUTOCOMMIT_KEY)
        )) {
            throw new PropertiesIncompleteException("Properties given is incomplete");
        }

        return propertie;
    }
}

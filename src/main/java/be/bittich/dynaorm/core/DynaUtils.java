/*
 * Copyright 2014 Nordine.
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nordine
 */
public class DynaUtils {

    public static Properties loadProperties(String propertiesFilename) throws FileNotFoundException {
        InputStream input = new FileInputStream(propertiesFilename);
        return loadProperties(input);
    }

    public static Properties loadProperties(InputStream input) {
        try {
            Properties props = new Properties();
            props.load(input);
            return props;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DynaUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DynaUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }



}

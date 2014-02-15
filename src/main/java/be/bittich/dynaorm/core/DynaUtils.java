package be.bittich.dynaorm.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

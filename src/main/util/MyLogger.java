package main.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Implementación de la clase Logger de librería java.util
 */
public class MyLogger {
    private static final Logger logger = Logger.getLogger(MyLogger.class.getName());
    private static FileHandler fh = null;

    private MyLogger (){}

    /**
     * Fija ubicación del archivo, formato de texto
     * También desactiva el log a la consola
     * @return objeto Logger
     */
    public static Logger init() {

        try {
            fh=new FileHandler("./error_log.txt",false);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        Logger l = Logger.getLogger("");
        fh.setFormatter(new SimpleFormatter());
        l.addHandler(fh);
        l.setUseParentHandlers(false);
        l.removeHandler(l.getHandlers()[0]);
        l.setLevel(Level.CONFIG);
        return l;
    }
    
}

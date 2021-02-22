package main.util;

import java.io.File;

public class Finalizer {

    private Finalizer () {}

     /**
     * Método que después de la ejecución, vacía la base de datos
     *  En SQLite se logra borrando manualmente el archivo
     * @return Estado de la operacion
     */
    public static boolean clean() {
        File dbFile = new File("./temp.sqlite");
        return dbFile.delete();
        }
    
}

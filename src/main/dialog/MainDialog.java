package main.dialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.data.DataProcessor;
import main.util.Finalizer;
import main.util.MyLogger;

public class MainDialog {

    private static final Logger LOGGER = MyLogger.init();

    /**
     * Método que provee el diálogo principal Solo acepta respuestas "si" y "no". Es
     * indiferente si la respuesta viene con mayúsculas o tilde En caso de ingresar
     * otra respuesta, el programa entra a un loop while hasta que se escriba una
     * respuesta aceptable Para evitar un loop infinito, se usa un contador que
     * limita a solo 3 intentos de respuestas no aceptadas.
     */
    public void getDialog() {

        System.out.println("¿Quiere procesar datos? (si/no)");

        BufferedReader br;
        String strClean;
        String inputStr;
        boolean pendingAnswer = true;
        int tryCounter = 0;

        try {
            br = new BufferedReader(new InputStreamReader(System.in));

            inputStr = br.readLine();
            strClean = cleanString(inputStr);

            while (pendingAnswer) {

                if (strClean.equals("si")) {
                    new DataProcessor().run();
                    System.out.println("Proceso ejecutado con éxito.");
                    pendingAnswer = false;
                } else if (strClean.equals("no") || tryCounter >= 2) {
                    System.out.println("Terminado (no se ejecutó nada).");
                    pendingAnswer = false;
                } else {
                    tryCounter++;
                    pendingAnswer = true;
                    inputStr = br.readLine();
                    strClean = cleanString(inputStr);
                }
            }
            br.close();

        } catch (IOException | SQLException | ParseException | ArrayIndexOutOfBoundsException e) {

            System.out.println("Error: no se pudo ejecutar el proceso.");
            System.out.println("Se ha generado un archivo con el texto completo del error (error_log.txt)\n");
            System.out.println(e.getMessage());
            LOGGER.log(Level.SEVERE, e.toString(),e);
        

        }finally{
            
            Finalizer.clean();
        }
    }

    /**
     * Método que normaliza el texto ingresado, eliminando acentos y eliminando
     * mayúsculas
     * 
     * @param s
     * @return s
     */
    private String cleanString(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        s = s.toLowerCase();
        return s;
    }
}

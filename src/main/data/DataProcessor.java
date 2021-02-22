package main.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import main.database.DataBaseProxy;
import main.util.DateParser;
/**
 * Clase que gestiona la lectura de los archivos CSV y su inserción en SQLite
 */
public class DataProcessor {


    /**
     * Método que contiene el proceso completo:
     * Iniciar DB, crear tablas, llenarlas, 
     * generar los TXT con los resultados de las queries
     * y borrar la DB.
     * 
     * @throws SQLException
     * @throws IOException
     * @throws ParseException
     */
    public void run() throws SQLException, IOException, ParseException {
        new DataBaseProxy().initDb();
        populateTables();
        generateOutput();
    }
    
        /**
     * Método que fija parámetros necesarios para parsear archivos CSV
     * 
     * @throws IOException
     * @throws SQLException
     * @throws ParseException
     */
    private void populateTables() throws IOException, SQLException, ParseException {

        String csvFile = "./personas.csv";
        String header = "ID;Rut;Nombre;FechaPago;Monto;Moroso;DiasMora;CobranzaJudicial";
        iterateRows(csvFile, header, "personas");

        csvFile = "./direccion.csv";
        header = "ID;IDPERSONA;DIRECCION;COMUNA";
        iterateRows(csvFile, header, "direcciones");

    }


    /**
     * Método que ejecuta las queries solicitadas en el ejercicio Y las exporta a
     * archivos TXT
     * 
     * @throws SQLException
     * @throws ParseException
     * @throws FileNotFoundException
     */
    private void generateOutput() throws SQLException, ParseException, FileNotFoundException {

        DataBaseProxy dbProxy = new DataBaseProxy();
        ResultSet rs = dbProxy.selectWhere("DIASMORA > 90", "");
        loopResultSet(rs, "Mora sobre 90 días:", "Mora_sobre_90_dias");

        rs = dbProxy.selectWhere("FECHAPAGO < date('2020-06-01')", "");
        loopResultSet(rs, "Pago anterior a junio:", "Pago_anterior_junio");

        String join = "JOIN tbl_direcciones ON tbl_personas.ID = tbl_direcciones.ID_PERSONA ";
        rs = dbProxy.selectWhere(" MONTO > 60000 AND MOROSO = 'true'", join);
        loopResultSet(rs, "Morosos sobre 60 mil:", "Mora_sobre_60_mil");

        rs = dbProxy.selectWhere("COMUNA = 'Santiago' AND MOROSO = 'true'", join);
        loopResultSet(rs, "Morosos en comuna de Santiago:", "Morosos_Santiago");

        String whereComunas = "NOT COMUNA IN ('Quilicura', 'Pudahuel', 'Maipu', 'San Bernardo', 'PTE Alto', 'La Florida', 'Peñalolen', 'La Reina', 'Las Condes', 'Lo Barnechea', 'Huechuraba', 'Conchalí', 'Renca', 'Independencia', 'Recoleta', 'Cerro Navia','Quinta Normal', 'Lo Prado', 'Estación Central', 'Santiago', 'Providencia', 'Ñuñoa', 'Macul', 'San Joaquin','San Miguel', 'PA Cerda', 'Cerrillos', 'Lo Espejo', 'La Cisterna', 'San Ramon', 'La Granja', 'La Pintana', 'El Bosque', 'Vitacura')";
        rs = dbProxy.selectWhere(whereComunas, join);
        loopResultSet(rs, "Fuera del radio urbano:", "Fuera_radio_urbano");

        rs = dbProxy.selectWhere("MOROSO = 'false'", join);
        loopResultSet(rs, "No morosos:", "No_morosos");

    }

    /**
     * @throws SQLException
     * @throws ParseException
     * @throws FileNotFoundException
     * 
     */
    private void loopResultSet(ResultSet rs, String header, String filename)
            throws SQLException, ParseException, FileNotFoundException {

        StringBuilder text = new StringBuilder();
        text.append(header);
        text.append("\n\n");

        while (rs.next()) {

            text.append(rs.getString("ID") + ", ");
            text.append(rs.getString("NOMBRE") + ", ");
            text.append(rs.getString("RUT") + ", ");

            switch (filename) {
                case "Mora_sobre_90_dias":

                    text.append("días de mora: ");
                    text.append(rs.getString("DIASMORA") + " \n");
                    break;
                case "Pago_anterior_junio":

                    text.append("fecha de pago: ");
                    text.append(new DateParser().sqlToCsv(rs.getString("FECHAPAGO")));
                    break;
                case "Mora_sobre_60_mil":
                case "Morosos_Santiago":
                case "No_morosos":
                    text.append("mora: ");
                    text.append(rs.getString("MONTO"));
                    text.append(", dirección: ");
                    text.append(rs.getString("DIRECCION"));
                    text.append(", " + rs.getString("COMUNA"));
                    break;
                case "Fuera_radio_urbano":

                    text.append("dirección: ");
                    text.append(rs.getString("DIRECCION"));
                    text.append(", " + rs.getString("COMUNA"));

                    break;
                default:
                    break;
            }
            text.append("\n");
        }

        PrintWriter out = new PrintWriter("./"+filename+".txt");
        out.println(text);
        out.close();
    }



    /**
     * Método que recorre cada fila del archivo CSV para su inserción en SQLite
     * 
     * @param csvFile -> el archivo CSV
     * @param header  -> la primera fila del archivo - que debe ser saltada
     * @param entity  -> si corresponde a personas o direcciones, para saber a qué
     *                método llamar al momento de insertar la data
     * @throws IOExceptionon
     * @throws SQLException
     * @throws ParseException
     */
    private void iterateRows(String csvFile, String header, String entity)
            throws IOException, SQLException, ParseException {

        BufferedReader br = null;
        String line = "";
        br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile),StandardCharsets.ISO_8859_1));
        
        while ((line = br.readLine()) != null) {
            String []row = line.split(";");
            if (!line.equals(header)) new DataBaseProxy().insertInto(entity,row);
 
        }            
        br.close();
    }


}

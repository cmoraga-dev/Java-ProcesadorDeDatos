package main.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Clase que contiene operaciones con el formato de fecha
 * El formato contenido en el documento CSV (dd/MM/yyyy)
 * Y el formato necesario para operar en SQLite (yyyy-MM-dd)
 */
public class DateParser {

    SimpleDateFormat csv = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sql = new SimpleDateFormat("yyyy-MM-dd");

    public String sqlToCsv(String date) throws ParseException {
        return csv.format(sql.parse(date));  
    }

    public String csvToSql (String date) throws ParseException {
        return sql.format(csv.parse(date));    
    }
    
}

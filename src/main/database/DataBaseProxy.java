package main.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import main.util.DateParser;

/**
 * Clase que gestiona operaciones específicas en la DB
 */
public class DataBaseProxy {

    StringBuilder sqlDireccion;
    StringBuilder sqlPersonas;

    /**
     * Inicializa la DB, con las tablas necesarias para almacenar data
     * 
     * @throws SQLException
     */
    public void initDb() throws SQLException {

        DataBase db = new DataBase();
        db.execQuery(createTblPersonas());
        db.execQuery(createTblDirecciones());

    }


    /**
     * Método público, que decide con qué metodo insertar la data en SQLite
     * 
     * @param entity
     * @param row
     * @throws SQLException
     * @throws ParseException
     */
    public void insertInto(String entity, String[] row) throws SQLException, ParseException {

        switch (entity) {
            case "personas":
                insertPersonas(row);
                break;

            case "direcciones":
                insertDirecciones(row);
                break;
            default:
                break;
        }
    }

    /**
     * Inserción de datos en tabla Personas Requiere cambiar formato de fecha
     * 
     * @param row
     * @throws SQLException
     * @throws ParseException
     */
    private void insertPersonas(String[] row) throws SQLException, ParseException {

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT OR IGNORE INTO tbl_personas ");
        sql.append("(ID,RUT,NOMBRE,FECHAPAGO,MONTO,MOROSO,DIASMORA,COBRANZAJUDICIAL) ");
        sql.append("VALUES (");
        sql.append(row[0] + ", ");
        sql.append("'"+row[1]+"', ");
        sql.append("'"+row[2]+"', ");
        sql.append("'"+ new DateParser().csvToSql(row[3])+"', ");
        sql.append(row[4]+", ");
        sql.append("'"+row[5]+"', ");
        sql.append(row[6]+", ");
        sql.append("'"+row[7]+"' ");
        sql.append(" );");
        new DataBase().execQuery(sql.toString());
    }


    /**
     * Inserción de datos en tabla Direcciones
     * @param row
     * @throws SQLException
     */
    private void insertDirecciones (String[] row) throws SQLException {

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT OR IGNORE INTO tbl_direcciones ");
        sql.append("(ID,ID_PERSONA,DIRECCION,COMUNA) ");
        sql.append("VALUES (");
        sql.append(row[0] + ", ");
        sql.append(row[1] + ", ");
        sql.append("'"+row[2]+"', ");
        sql.append("'"+row[3]+"' ");
        sql.append(" );");

        new DataBase().execQuery(sql.toString());
    }

    /**
     * Creación de tabla Direcciones
     * @return String
     */
    private String createTblDirecciones () {

        sqlDireccion = new StringBuilder();
        sqlDireccion.append("CREATE TABLE IF NOT EXISTS  tbl_direcciones ( ");
        sqlDireccion.append("ID INTEGER PRIMARY KEY, ");
        sqlDireccion.append("ID_PERSONA INTEGER, ");
        sqlDireccion.append("DIRECCION TEXT, ");
        sqlDireccion.append("COMUNA TEXT );");
        return sqlDireccion.toString();

    }

    /**
     * Creación de tabla Personas
     * @return String
     */
    private String createTblPersonas () {
        
        sqlPersonas = new StringBuilder();
        sqlPersonas.append("CREATE TABLE IF NOT EXISTS tbl_personas ( ");
        sqlPersonas.append("ID INTEGER PRIMARY KEY, ");
        sqlPersonas.append("RUT TEXT, ");
        sqlPersonas.append("NOMBRE TEXT, ");
        sqlPersonas.append("FECHAPAGO TEXT, ");
        sqlPersonas.append("MONTO INTEGER,");
        sqlPersonas.append("MOROSO INTEGER,");
        sqlPersonas.append("DIASMORA INTEGER,");
        sqlPersonas.append("COBRANZAJUDICIAL INTEGER );");

        return sqlPersonas.toString();
    }

    public ResultSet selectWhere (String condition,String join) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("select * ");
        sql.append("from ");
        sql.append("tbl_personas ");
        sql.append(join);
        sql.append("where ");
        sql.append(condition);

        return new DataBase().execSelect(sql.toString());
    }
    
}

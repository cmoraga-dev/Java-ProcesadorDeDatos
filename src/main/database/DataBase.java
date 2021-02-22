package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * class Database clase que crea una base de datos temporal SQLite en caso de no
 * existir También gestiona las operaciones posibles dentro de dicha DB
 */
public class DataBase {

    Connection conn = null;

    public DataBase() throws SQLException {
        createNewDatabase();
    }

    /**
     * Creación de DB mediante getConnection
     * 
     * @throws SQLException
     */
    protected void createNewDatabase() throws SQLException {

        conn = DriverManager.getConnection("jdbc:sqlite:./temp.sqlite");

    }

    /**
     * Método genérico para ejecutar queries en la DB creada
     * 
     * @param sql
     * @throws SQLException
     */
    public void execQuery(String sql) throws SQLException {

        conn = DriverManager.getConnection("jdbc:sqlite:./temp.sqlite");
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
    }

    /**
     * Método que ejecuta queries que retornan un RS, típicamente un SELECT
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet execSelect(String sql) throws SQLException {
        
        conn = DriverManager.getConnection("jdbc:sqlite:./temp.sqlite");
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

}

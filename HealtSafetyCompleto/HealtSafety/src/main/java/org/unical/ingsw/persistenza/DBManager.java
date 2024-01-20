package org.unical.ingsw.persistenza;

import org.unical.ingsw.persistenza.dao.DocumentoDao;
import org.unical.ingsw.persistenza.dao.postgres.DocumentoDaoPostgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static DBManager instance = null;

    private DBManager(){}

    public static DBManager getInstance(){
        if (instance == null){
            instance = new DBManager();
        }
        return instance;
    }

    Connection con = null;

    public Connection getConnection(){
        if (con == null){
            try {
                con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/postgres", "postgres", "postgres");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return con;
    }

    public DocumentoDao getDocumentoDao(){
        return new DocumentoDaoPostgres(getConnection());
    }
}

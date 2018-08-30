/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;
import db.controllers.LogMessages;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Emiliano
 */
public class DBConnection {
    
    private Connection con;
    private LogMessages LogSms;
    
    public DBConnection () {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            String server = "jdbc:mysql://localhost:3306/pedidossmc";
            String user = "root";
            String pswd = "";
            
            //Cambiar por datos del servidor donde se hospedará la BD
            /*
            String server = "";
            String user = ""
            String pswd = "";            
            */
                                    
            con = DriverManager.getConnection(server, user, pswd);
            
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            LogSms.write_DBException("Error al intentar conectar con el"
                    + " servidor de BD");
        } 
    }
    
    public Connection getCon () {
        return this.con;
    }
}

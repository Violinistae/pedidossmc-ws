/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.controllers;

import db.models.UsuarioModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Emiliano
 */
public class UsuarioController {
    private Connection con;
    private LogMessages LogSms;

    public UsuarioController (Connection con) {
        this.con = con;
    }
    
    public ArrayList<UsuarioModel> readAllUsuario () {
        PreparedStatement sqlStmt;
        
        ArrayList<UsuarioModel> usuarios = new ArrayList();
        UsuarioModel usuario;
        ResultSet rs;
        
        try {
            sqlStmt = con.prepareStatement("Select * from usuario order by IdUsuario");
            rs = sqlStmt.executeQuery();
            
            while(rs.next()) {
                usuario = new UsuarioModel();
                usuario.setIdUsuario(rs.getInt("IdUsuario"));
                usuario.setUsername(rs.getString("Username"));
                usuario.setPassword(rs.getString("Password"));
                usuario.setNombres(rs.getString("Nombres"));
                usuario.setApellidos(rs.getString("Apellidos"));
                usuario.setHash(rs.getString("Hash"));
                usuario.setTipoUsuario(rs.getInt("TipoUsuario"));
                usuarios.add(usuario);
            }
            
        } catch (Exception e) {
            LogSms.write_DBException("Error al consultar 'Usuario'");
        }
                       
        return usuarios;
    }
    
    public UsuarioModel readUsuarioById (int IdU) {
        PreparedStatement sqlStmnt;
        UsuarioModel usuario = new UsuarioModel();
        ResultSet rs;
        
        try {
            sqlStmnt = con.prepareStatement("Select * from usuario where IdUsuario = ?");
            sqlStmnt.setInt(1, IdU);
            rs = sqlStmnt.executeQuery();
            rs.next();
            usuario.setIdUsuario(rs.getInt("IdUsuario"));
            usuario.setUsername(rs.getString("Username"));
            usuario.setPassword(rs.getString("Password"));
            usuario.setNombres(rs.getString("Nombres"));
            usuario.setApellidos(rs.getString("Apellidos"));
            usuario.setHash(rs.getString("Hash"));
            usuario.setTipoUsuario(rs.getInt("TipoUsuario"));
            
        } catch (SQLException ex){
            LogSms.write_DBException("Error al consultar 'usuario' mediante Id");
        }
        return usuario;
    }
    
    private boolean checkExistUsername (String Username) {
        PreparedStatement sqlStmt;
        ResultSet rs;
        boolean flag =  false;
        
        try {
            sqlStmt = con.prepareStatement("Select * from usuario where Username = ?");
            sqlStmt.setString(1, Username);
            rs = sqlStmt.executeQuery();
            
            if (rs.next()) {            //Existe ese usuario
                flag = true;
            } else {                    //No existe ese usuario
                flag = false;
            }
                        
        } catch (SQLException ex){
            LogSms.write_DBException("Error al consultar 'usuario' mediante Username");
        }
        return flag;
    }
    
    public int createUsuario (String Username, String Password, String Nombres, 
            String Apellidos, String Hash, int TipoUsuario) {               
        
        if (this.checkExistUsername(Username)) {  // Ya existe ese usuario
            return -1;
        }
        
        PreparedStatement sqlStmt;
        try {
            //Hash= Generate hash
            //Password = Generate password
            
            sqlStmt = con.prepareStatement(
                    "Insert into usuario (IdUsuario, Username, Password,"
                            + "Nombres, Apellidos, Hash, TipoUsuario) values "
                            + "(?, ?, ?, ?, ?, ?)");
            sqlStmt.setString(1, Username);
            sqlStmt.setString(2, Password);
            sqlStmt.setString(3, Nombres);
            sqlStmt.setString(4, Apellidos);
            sqlStmt.setString(5, Hash);
            sqlStmt.setInt(6, TipoUsuario);
            sqlStmt.executeUpdate();
            
            return 1;           //Exito al crear un nuevo usuario
                        
        } catch (Exception e) {     //No se pudo crear el nuevo usuario
            LogSms.write_DBException("Error al crear 'usuario'");
            return 0;
        }
        
    }    
    
    public int verifyLogin (String Username, String Password) {        
        
        if (this.checkExistUsername(Username)) {  // Ya existe ese usuario
            return 0;
        }
        
        PreparedStatement sqlStmt;
        ResultSet rs;
        
        try {
            sqlStmt = con.prepareStatement("Select Password, TipoUsuario"
                    + "from usuario where Username = ?");
            sqlStmt.setString(1, Username);
            rs = sqlStmt.executeQuery();
            
            if (!rs.next()) {            //No existe ese usuario
                return 0;
            } else {                    //Existe ese usuario               
                
                //Password = Create hash to compare
                if (Password.equals(rs.getString("Password"))) { //Login Exitoso                    
                    //Switch for interface on WS Method and limit on Android
                    return rs.getInt("TipoUsuario");
                } else {
                    return 0;
                }                                
            }            
        } catch (Exception e) {
            LogSms.write_DBException("Error al consultar 'usuario' para Login");
            return 0;
        }
        
    }
    
}

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
    
    // ----------------------- Private Methods -------------------------
    
    /**
     * Método que realiza una consulta a BD para verificar la existencia de
     * un nombre de usuario (Username)
     * @param Username  Username a verificar existencia
     * @return Estado de existencia del username
     */
    private int checkExistUsername (String Username) {
        PreparedStatement sqlStmt;
        ResultSet rs;
        int IdU =  -1;
        
        try {
            sqlStmt = con.prepareStatement("Select * from usuario where Username = ?");
            sqlStmt.setString(1, Username);
            rs = sqlStmt.executeQuery();
            
            if (rs.next()) {            //Existe ese usuario
                
                IdU = rs.getInt("IdUsuario");
            } else {                    //No existe ese usuario
                IdU = -1;
            }
                        
        } catch (SQLException ex){
            LogSms.write_DBException("Error al consultar 'usuario' mediante Username");
        }
        return IdU;
    }        
      
    // --------------------- Public Methods For WS ---------------------
    
    /**
     * Método que realiza una consulta a BD para obtener todos los usuarios
     * del sistema.
     * @return  ArrayList de tipo UsuarioModel con todos los usuarios 
     *          existentes en el sistema
     */
    public ArrayList<UsuarioModel> readAllUsuario () {
        PreparedStatement sqlStmt;
        
        ArrayList<UsuarioModel> usuarios = new ArrayList();
        UsuarioModel usuario;
        ResultSet rs;
        
        try {
            sqlStmt = this.con.prepareStatement("Select * from usuario order by IdUsuario");
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
            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al consultar 'Usuario'");
        }
                       
        return usuarios;
    }
    
    /**
     * Método que realiza una consulta a BD para obtener la información de
     * determinado usuario.
     * @param IdU   Id de Usuario con el que se realizará la consulta a BD
     * @return      Objeto de tipo UsuarioModel con la información del usuario
     *              consultado
     */
    public UsuarioModel readUsuarioById (int IdU) {
        
        PreparedStatement sqlStmnt;
        UsuarioModel usuario = new UsuarioModel();
        ResultSet rs;
        
        try {
            sqlStmnt = this.con.prepareStatement("Select * from usuario where IdUsuario = ?");
            sqlStmnt.setInt(1, IdU);
            rs = sqlStmnt.executeQuery();
            
            if (rs.next()) {
                usuario.setIdUsuario(rs.getInt("IdUsuario"));
                usuario.setUsername(rs.getString("Username"));
                usuario.setPassword(rs.getString("Password")); // Retornar campo ?
                usuario.setNombres(rs.getString("Nombres"));
                usuario.setApellidos(rs.getString("Apellidos"));
                usuario.setHash(rs.getString("Hash"));
                usuario.setTipoUsuario(rs.getInt("TipoUsuario"));
            } else {
                usuario.setIdUsuario(-1);
            }            
                        
        } catch (SQLException ex){
            LogSms.write_DBException("Error al consultar 'usuario' mediante Id");
            usuario.setIdUsuario(-2);
        }
        return usuario;
    }    
    
    //Read usuario by Nombres ?
    
    /** 
     * Método que realiza una consulta a BD para obtener la información de
     * determinado usuario.
     * @param Username  Username con el que se realizará la consulta a BD
     * @return  
     */
    public UsuarioModel readUsuarioByUsername (String Username) {
        
        PreparedStatement sqlStmnt;
        UsuarioModel usuario = new UsuarioModel();
        ResultSet rs;
        
        try {
            sqlStmnt = this.con.prepareStatement("Select * from usuario where Username = ?");
            sqlStmnt.setString(1, Username);
            rs = sqlStmnt.executeQuery();
                        
            if (rs.next()) {
                usuario.setIdUsuario(rs.getInt("IdUsuario"));
                usuario.setUsername(rs.getString("Username"));
                usuario.setPassword(rs.getString("Password"));  // Retornar campo ?
                usuario.setNombres(rs.getString("Nombres"));
                usuario.setApellidos(rs.getString("Apellidos"));
                usuario.setHash(rs.getString("Hash"));
                usuario.setTipoUsuario(rs.getInt("TipoUsuario"));
            } else {
                usuario.setIdUsuario(-1);
            }            
            
        } catch (SQLException ex){
            LogSms.write_DBException("Error al consultar 'usuario' mediante Id");
            usuario.setIdUsuario(-2);
        }
        return usuario;
    }    
    
    /**
     * Método para buscar usuarios en base a un Username ingresado o semiingresado
     * @param Username  Username con el que se compararán los resultados en la BD
     * @return 
     */
    public ArrayList<UsuarioModel> readUsuarioLikeUsername (String Username) {
        PreparedStatement sqlStmt;
        
        ArrayList<UsuarioModel> usuarios = new ArrayList();
        UsuarioModel usuario;
        ResultSet rs;
        
        try {
            sqlStmt = this.con.prepareStatement("Select * from usuario "
                    + "where Username LIKE '?%' order by IdUsuario");
            sqlStmt.setString(1, Username);
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
            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al consultar 'Usuario' like Username");
        }
                       
        return usuarios;
    }       
    
    /**
     * Método para crear usuarios
     * @param Username
     * @param Password
     * @param Nombres
     * @param Apellidos
     * @param Hash
     * @param TipoUsuario
     * @return 
     */
    public int createUsuario (String Username, String Password, String Nombres, 
            String Apellidos, String Hash, int TipoUsuario) {               
        
        int IdU = this.checkExistUsername(Username);
        
        if (IdU > 0) {  // Ya existe ese usuario
            return -1;
        }
        
        PreparedStatement sqlStmt;
        try {
            //Hash= Generate hash
            //Password = Generate password
            
            sqlStmt = this.con.prepareStatement(
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
                        
        } catch (SQLException e) {     //No se pudo crear el nuevo usuario
            LogSms.write_DBException("Error al crear 'usuario'");
            return 0;
        }
        
    }    
    
    /**
     * Método para verifcar información de login en BD
     * @param Username  Nombre de usuario a verificar en BD
     * @param Password  Contraseña de Usuario a verificar en la BD
     * @return 
     */
    public int verifyLogin (String Username, String Password) {                       
        
        int IdU = this.checkExistUsername(Username);
        
        if (IdU < 1) {  // No existe ese usuario
            return -1;
        }
        
        PreparedStatement sqlStmt;
        ResultSet rs;        
        try {
            sqlStmt = this.con.prepareStatement("Select Username, Password, TipoUsuario"
                    + "from usuario where IdUsuario = ?");
            sqlStmt.setInt(1, IdU);
            rs = sqlStmt.executeQuery();
            
            if (!rs.next()) {            //No existe ese usuario
                return 0;
            } else {                    //Existe ese usuario
                
                //Password = Create hash to compare
                if (Username.equals(rs.getString("Username")) && 
                        Password.equals(rs.getString("Password"))) {
                    //Login Exitoso
                    
                    //Switch for interface on WS Method and limit on Android
                    return rs.getInt("TipoUsuario");

                } else {
                    return 0;
                }
                                                            
            }            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al consultar 'usuario' para Login");
            return 0;
        }
        
    }
    
    /**
     * Método para actualizar información de usuario
     * @param OldUsername       Nombre de usuario anterior (Tomado de Variable de "Sesión")
     * @param Username          Nuevo nombre de usuario
     * @param Nombres           Nombres
     * @param Apellidos         Apellidos
     * @return 
     */
    public int updateUsuarioInfo (String OldUsername, String Username, String Nombres, 
            String Apellidos) {               
        
        if (!OldUsername.equals(Username)) {
            int IdU = this.checkExistUsername(Username);
        
            if (IdU > 0) {  // Ya existe ese usuario
                return -1;
            }
        }
                       
        PreparedStatement sqlStmt;
        try {                       
            
            sqlStmt = this.con.prepareStatement(
                    "Update usuario Set "
                            + "Username = ?, "
                            + "Nombres = ?,"
                            + "Apellidos = ? where Username = ?"
            );
            sqlStmt.setString(1, Username);
            sqlStmt.setString(2, Nombres);
            sqlStmt.setString(3, Apellidos);
            sqlStmt.setString(4, OldUsername);
            
            if (sqlStmt.executeUpdate() > 0) 
                return 1;           //Exito al actualizar info de usuario
            return -2;      //No se encontró al usuario a actualizar
                        
        } catch (SQLException e) {     //No se pudo actualizar info de usuario
            LogSms.write_DBException("Error al actualizar info 'usuario'");
            return 0;
        }
    }
    
    /**
     * Método para cambiar contraseña de usuario
     * @param Username      Nombre de usuario
     * @param OldPswd       Contraseña anterior
     * @param NewPswd       Nueva contraseña
     * @return 
     */
    public int changeUserPassword (String Username, String OldPswd, 
            String NewPswd) {
        
        int IdU = this.checkExistUsername(Username);
        
        if (IdU < 1) {  // No existe ese usuario
            return -1;
        }
        
        PreparedStatement sqlStmt;
        try {
            
            //OldPswd = Hash for check old password
            //NewPswd = Hash for update new password
            
            sqlStmt = this.con.prepareStatement(
                    "Update usuario Set "
                            + "Password = ?"
                            + "where Username = ? and Password = ?"
            );
            sqlStmt.setString(1, NewPswd);
            sqlStmt.setString(2, Username);
            sqlStmt.setString(3, OldPswd);            
            if (sqlStmt.executeUpdate() > 0) 
                return 1;    //Actualización de contraseña correcta                        
            return -2;      //Información para cambiar contraseña es incorrecta
            
        } catch (SQLException e) {  
            LogSms.write_DBException("Error al actualizar password 'usuario'");
            return 0;
        }
        
    }
    
    /**
     * Método para eliminar usuario cuando se confirme la acción
     * @param IdU       Registro de usuario enviado para confirmar la accion
     * @return 
     */
    public int deleteUsuarioById (int IdU) {
        PreparedStatement sqlStmt;
        
        try {
            sqlStmt = this.con.prepareStatement("Delete from usuario where IdUsuario= ?");
            sqlStmt.setInt(1, IdU);
            if (sqlStmt.executeUpdate() > 0)
                return 1;
            return -1;              //No se pudo encontrar al usuario a eliminar

        } catch (SQLException ex){
            LogSms.write_DBException("Error al eliminar 'usuario' mediante Id");
            return 0;
        }        
    }
    
}

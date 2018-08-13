/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.controllers;

import db.models.ClientesModel;

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
public class ClientesController {
    private Connection con;
    private LogMessages LogSms;

    public ClientesController (Connection con) {
        this.con = con;
    }
    
    // ----------------------- Private Methods -------------------------
    
    
    // --------------------- Public Methods For WS ---------------------
    
    public ArrayList<ClientesModel> readAllClientes () {
        PreparedStatement sqlStmt;
        
        ArrayList<ClientesModel> clientes = new ArrayList();
        ClientesModel cliente;
        ResultSet rs;
        
        try {
            sqlStmt = this.con.prepareStatement("Select * from clientes "
                    + "order by CodCliente");
            
            rs = sqlStmt.executeQuery();
            
            while(rs.next()) {
                cliente = new ClientesModel();
                cliente.setIdCliente(rs.getInt("IdCliente"));
                cliente.setCodCliente(rs.getString("CodCliente"));
                cliente.setNombres(rs.getString("Nombres"));
                cliente.setApellidos(rs.getString("Apellidos"));               
                clientes.add(cliente);
            }
            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al consultar 'Clientes'");
        }
        
        return clientes;
    }
    
    public ClientesModel readClienteByCodCliente (String CodCliente) {
        PreparedStatement sqlStmnt;
        ClientesModel cliente = new ClientesModel();
        ResultSet rs;
        
        try {
            sqlStmnt = this.con.prepareStatement("Select * from clientes where CodCliente = ?");
            sqlStmnt.setString(1, CodCliente);
            rs = sqlStmnt.executeQuery();
            
            rs.next();
            cliente.setIdCliente(rs.getInt("IdCliente"));
            cliente.setCodCliente(rs.getString("CodCliente"));
            cliente.setNombres(rs.getString("Nombres"));
            cliente.setApellidos(rs.getString("Apellidos"));
            
            
        } catch (SQLException ex){
            LogSms.write_DBException("Error al consultar 'cliente' mediante Id");
        }
        return cliente;
    }
    
    public int createCliente (String CodCliente, String Nombres,
            String Apellidos) {
        
        ClientesModel verfCliente = this.readClienteByCodCliente(CodCliente);
        if (verfCliente.getCodCliente().equals(CodCliente)) {
            return -1;          //Ya existe un cliente con ese Código
        }
        
        PreparedStatement sqlStmt;
        try {
            sqlStmt = this.con.prepareStatement("Insert into clientes "
                    + "(CodCliente, Nombres, Apellidos) values (?, ?, ?)");
            sqlStmt.setString(1, CodCliente);
            sqlStmt.setString(2, Nombres);
            sqlStmt.setString(3, Apellidos);
            sqlStmt.executeUpdate();
            
            return 1;       //Exito al crear nuevo cliente
            
        } catch (SQLException e) {  //No se pudo crear el nuevo cliente
            LogSms.write_DBException("Error al crear 'cliente'");
            return 0;
        }
        
    }
    
    public int updateClienteInfo (int IdCliente,  String CodCliente, 
            String Nombres, String Apellidos) {        
        //Verificar en App si se modificaron los campos
        
        ClientesModel verfCliente = this.readClienteByCodCliente(CodCliente);
        if (verfCliente.getCodCliente().equals(CodCliente)) {
            return -1;          //Ya existe un cliente con ese Código
        }
        
        PreparedStatement sqlStmt;
        try {                       
            
            sqlStmt = this.con.prepareStatement(
                    "Update clientes Set "
                            + "CodCliente = ?, "
                            + "Nombres = ?,"
                            + "Apellidos = ? where IdCliente = ?"
            );
            sqlStmt.setString(1, CodCliente);
            sqlStmt.setString(2, Nombres);
            sqlStmt.setString(3, Apellidos);
            sqlStmt.setInt(4, IdCliente);
            
            if (sqlStmt.executeUpdate() > 0)
                return 1;           //Exito al actualizar info de cliente
            return -2;      //No se encontró al cliente a actualizar
                        
        } catch (SQLException e) {     //No se pudo actualizar info de cliente
            LogSms.write_DBException("Error al actualizar info 'usuario'");
            return 0;
        }
        
    }
    
    public int deleteClienteById (int IdCl) {
        PreparedStatement sqlStmt;
        
        try {
            sqlStmt = this.con.prepareStatement("Delete * from clientes "
                    + "where IdClientes = ?");
            sqlStmt.setInt(1, IdCl);
            if (sqlStmt.executeUpdate() > 0) {
                return 1;
            }
            return -1;
        } catch (SQLException ex) {
            LogSms.write_DBException("Error al eliminar 'cliente' mediante Id");
            return 0;
        }
    }
    
}

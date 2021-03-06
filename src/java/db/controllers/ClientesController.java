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
    
    /**
     * Método para obtener la lista de todos los clientes existentes en el
     * sistema.
     * @return  Lista de Clientes en el sistema
     */
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
    
    /**
     * Método para consultar la información de un cliente en base a CodCliente
     * @param CodCliente    Código de cliente con el que se realizará la consulta
     * @return    ClientesModel con información del cliente
     */
    public ClientesModel readClienteByCodCliente (String CodCliente) {
        PreparedStatement sqlStmnt;
        ClientesModel cliente = new ClientesModel();
        ResultSet rs;
        
        try {
            sqlStmnt = this.con.prepareStatement("Select * from clientes where CodCliente = ?");
            sqlStmnt.setString(1, CodCliente);
            rs = sqlStmnt.executeQuery();
            
            if (rs.next()) {
                cliente.setIdCliente(rs.getInt("IdCliente"));
                cliente.setCodCliente(rs.getString("CodCliente"));
                cliente.setNombres(rs.getString("Nombres"));
                cliente.setApellidos(rs.getString("Apellidos"));
            } else {
                cliente.setIdCliente(-1);
            }
                                  
        } catch (SQLException ex){
            LogSms.write_DBException("Error al consultar 'cliente' mediante Id");
            cliente.setIdCliente(-2);
        }
        return cliente;
    }
    
    /**
     * Método para buscar clientes en base a un CodCliente ingresado o semiingresado
     * @param CodCliente    CodCliente con el que se compararán los resultados en la BD
     * @return 
     */
    public ArrayList<ClientesModel> readClienteLikeCodCliente (String CodCliente) {
        PreparedStatement sqlStmt;
        
        ArrayList<ClientesModel> clientes = new ArrayList();
        ClientesModel cliente;
        ResultSet rs;
        
        try {
            sqlStmt = this.con.prepareStatement("Select * from clientes "
                    + "where CodCliente LIKE '?%' order by CodCliente");
            sqlStmt.setString(1, CodCliente);
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
            LogSms.write_DBException("Error al consultar 'clientes' like CodCliente");
        }
                       
        return clientes;
    }
    
    /**
     * Método para crear clientes en la BD
     * @param CodCliente    Código de nuevo Cliente
     * @param Nombres       Nombres del cliente
     * @param Apellidos     Apellidos del cliente
     * @return          Bandera de éxito al crear cliente
     */
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
    
    /**
     * Método para actualizar información de cliente
     * @param IdCliente     Id de Cliente a actualizar
     * @param CodCliente    Nuevo Código de Cliente
     * @param Nombres       Nombres
     * @param Apellidos     Apellidos
     * @return 
     */
    public int updateClienteInfo (int IdCliente, String CodCliente, 
            String Nombres, String Apellidos) {        
        //Verificar en App si se modificaron los campos
        
        ClientesModel verfCliente = this.readClienteByCodCliente(CodCliente);
        if (verfCliente.getCodCliente().equals(CodCliente)
                && verfCliente.getIdCliente() != IdCliente) {            
            //Verificar id de cliente
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
            LogSms.write_DBException("Error al actualizar info 'cliente'");
            return 0;
        }
        
    }
    
    /**
     * Método para eliminar cliente cuando se confirme la acción
     * @param IdCl      Id de cliente enviado para confirmar la accion
     * @return
     */
    public int deleteClienteById (int IdCl) {
        PreparedStatement sqlStmt;
        
        try {
            sqlStmt = this.con.prepareStatement("Delete from clientes "
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

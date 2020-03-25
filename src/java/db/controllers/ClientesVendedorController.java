/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.controllers;

import db.models.ClientesVendedorModel;

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
public class ClientesVendedorController {
    private Connection con;
    private LogMessages LogSms;
    
    public ClientesVendedorController (Connection con) {
        this.con = con;
    }
    
    // ----------------------- Private Methods -------------------------
    
    private int checkClientIfVend(int IdCli) {
        PreparedStatement sqlStmt;
        ResultSet rs;
        try {
            sqlStmt = this.con.prepareStatement("Select IdCliVen from"
                + "clientesvendedor where Cliente = ?");
            sqlStmt.setInt(0, IdCli);
            rs = sqlStmt.executeQuery();
            
            if (rs.next())
                return -1;//Ya tiene un Vendedor Asociado. Retornado hasta el WS
            return 1;
            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al consultar si 'clientes' associado");
            return -2; //Error al consultar. Retornado hasta el WS
        }                
    }
    
    // --------------------- Public Methods For WS ---------------------
    
    public int assocClientToVend (int IdU, int IdCliente) {
        int chk = checkClientIfVend(IdCliente);
        if (chk > 0) {
            PreparedStatement sqlStmt;
            try {
                sqlStmt = this.con.prepareStatement("Insert into clientesvendedor"
                        + "(Vendedor, Cliente) values (?, ?)");
                sqlStmt.setInt(0, IdU);
                sqlStmt.setInt(1, IdCliente);
                sqlStmt.executeUpdate();
                return 1;
            } catch (SQLException e) {
                return -3;
            }                                
        } else
            return chk;
    }
        
}

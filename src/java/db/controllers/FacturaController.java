/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.controllers;

import db.models.FacturaModel;

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
public class FacturaController {
    private Connection con;
    private LogMessages LogSms;
    
    public FacturaController (Connection con) {
        this.con = con;
    }
    
    // ----------------------- Private Methods -------------------------
    
    // --------------------- Public Methods For WS ---------------------
    
    /**
     * 
     * @return 
     */
    public ArrayList<FacturaModel> readAllFacturas () {
        PreparedStatement sqlStmt;
        ArrayList<FacturaModel> facturas = new ArrayList();
        FacturaModel factura;
        ResultSet rs;
        
        try {
            sqlStmt = this.con.prepareStatement("Select * from factura"
                    + "order by IdFactura");
            rs = sqlStmt.executeQuery();
            
            while(rs.next()){
                factura = new FacturaModel();
                factura.setIdFactura(rs.getInt(("IdFactura")));
                factura.setFecha(rs.getString(("Fecha")));
                factura.setVendedor(rs.getInt(("Vendedor")));
                factura.setCliente(rs.getInt("Cliente"));
                factura.setTotal(rs.getInt(("Total")));
                facturas.add(factura);
           }                        
            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al consultar 'Facturas'");
        }
        return facturas;
    }
    
    /**
     * 
     * @param IdU
     * @return 
     */
    public ArrayList<FacturaModel> readFacturasByVendedor(int IdU) {
        PreparedStatement sqlStmt;
        
        ArrayList<FacturaModel> facturas = new ArrayList();
        FacturaModel factura;
        ResultSet rs;
        
        try {
            sqlStmt = this.con.prepareStatement("Select * from factura"
                    + "where Vendedor = ? order by IdFactura");
            sqlStmt.setInt(1, IdU);
            rs = sqlStmt.executeQuery();
            
            while(rs.next()){
                factura = new FacturaModel();
                factura.setIdFactura(rs.getInt(("IdFactura")));
                factura.setFecha(rs.getString(("Fecha")));
                factura.setVendedor(rs.getInt(("Vendedor")));
                factura.setCliente(rs.getInt("Cliente"));
                factura.setTotal(rs.getInt(("Total")));
                facturas.add(factura);
           }                        
            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al consultar 'Facturas'");
        }
        return facturas;
        
    }
    
    /**
     * 
     * @param Fecha
     * @return 
     */
    public ArrayList<FacturaModel> readFacturasByFecha(String Fecha) {
        PreparedStatement sqlStmt;
        
        ArrayList<FacturaModel> facturas = new ArrayList();
        FacturaModel factura;
        ResultSet rs;
        
        try {
            sqlStmt = this.con.prepareStatement("Select * from factura"
                    + "where Fecha = ? order by IdFactura");
            sqlStmt.setString(1, Fecha);
            rs = sqlStmt.executeQuery();
            
            while(rs.next()){
                factura = new FacturaModel();
                factura.setIdFactura(rs.getInt(("IdFactura")));
                factura.setFecha(rs.getString(("Fecha")));
                factura.setVendedor(rs.getInt(("Vendedor")));
                factura.setCliente(rs.getInt("Cliente"));
                factura.setTotal(rs.getInt(("Total")));
                facturas.add(factura);
           }                        
            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al consultar 'Facturas'");
        }
        return facturas;
        
    }
    
    /**
     * 
     * @param Fecha
     * @param Vendedor
     * @param Cliente
     * @param Total
     * @return 
     */
    public int createFactura (String Fecha, int Vendedor, int Cliente, int Total) {
        PreparedStatement sqlStmt;
        
        try {
            sqlStmt = this.con.prepareStatement("Insert into factura"
                    + "(Fecha, Vendedor, Cliente, Total) values"
                    + "(?, ?, ?, ?)");
            sqlStmt.setString(1, Fecha);
            sqlStmt.setInt(2, Vendedor);
            sqlStmt.setInt(3, Cliente);
            sqlStmt.setInt(4, Total);
            sqlStmt.executeUpdate();
            
            return 1;
            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al crear 'factura'");
            return 0;
        }
    }
    
    //Update info de factura
    
    /**
     * 
     * @param IdFactura
     * @param NewTotal
     * @return 
     */
    public int updateTotalFactura (int IdFactura, int NewTotal) {
        PreparedStatement sqlStmt;
        
        try {
            sqlStmt = this.con.prepareStatement("Update factura Set"
                    + "Total = ? where IdFactura = ?");
            sqlStmt.setInt(1, NewTotal);
            sqlStmt.setInt(2, IdFactura);
            
            if (sqlStmt.executeUpdate() > 0) 
                return 1;
            return -1;
            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al crear 'factura'");
            return 0;
        }
    }
    
    //Eliminar facturas?
    public int deleteFactura (int IdFactura) {
        PreparedStatement sqlStmt;
        
        try {
            sqlStmt = this.con.prepareStatement("Delete factura "
                    + "where IdFactura = ?");
            sqlStmt.setInt(1, IdFactura);
            
            if (sqlStmt.executeUpdate() > 0) 
                return 1;
            return -1;
            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al eliminar 'factura'");
            return 0;
        }
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.controllers;

import db.models.PedidoModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Emiliano
 */
public class PedidoController {
    private Connection con;
    private LogMessages LogSms;
    
    public PedidoController (Connection con) {
        this.con = con;
    }
    
    // ----------------------- Private Methods -------------------------
    
    // --------------------- Public Methods For WS ---------------------
    
    public ArrayList<PedidoModel> readAllPedidosByFactura (int IdFactura) {
        PreparedStatement sqlStmt;
        ArrayList <PedidoModel> PedidosFact = new ArrayList();
        PedidoModel pedido;
        ResultSet rs;
        
        try {
            sqlStmt = this.con.prepareStatement("Select * from pedido"
                    + "where Factura = ?");
            sqlStmt.setInt(1, IdFactura);
            rs = sqlStmt.executeQuery();
            
            while (rs.next()) {
                pedido = new PedidoModel();
                pedido.setIdPedido(rs.getInt("IdPedido"));
                pedido.setFactura(rs.getInt("Factura"));
                pedido.setProducto(rs.getInt("Producto"));
                pedido.setCantidad(rs.getInt("Cantidad"));
                pedido.setSubtotal(rs.getInt("Subtotal"));
                PedidosFact.add(pedido);
            }
            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al consultar 'Pedidos de una Factura'");
        }
        
        return PedidosFact;
    }
    
    public int readSubtotalAllPedidosByFactura (int IdFactura) {
        PreparedStatement sqlStmt;        
        ResultSet rs;
        
        int SubtotalFact;        
        
        try {
            
            sqlStmt = this.con.prepareStatement("Select * from pedido"
                    + "where Factura = ?");
            sqlStmt.setInt(1, IdFactura);
            rs = sqlStmt.executeQuery();     
            
            SubtotalFact = 0;            
            while (rs.next()) {
                SubtotalFact += rs.getInt("Subtotal");
            }
            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al consultar 'Pedidos de una Factura'");
            return -1;
        }
        
        return SubtotalFact;
    }
    
    public int addPedidoToFactura (int Factura, int Producto, int Cantidad, 
            int Subtotal) {
        PreparedStatement sqlStmt;
        
        try {
            sqlStmt = this.con.prepareStatement("Insert into Pedido"
                    + "(Factura, Producto, Cantidad, Subtotal) values"
                    + "(?, ?, ?, ?)");
            sqlStmt.setInt(1, Factura);
            sqlStmt.setInt(2, Producto);
            sqlStmt.setInt(3, Cantidad);
            sqlStmt.setInt(4, Subtotal);
            return 1;
            
        } catch (SQLException ex) {
            LogSms.write_DBException("Error al añadir 'pedido' a 'factura'");
            return 0;
        }
        
        //Actualizar total de factura en el método WS --> Llamar al metodo para 
        //actualizar total
    }
    
    //Vigiliar este método, ya que puede se un poco problemático
    public int updatePedidoOfFactura (int IdPedido, int Producto, int Cantidad, 
            int Subtotal /*int Factura ??? */) {
        
    }
    
    public int deletePedido_FromFactura (int IdPedido) {
        PreparedStatement sqlStmt;
        
        try {
            sqlStmt = this.con.prepareStatement("Delete pedido "
                    + "where IdPedido = ?");
            sqlStmt.setInt(1, IdPedido);
            
            if (sqlStmt.executeUpdate() > 0) 
                return 1;
            return -1;
            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al eliminar 'pedido'");
            return 0;
        }
    }
    
}

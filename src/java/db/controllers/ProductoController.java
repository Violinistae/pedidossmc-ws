/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.controllers;

import db.models.ProductoModel;

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
public class ProductoController {
    private Connection con;
    private LogMessages LogSms;
    
    public ProductoController (Connection con) {
        this.con = con;
    }
    
    // ----------------------- Private Methods -------------------------
    
    // --------------------- Public Methods For WS ---------------------
    
    /**
     * 
     * @return 
     */
    public ArrayList<ProductoModel> readAllProducto () {
        PreparedStatement sqlStmt;
        
        ArrayList<ProductoModel> productos = new ArrayList();
        ProductoModel producto;
        ResultSet rs;
        
        try {
            sqlStmt = this.con.prepareStatement("Select * from producto "
                    + "order by CodProd");
            
            rs = sqlStmt.executeQuery();
            
            while(rs.next()) {
                producto = new ProductoModel();
                producto.setIdProd(rs.getInt("IdProd"));
                producto.setCodProd(rs.getString("CodProd"));
                producto.setDescripcion(rs.getString("Descripcion"));
                producto.setPesoSaco(rs.getInt("PesoSaco"));               
                productos.add(producto);
            }
            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al consultar 'producto'");
        }
        
        return productos;
    }
    
    /**
     * 
     * @param CodProd
     * @return 
     */
    public ProductoModel readProductoByCodProd (String CodProd) {
        PreparedStatement sqlStmt;
        ProductoModel producto = new ProductoModel();
        ResultSet rs;
        
        try {
            sqlStmt = this.con.prepareStatement("Select¨* from producto where"
                    + "CodProd = ?");
            sqlStmt.setString(1, CodProd);
            rs = sqlStmt.executeQuery();
            
            producto.setIdProd(rs.getInt("IdProd"));
            producto.setCodProd(rs.getString("CodProd"));
            producto.setDescripcion(rs.getString("Descripcion"));
            producto.setPesoSaco(rs.getInt("PesoSaco"));
                               
        } catch (SQLException ex) {
            LogSms.write_DBException("Error al consultar 'producto' mediante Id");
        }
        return producto;
    }
    
    /**
     * 
     * @param CodProd
     * @return 
     */
    public ArrayList<ProductoModel> readProductoLikeCodProd (String CodProd) {
        PreparedStatement sqlStmt;
        
        ArrayList<ProductoModel> productos = new ArrayList();
        ProductoModel producto;
        ResultSet rs;
        
        try {
            sqlStmt = this.con.prepareStatement("Select * from producto"
                    + "where CodProd LIKE '?%' order by CodProd");
            sqlStmt.setString(1, CodProd);
            rs = sqlStmt.executeQuery();
            
            while (rs.next()) {
                producto = new ProductoModel();
                producto.setIdProd(rs.getInt("IdProd"));
                producto.setCodProd(rs.getString("CodProd"));
                producto.setDescripcion(rs.getString("Descripcion"));
                producto.setPesoSaco(rs.getInt("PesoSaco"));        
                productos.add(producto);
            }            
            
        } catch (SQLException e) {
            LogSms.write_DBException("Error al consultar 'clientes' like CodCliente");            
        }
        return productos;
    }
    
    /**
     * 
     * @param CodProd
     * @param Descripcion
     * @param PesoSaco
     * @return 
     */
    public int createProducto (String CodProd, String Descripcion, int PesoSaco) {
        
        ProductoModel prodVerf = this.readProductoByCodProd(CodProd);
        if (prodVerf.getCodProd().equals(CodProd)) {
            return -1;
        }
        
        PreparedStatement sqlStmt;
        try {                                                        
            
            sqlStmt = this.con.prepareStatement("Insert into producto "
                    + "(CodProd, Descripcion, PesoSaco) values (?, ?, ?)");
            sqlStmt.setString(1, CodProd);
            sqlStmt.setString(2, Descripcion);
            sqlStmt.setInt(3, PesoSaco);
            sqlStmt.executeUpdate();
            
            return 1;       //Éxito al crear nuevo producto
                        
        } catch (SQLException e) {     //No se pudo actualizar info de cliente
            LogSms.write_DBException("Error al actualizar info 'producto'");
            return 0;
        }
        
    }
    
    /**
     * 
     * @param IdProd
     * @param CodProd
     * @param Descripcion
     * @param PesoSaco
     * @return 
     */
    public int updateProducto (int IdProd, String CodProd, String Descripcion,
            int PesoSaco) {
        
        //Verificar que no exista un producto con el mismo CodigoProducto y que
        //no sea el mismo
        ProductoModel prodVerf = this.readProductoByCodProd(CodProd);
        if (prodVerf.getCodProd().equals(CodProd) &&
                prodVerf.getIdProd() != IdProd) {
            return -1;
        }
                
        PreparedStatement sqlStmt;
        try {
            sqlStmt = this.con.prepareStatement(
                    "Update producto Set"
                            + "CodProd = ?,"
                            + "Descripcion = ?,"
                            + "PesoSaco = ? where IdProd = ?"
            );
            sqlStmt.setString(1, CodProd);
            sqlStmt.setString(2, Descripcion);
            sqlStmt.setInt(3, PesoSaco);
            sqlStmt.setInt(4, IdProd);
            
            if (sqlStmt.executeUpdate() > 0)
                return 1;       //Éxito
            return -2;          //No se pudo actualizar (no se encontró IdProd)
            
        } catch (Exception e) {     //Error al actualizar
            LogSms.write_DBException("Error al actualizar info 'producto'");
            return 0;
        }
        
    }
    
    /**
     * 
     * @param IdProd
     * @return 
     */
    public int deleteProductoById (int IdProd) {
        PreparedStatement sqlStmt;
        
        try {
            sqlStmt = this.con.prepareStatement("Delete from Producto"
                    + "where IdProd = ?");
            sqlStmt.setInt(1, IdProd);
            if (sqlStmt.executeUpdate() > 0) {
                return 1;
            }
            return -1;
        } catch (Exception e) {
            LogSms.write_DBException("Error al eliminar 'cliente' mediante Id");
            return 0;
        }
    }
    
}

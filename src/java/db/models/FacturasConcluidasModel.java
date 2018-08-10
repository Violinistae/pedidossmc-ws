/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.models;

/**
 *
 * @author Emiliano
 */
public class FacturasConcluidasModel {
    private int IdFC;
    private int Factura;
    private int Facturador;
    private String Fecha;

    public int getIdFC() {
        return IdFC;
    }

    public void setIdFC(int IdFC) {
        this.IdFC = IdFC;
    }

    public int getFactura() {
        return Factura;
    }

    public void setFactura(int Factura) {
        this.Factura = Factura;
    }

    public int getFacturador() {
        return Facturador;
    }

    public void setFacturador(int Facturador) {
        this.Facturador = Facturador;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }
    
}

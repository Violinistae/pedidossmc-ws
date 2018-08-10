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
public class ClientesVendedorModel {
    private int IdCliVen;
    private int Vendedor;
    private int Cliente;

    public int getIdCliVen() {
        return IdCliVen;
    }

    public void setIdCliVen(int IdCliVen) {
        this.IdCliVen = IdCliVen;
    }

    public int getVendedor() {
        return Vendedor;
    }

    public void setVendedor(int Vendedor) {
        this.Vendedor = Vendedor;
    }

    public int getCliente() {
        return Cliente;
    }

    public void setCliente(int Cliente) {
        this.Cliente = Cliente;
    }
    
}

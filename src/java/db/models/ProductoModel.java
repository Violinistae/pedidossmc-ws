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
public class ProductoModel {
    private int IdProd;
    private String CodProd;
    private String Descripcion;
    private int PesoSaco;

    public int getIdProd() {
        return IdProd;
    }

    public void setIdProd(int IdProd) {
        this.IdProd = IdProd;
    }

    public String getCodProd() {
        return CodProd;
    }

    public void setCodProd(String CodProd) {
        this.CodProd = CodProd;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public int getPesoSaco() {
        return PesoSaco;
    }

    public void setPesoSaco(int PesoSaco) {
        this.PesoSaco = PesoSaco;
    }
    
     
}

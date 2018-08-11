/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WS;

import DB.*;
import db.DBConnection;
import db.controllers.UsuarioController;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Emiliano
 */
@WebService(serviceName = "PedidosSMC")
public class PedidosSMC {

    private final DBConnection intermediate = new DBConnection();
    private final UsuarioController userCtrlr = new UsuarioController(intermediate.getCon());
    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    
    // ------ Web Methods only For User Creation, Login, Delete Actions ------
    
    @WebMethod(operationName = "userLogin")
    public int userLogin (@WebParam(name = "Username")String Username, 
            @WebParam(name = "Password")String Password) {       
        return userCtrlr.verifyLogin(Username, Password);
        // En Android limitar a que solo lo pueda usar el Vendedor
        // En Desktop limitar a solo ingreso a Gerente y Facturador
    }
    
    @WebMethod(operationName = "createUser")
    public int createUser (@WebParam(name = "Username")String Username, 
            @WebParam(name = "Password")String Password,
            @WebParam(name = "Nombres")String Nombres,
            @WebParam(name = "Apellidos")String Apellidos,            
            @WebParam(name = "TipoUsuario")int TipoUsuario) {
        
        String Hash = "123";
        return userCtrlr.createUsuario(Username,
                Password, Nombres, Apellidos, Hash, TipoUsuario);
    }
    
}

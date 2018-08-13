/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WS;

import DB.*;
import db.DBConnection;
import db.controllers.*;
import db.models.*;
import java.util.ArrayList;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Emiliano
 */
@WebService(serviceName = "PedidosSMC")
public class PedidosSMC {

    // ------------ DB Connection ------------
    private final DBConnection intermediate = new DBConnection();
    
    // ------------- Controllers -------------
    private final UsuarioController userCtrlr = new UsuarioController(intermediate.getCon());
    private final ClientesController clientCtrlr = new ClientesController(intermediate.getCon());
       
    // - Web Methods only For 'usuario' Creation, Login, Update & Delete Actions -
    
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
    
    @WebMethod(operationName = "readAllUsers")
    public ArrayList<UsuarioModel> readAllUsers () {
        return userCtrlr.readAllUsuario();
        //Realizar conteo de objetos en App para mostrar mensaje sobre 
        //que no existen usuarios
    }
    
    @WebMethod(operationName = "readUserById")
    public UsuarioModel readUserById (@WebParam(name = "IdU")int IdU) {
        return userCtrlr.readUsuarioById(IdU);
    }    
    
    @WebMethod(operationName = "searchUserLikeUsername")
    public ArrayList<UsuarioModel> searchUserLikeUsername (
            @WebParam(name = "Username")String Username) {
        return userCtrlr.readUsuarioLikeUsername(Username);
    }    
    
    @WebMethod(operationName = "updateUserInfo")
    public int updateUserInfo (@WebParam(name = "OldUsername")String OldUsername,
            @WebParam(name = "Username")String Username,
            @WebParam(name = "Nombres")String Nombres,
            @WebParam(name = "Apellidos")String Apellidos) {
        
        return userCtrlr.updateUsuarioInfo(OldUsername, 
                Username, Nombres, Apellidos);
    }
    
    @WebMethod(operationName = "updateUserPswd")
    public int updateUserPswd (@WebParam(name = "Username")String Username,            
            @WebParam(name = "OldPwsd")String OldPwsd,
            @WebParam(name = "NewPswd")String NewPswd) {
        
        return userCtrlr.changeUserPassword(Username, OldPwsd, NewPswd);
    }
    
    //Solo se determina si se desea eliminar al usuario, en seguida se pude
    //la confirmación y al final se realiza la consulta al WS para ejecutar
    //este método
    @WebMethod(operationName = "deleteUser")
    public int deleteUser (@WebParam(name = "IdU")int IdU) {
        
        UsuarioModel verUser = userCtrlr.readUsuarioById(IdU);
        
        if (verUser.getIdUsuario() > 0) {
            return userCtrlr.deleteUsuarioById(verUser.getIdUsuario());
            //-1 --> No se encontró el usuario
            //0 --> Error con BD
            //1 --> Éxito al eliminar
        } else 
            return verUser.getIdUsuario();  
            //Cuando hay error que no existe o no se pudo realizar la consulta
        
    }    
    
    // ---------------- Web Methods only For 'cliente' --------------------
    
    @WebMethod(operationName = "createClient")
    public int createClient (@WebParam(name = "CodCliente")String CodCliente,            
            @WebParam(name = "Nombres")String Nombres,
            @WebParam(name = "Apellidos")String Apellidos) {
        return clientCtrlr.createCliente(CodCliente, Nombres, Apellidos);
    }
    
    @WebMethod(operationName = "readAllClient")
    public ArrayList<ClientesModel> readAllClient () {
        return clientCtrlr.readAllClientes();
    }
    
    @WebMethod(operationName = "readClient")
    public ClientesModel readClientByCodCli (@WebParam(name="CodCliente")String CodCliente) {
        return clientCtrlr.readClienteByCodCliente(CodCliente);
    }
    
    @WebMethod(operationName = "updateClientInfo")
    public int updateClientInfo (@WebParam(name = "IdCliente")int IdCliente,
            @WebParam(name = "CodCliente")String CodCliente,
            @WebParam(name = "Nombres")String Nombres,
            @WebParam(name = "Apellidos")String Apellidos) {
        return clientCtrlr.updateClienteInfo(IdCliente, 
                CodCliente, Nombres, Apellidos);
    }
    
    @WebMethod(operationName = "updateClient")
    public int deleteClient (@WebParam (name = "IdCliente")int IdCl) {
        return clientCtrlr.deleteClienteById(IdCl);
    }
    
    // ---------------- Web Methods only For 'cliente' --------------------
    
    
            
    //*******************************************************************
            
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "helloworld")
    public String hello() {
        return "Hola Mundo !";
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.Proveedor;
import co.com.ppi.modelo.ProveedorDAO;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author SANTI
 */
@Stateless
@Path("/proveedor")
public class ProveedorREST {
    
    /**
     *
     * @param seleccionar
     * @param campos
     * @param valores
     * @param orden
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultarAll")
    public ArrayList<Proveedor> listar(@QueryParam("seleccionar")String seleccionar,
                                       @QueryParam("campos")String campos,
                                       @QueryParam("valores")String valores,
                                       @QueryParam("orden")String orden) {
        ProveedorDAO dao = new ProveedorDAO();
        return dao.consultarProveedores(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param idProveedor
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<Proveedor> consultarProveedor(@QueryParam("idProveedor")int idProveedor) {
        ProveedorDAO dao = new ProveedorDAO();
        return dao.consultarProveedor(idProveedor);
    }
    
    /**
     *
     * @param idProveedor
     * @param nombreEmpresa
     * @param nit
     * @param direccion
     * @param telefono
     * @param nombreContacto
     * @param email
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarProveedor(/*@QueryParam("idProveedor") int idProveedor, */
                                    @QueryParam("nombreEmpresa")String nombreEmpresa, 
                                    @QueryParam("nit")String nit, 
                                    @QueryParam("direccion")String direccion,
                                    @QueryParam("telefono")String telefono,
                                    @QueryParam("nombreContacto")String nombreContacto,
                                    @QueryParam("email")String email){
        ProveedorDAO dao = new ProveedorDAO();
        return dao.insertarProveedor(/*idProveedor,*/nombreEmpresa,nit,direccion,
                                     telefono,nombreContacto,email);
    }
    
    /**
     *
     * @param idProveedor
     * @param nombreEmpresa
     * @param nit
     * @param direccion
     * @param telefono
     * @param nombreContacto
     * @param email
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarProveedor(@QueryParam("idProveedor") int idProveedor, 
                                      @QueryParam("nombreEmpresa")String nombreEmpresa, 
                                      @QueryParam("nit")String nit, 
                                      @QueryParam("direccion")String direccion,
                                      @QueryParam("telefono")String telefono,
                                      @QueryParam("nombreContacto")String nombreContacto,
                                      @QueryParam("email")String email){
        ProveedorDAO dao = new ProveedorDAO();
        return dao.actualizarProveedor(idProveedor,nombreEmpresa,nit,direccion,
                                       telefono,nombreContacto,email);
        
    }
    
    /**
     *
     * @param idProveedor
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
    public String eliminarProveedor(@QueryParam("idProveedor") int idProveedor){
        ProveedorDAO dao = new ProveedorDAO();
        return dao.eliminarProveedor(idProveedor);
    }
}

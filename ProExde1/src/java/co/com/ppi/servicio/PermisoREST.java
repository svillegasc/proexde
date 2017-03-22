/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.Permiso;
import co.com.ppi.modelo.PermisoDAO;
import co.com.ppi.util.Validador;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.HeaderParam;

/**
 *
 * @author SANTI
 */
@Stateless
@Path("/permiso")
public class PermisoREST {
    
    Validador validador = new Validador();
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
    public ArrayList<Permiso> listar(@HeaderParam("seleccionar")String seleccionar,
                                     @HeaderParam("campos")String campos,
                                     @HeaderParam("valores")String valores,
                                     @HeaderParam("orden")String orden) {
        PermisoDAO dao = new PermisoDAO();
        return dao.consultarPermisos(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param idPermiso
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<Permiso> consultarPermiso(@HeaderParam("idPermiso")int idPermiso) {
        PermisoDAO dao = new PermisoDAO();
        return dao.consultarPermiso(idPermiso);
    }
    
    /**
     *
     * @param token
     * @param descripcion
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarPermiso(/*@HeaderParam("idPermiso") int idPermiso,*/
                                  @HeaderParam("token")String token,
                                  @HeaderParam("descripcion")String descripcion){
        PermisoDAO dao = new PermisoDAO();
        if ( validador.validar_token(token) ){ 
            return dao.insertarPermiso(/*idPermiso,*/descripcion);
        }else{
            return validador.getMensajeToken();
        }
        
    }
    
    /**
     *
     * @param token
     * @param idPermiso
     * @param descripcion
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarPermiso(@HeaderParam("token")String token,
                                    @HeaderParam("idPermiso") int idPermiso,
                                    @HeaderParam("descripcion") String descripcion){
        PermisoDAO dao = new PermisoDAO();
        if ( validador.validar_token(token) ){ 
            return dao.actualizarPermiso(idPermiso,descripcion);
        }else{
            return validador.getMensajeToken();
        }
        
    }
    
    /**
     *
     * @param token
     * @param idPermiso
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
    public String eliminarPermiso(@HeaderParam("token")String token,
                                  @HeaderParam("idPermiso") int idPermiso){
        PermisoDAO dao = new PermisoDAO();
        if ( validador.validar_token(token) ){ 
            return dao.eliminarPermiso(idPermiso);
        }else{
            return validador.getMensajeToken();
        }
        
    }
}

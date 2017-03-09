/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.DetallePermiso;
import co.com.ppi.modelo.DetallePermisoDAO;
import co.com.ppi.util.Validador;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.HeaderParam;

/**
 *
 * @author SANTI
 */
@Stateless
@Path("/detallePermiso")
public class DetallePermisoREST {
    
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
    public ArrayList<DetallePermiso> listar(@HeaderParam("seleccionar")String seleccionar,
                                            @HeaderParam("campos")String campos,
                                            @HeaderParam("valores")String valores,
                                            @HeaderParam("orden")String orden) {
        DetallePermisoDAO dao = new DetallePermisoDAO();
        return dao.consultarDetallePermisos(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param idPermiso
     * @param idPerfil
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<DetallePermiso> consultarDetallePermiso(@HeaderParam("idPermiso")int idPermiso,
                                                             @HeaderParam("idPerfil")int idPerfil) {
        DetallePermisoDAO dao = new DetallePermisoDAO();
        return dao.consultarDetallePermiso(idPermiso,idPerfil);
    }
    
    /**
     *
     * @param token
     * @param idPermiso
     * @param idPerfil
     * @return
     * @throws java.lang.Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarDetallePermiso(@HeaderParam("token")String token,
                                         @HeaderParam("idPermiso") int idPermiso, 
                                         @HeaderParam("idPerfil") int idPerfil) throws Exception{
        DetallePermisoDAO dao = new DetallePermisoDAO();
        if ( validador.validar_token(token) ){ 
            return dao.insertarDetallePermiso(idPermiso,idPerfil);
        }else{
            return validador.getMensajeToken();
        }
        
    }
    
    /**
     *
     * @param token
     * @param idPermiso
     * @param idPerfil
     * @return
     * @throws java.lang.Exception
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
    public String eliminarDetallePermiso(@HeaderParam("token")String token,
                                         @HeaderParam("idPermiso") int idPermiso,
                                         @HeaderParam("idPerfil") int idPerfil) throws Exception{
        DetallePermisoDAO dao = new DetallePermisoDAO();
        if ( validador.validar_token(token) ){ 
            return dao.eliminarDetallePermiso(idPermiso,idPerfil);
        }else{
            return validador.getMensajeToken();
        }
        
    }
}

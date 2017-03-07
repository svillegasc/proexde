/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.Permiso;
import co.com.ppi.modelo.PermisoDAO;
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
@Path("/permiso")
public class PermisoREST {
    
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
    public ArrayList<Permiso> listar(@QueryParam("seleccionar")String seleccionar,
                                     @QueryParam("campos")String campos,
                                     @QueryParam("valores")String valores,
                                     @QueryParam("orden")String orden) {
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
    public ArrayList<Permiso> consultarPermiso(@QueryParam("idPermiso")int idPermiso) {
        PermisoDAO dao = new PermisoDAO();
        return dao.consultarPermiso(idPermiso);
    }
    
    /**
     *
     * @param idPermiso
     * @param descripcion
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarPermiso(/*@QueryParam("idPermiso") int idPermiso,*/ 
                                  @QueryParam("descripcion")String descripcion){
        PermisoDAO dao = new PermisoDAO();
        return dao.insertarPermiso(/*idPermiso,*/descripcion);
    }
    
    /**
     *
     * @param idPermiso
     * @param descripcion
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarPermiso(@QueryParam("idPermiso") int idPermiso,
                                    @QueryParam("descripcion") String descripcion){
        PermisoDAO dao = new PermisoDAO();
        return dao.actualizarPermiso(idPermiso,descripcion);
    }
    
    /**
     *
     * @param idPermiso
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
    public String eliminarPermiso(@QueryParam("idPermiso") int idPermiso){
        PermisoDAO dao = new PermisoDAO();
        return dao.eliminarPermiso(idPermiso);
    }
}
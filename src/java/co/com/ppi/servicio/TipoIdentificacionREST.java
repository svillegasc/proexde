/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.TipoIdentificacion;
import co.com.ppi.modelo.TipoIdentificacionDAO;
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
@Path("/tipoIdentificacion")
public class TipoIdentificacionREST {
    
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
    public ArrayList<TipoIdentificacion> listar(@QueryParam("seleccionar")String seleccionar,
                                                @QueryParam("campos")String campos,
                                                @QueryParam("valores")String valores,
                                                @QueryParam("orden")String orden) {
        TipoIdentificacionDAO dao = new TipoIdentificacionDAO();
        return dao.consultarTipoIdentificaciones(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param tipoIdentificacion
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<TipoIdentificacion> consultarTipoIdentificacion(@QueryParam("tipoIdentificacion")int tipoIdentificacion) {
        TipoIdentificacionDAO dao = new TipoIdentificacionDAO();
        return dao.consultarTipoIdentificacion(tipoIdentificacion);
    }

    /**
     *
     * @param tipoIdentificacion
     * @param descripcion
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarTipoIdentificacion(/*@QueryParam("tipoIdentificacion") String tipoIdentificacion,*/
                                             @QueryParam("descripcion")String descripcion){
        TipoIdentificacionDAO dao = new TipoIdentificacionDAO();
        return dao.insertarTipoIdentificacion(/*tipoIdentificacion,*/descripcion);
    }
    
    /**
     *
     * @param tipoIdentificacion
     * @param descripcion
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarTipoIdentificacion(@QueryParam("tipoIdentificacion") int tipoIdentificacion,
                                               @QueryParam("descripcion") String descripcion){
        TipoIdentificacionDAO dao = new TipoIdentificacionDAO();
        return dao.actualizarTipoIdentificacion(tipoIdentificacion,descripcion);
    }
    
    /**
     *
     * @param tipoIdentificacion
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
    public String eliminarTipoIdentificacion(@QueryParam("tipoIdentificacion") int tipoIdentificacion){
        TipoIdentificacionDAO dao = new TipoIdentificacionDAO();
        return dao.eliminarTipoIdentificacion(tipoIdentificacion);
    }
}

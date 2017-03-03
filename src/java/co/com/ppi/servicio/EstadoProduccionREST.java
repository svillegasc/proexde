/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.EstadoProduccion;
import co.com.ppi.modelo.EstadoProduccionDAO;
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
@Path("/estadoProduccion")
public class EstadoProduccionREST {
    
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
    public ArrayList<EstadoProduccion> listar(@QueryParam("seleccionar")String seleccionar,
                                              @QueryParam("campos")String campos,
                                              @QueryParam("valores")String valores,
                                              @QueryParam("orden")String orden) {
        EstadoProduccionDAO dao = new EstadoProduccionDAO();
        return dao.consultarEstadoProducciones(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param estadoProduccion
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<EstadoProduccion> consultarEstadoProduccion(@QueryParam("estadoProduccion")int estadoProduccion) {
        EstadoProduccionDAO dao = new EstadoProduccionDAO();
        return dao.consultarEstadoProduccion(estadoProduccion);
    }
    
    /**
     *
     * @param descripcion
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarEstadoProduccion(/*@QueryParam("estadoProduccion") int estadoProduccion,*/
                                           @QueryParam("descripcion")String descripcion){
        EstadoProduccionDAO dao = new EstadoProduccionDAO();
        return dao.insertarEstadoProduccion(/*estadoProduccion,*/descripcion);
    }
    
    /**
     *
     * @param estadoProduccion
     * @param descripcion
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarEstadoProduccion(@QueryParam("estadoProduccion") int estadoProduccion,
                                             @QueryParam("descripcion") String descripcion){
        EstadoProduccionDAO dao = new EstadoProduccionDAO();
        return dao.actualizarEstadoProduccion(estadoProduccion,descripcion);
    }
    
    /**
     *
     * @param estadoProduccion
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
     public String eliminarEstadoProduccion(@QueryParam("estadoProduccion") int estadoProduccion){
        EstadoProduccionDAO dao = new EstadoProduccionDAO();
        return dao.eliminarEstadoProduccion(estadoProduccion);
    }
}

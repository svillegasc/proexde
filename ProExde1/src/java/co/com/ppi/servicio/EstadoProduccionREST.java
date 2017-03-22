/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.EstadoProduccion;
import co.com.ppi.modelo.EstadoProduccionDAO;
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
@Path("/estadoProduccion")
public class EstadoProduccionREST {
    
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
    public ArrayList<EstadoProduccion> listar(@HeaderParam("seleccionar")String seleccionar,
                                              @HeaderParam("campos")String campos,
                                              @HeaderParam("valores")String valores,
                                              @HeaderParam("orden")String orden) {
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
    public ArrayList<EstadoProduccion> consultarEstadoProduccion(@HeaderParam("estadoProduccion")int estadoProduccion) {
        EstadoProduccionDAO dao = new EstadoProduccionDAO();
        return dao.consultarEstadoProduccion(estadoProduccion);
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
    public String insertarEstadoProduccion(/*@HeaderParam("estadoProduccion") int estadoProduccion,*/
                                           @HeaderParam("token")String token,
                                           @HeaderParam("descripcion")String descripcion){
        EstadoProduccionDAO dao = new EstadoProduccionDAO();
        if ( validador.validar_token(token) ){ 
            return dao.insertarEstadoProduccion(/*estadoProduccion,*/descripcion);
        }else{
            return validador.getMensajeToken();
        }
        
    }
    
    /**
     *
     * @param token
     * @param estadoProduccion
     * @param descripcion
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarEstadoProduccion(@HeaderParam("token")String token,
                                             @HeaderParam("estadoProduccion") int estadoProduccion,
                                             @HeaderParam("descripcion") String descripcion){
        EstadoProduccionDAO dao = new EstadoProduccionDAO();
        if ( validador.validar_token(token) ){ 
            return dao.actualizarEstadoProduccion(estadoProduccion,descripcion);
        }else{
            return validador.getMensajeToken();
        }
        
    }
    
    /**
     *
     * @param token
     * @param estadoProduccion
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
     public String eliminarEstadoProduccion(@HeaderParam("token")String token,
                                            @HeaderParam("estadoProduccion") int estadoProduccion){
        EstadoProduccionDAO dao = new EstadoProduccionDAO();
        if ( validador.validar_token(token) ){ 
            return dao.eliminarEstadoProduccion(estadoProduccion);
        }else{
            return validador.getMensajeToken();
        }
        
    }
}

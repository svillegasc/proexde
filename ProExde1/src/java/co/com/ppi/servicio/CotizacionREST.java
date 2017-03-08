/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.Cotizacion;
import co.com.ppi.modelo.CotizacionDAO;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
@Path("/cotizacion")
public class CotizacionREST {
    
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
    public ArrayList<Cotizacion> listar(@HeaderParam("seleccionar")String seleccionar,
                                        @HeaderParam("campos")String campos,
                                        @HeaderParam("valores")String valores,
                                        @HeaderParam("orden")String orden) {
        CotizacionDAO dao = new CotizacionDAO();
        return dao.consultarCotizaciones(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param idCotizacion
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<Cotizacion> consultarCotizacion(@HeaderParam("idCotizacion")int idCotizacion) {
        CotizacionDAO dao = new CotizacionDAO();
        return dao.consultarCotizacion(idCotizacion);
    }
    
    /**
     *
     * @param idCotizacion
     * @param idUsuario
     * @param fechaCreacion
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarCotizacion(/*@HeaderParam("idCotizacion") int idCotizacion, */
                                     @HeaderParam("idUsuario")int idUsuario,
                                     @HeaderParam("fechaCreacion")String fechaCreacion){
        CotizacionDAO dao = new CotizacionDAO();
        return dao.insertarCotizacion(/*idCotizacion,*/idUsuario,fechaCreacion);
    }
    
    /**
     *
     * @param idCotizacion
     * @param idUsuario
     * @param fechaCreacion
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarCotizacion(@HeaderParam("idCotizacion") int idCotizacion,
                                       @HeaderParam("idUsuario")int idUsuario,
                                       @HeaderParam("fechaCreacion")String fechaCreacion){
        CotizacionDAO dao = new CotizacionDAO();
        return dao.actualizarCotizacion(idCotizacion,idUsuario,fechaCreacion);
    }
    
    /**
     *
     * @param idCotizacion
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
    public String eliminarCotizacion(@HeaderParam("idCotizacion") int idCotizacion){
        CotizacionDAO dao = new CotizacionDAO();
        return dao.eliminarCotizacion(idCotizacion);
    }
}

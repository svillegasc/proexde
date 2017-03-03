/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.DetalleCotizacionCliente;
import co.com.ppi.modelo.DetalleCotizacionClienteDAO;
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
@Path("/detalleCotizacionCliente")
public class DetalleCotizacionClienteREST {
    
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
    public ArrayList<DetalleCotizacionCliente> listar(@QueryParam("seleccionar")String seleccionar,
                                                      @QueryParam("campos")String campos,
                                                      @QueryParam("valores")String valores,
                                                      @QueryParam("orden")String orden) {
        DetalleCotizacionClienteDAO dao = new DetalleCotizacionClienteDAO();
        return dao.consultarDetalleCotizacionClientes(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param idCotizacion
     * @param idProducto
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<DetalleCotizacionCliente> consultarDetalleCotizacionCliente(@QueryParam("idCotizacion")int idCotizacion,
                                                                                 @QueryParam("idProducto")int idProducto) {
        DetalleCotizacionClienteDAO dao = new DetalleCotizacionClienteDAO();
        return dao.consultarDetalleCotizacionCliente(idCotizacion,idProducto);
    }
    
    /**
     *
     * @param idCotizacion
     * @param idProducto
     * @param precio
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarDetalleCotizacionCliente(@QueryParam("idCotizacion") int idCotizacion, 
                                                   @QueryParam("idProducto") int idProducto,
                                                   @QueryParam("precio") int precio){
        DetalleCotizacionClienteDAO dao = new DetalleCotizacionClienteDAO();
        return dao.insertarDetalleCotizacionCliente(idCotizacion,idProducto,precio);
    }
    
    /**
     *
     * @param idCotizacion
     * @param idProducto
     * @param precio
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarDetalleCotizacionCliente(@QueryParam("idCotizacion") int idCotizacion, 
                                                     @QueryParam("idProducto") int idProducto,
                                                     @QueryParam("precio") int precio){
        DetalleCotizacionClienteDAO dao = new DetalleCotizacionClienteDAO();
        return dao.actualizarDetalleCotizacionCliente(idCotizacion,idProducto,precio);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.DetalleCotizacionCliente;
import co.com.ppi.modelo.DetalleCotizacionClienteDAO;
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
@Path("/detalleCotizacionCliente")
public class DetalleCotizacionClienteREST {
    
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
    public ArrayList<DetalleCotizacionCliente> listar(@HeaderParam("seleccionar")String seleccionar,
                                                      @HeaderParam("campos")String campos,
                                                      @HeaderParam("valores")String valores,
                                                      @HeaderParam("orden")String orden) {
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
    public ArrayList<DetalleCotizacionCliente> consultarDetalleCotizacionCliente(@HeaderParam("idCotizacion")int idCotizacion,
                                                                                 @HeaderParam("idProducto")int idProducto) {
        DetalleCotizacionClienteDAO dao = new DetalleCotizacionClienteDAO();
        return dao.consultarDetalleCotizacionCliente(idCotizacion,idProducto);
    }
    
    /**
     *
     * @param token
     * @param idCotizacion
     * @param idProducto
     * @param precio
     * @return
     * @throws java.lang.Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarDetalleCotizacionCliente(@HeaderParam("token")String token,
                                                   @HeaderParam("idCotizacion") int idCotizacion, 
                                                   @HeaderParam("idProducto") int idProducto,
                                                   @HeaderParam("precio") int precio){
        DetalleCotizacionClienteDAO dao = new DetalleCotizacionClienteDAO();
        if ( validador.validar_token(token) ){ 
            return dao.insertarDetalleCotizacionCliente(idCotizacion,idProducto,precio);
        }else{
            return validador.getMensajeToken();
        }
        
    }
    
    /**
     *
     * @param token
     * @param idCotizacion
     * @param idProducto
     * @param precio
     * @return
     * @throws java.lang.Exception
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarDetalleCotizacionCliente(@HeaderParam("token")String token,
                                                     @HeaderParam("idCotizacion") int idCotizacion, 
                                                     @HeaderParam("idProducto") int idProducto,
                                                     @HeaderParam("precio") int precio){
        DetalleCotizacionClienteDAO dao = new DetalleCotizacionClienteDAO();
        if ( validador.validar_token(token) ){ 
            return dao.actualizarDetalleCotizacionCliente(idCotizacion,idProducto,precio);
        }else{
            return validador.getMensajeToken();
        }
        
    }
}

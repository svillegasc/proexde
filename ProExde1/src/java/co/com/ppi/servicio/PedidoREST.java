/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.Pedido;
import co.com.ppi.modelo.PedidoDAO;
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
@Path("/pedido")
public class PedidoREST {
    
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
    public ArrayList<Pedido> listar(@HeaderParam("seleccionar")String seleccionar,
                                    @HeaderParam("campos")String campos,
                                    @HeaderParam("valores")String valores,
                                    @HeaderParam("orden")String orden) {
        PedidoDAO dao = new PedidoDAO();
        return dao.consultarPedidos(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param idPedido
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<Pedido> consultarPedido(@HeaderParam("idPedido")int idPedido) {
        PedidoDAO dao = new PedidoDAO();
        return dao.consultarPedido(idPedido);
    }
    
    /**
     *
     * @param token
     * @param idCotizacion
     * @param fechaPedido
     * @param fechaInicio
     * @param fechaTerminacion
     * @param estadoProduccion
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarPedido(/*@HeaderParam("idPedido") int idPedido, */
                                 @HeaderParam("token")String token,
                                 @HeaderParam("idCotizacion")int idCotizacion, 
                                 @HeaderParam("fechaPedido")String fechaPedido,
                                 @HeaderParam("fechaInicio")String fechaInicio,
                                 @HeaderParam("fechaTerminacion")String fechaTerminacion,
                                 @HeaderParam("estadoProduccion")int estadoProduccion){
        PedidoDAO dao = new PedidoDAO();
        if ( validador.validar_token(token) ){ 
            return dao.insertarPedido(/*idPedido,*/idCotizacion,fechaPedido,
                                  fechaInicio,fechaTerminacion,estadoProduccion);
        }else{
            return validador.getMensajeToken();
        }
        
    }
    
    /**
     *
     * @param token
     * @param idPedido
     * @param idCotizacion
     * @param fechaPedido
     * @param cantidadProducida
     * @param fechaInicio
     * @param fechaTerminacion
     * @param estadoProduccion
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarPedido(@HeaderParam("token")String token,
                                   @HeaderParam("idPedido") int idPedido, 
                                   @HeaderParam("idCotizacion")int idCotizacion, 
                                   @HeaderParam("fechaPedido")String fechaPedido, 
                                   @HeaderParam("cantidadProducida")int cantidadProducida,
                                   @HeaderParam("fechaInicio")String fechaInicio,
                                   @HeaderParam("fechaTerminacion")String fechaTerminacion,
                                   @HeaderParam("estadoProduccion")int estadoProduccion){
        PedidoDAO dao = new PedidoDAO();
        if ( validador.validar_token(token) ){ 
            return dao.actualizarPedido(idPedido,idCotizacion,fechaPedido,cantidadProducida,
                                    fechaInicio,fechaTerminacion,estadoProduccion);
        }else{
            return validador.getMensajeToken();
        }
        
        
    }
}

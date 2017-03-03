/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.Pedido;
import co.com.ppi.modelo.PedidoDAO;
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
@Path("/pedido")
public class PedidoREST {
    
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
    public ArrayList<Pedido> listar(@QueryParam("seleccionar")String seleccionar,
                                    @QueryParam("campos")String campos,
                                    @QueryParam("valores")String valores,
                                    @QueryParam("orden")String orden) {
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
    public ArrayList<Pedido> consultarPedido(@QueryParam("idPedido")int idPedido) {
        PedidoDAO dao = new PedidoDAO();
        return dao.consultarPedido(idPedido);
    }
    
    /**
     *
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
    public String insertarPedido(/*@QueryParam("idPedido") int idPedido, */
                                 @QueryParam("idCotizacion")int idCotizacion, 
                                 @QueryParam("fechaPedido")String fechaPedido,
                                 @QueryParam("fechaInicio")String fechaInicio,
                                 @QueryParam("fechaTerminacion")String fechaTerminacion,
                                 @QueryParam("estadoProduccion")int estadoProduccion){
        PedidoDAO dao = new PedidoDAO();
        return dao.insertarPedido(/*idPedido,*/idCotizacion,fechaPedido,
                                  fechaInicio,fechaTerminacion,estadoProduccion);
    }
    
    /**
     *
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
    public String actualizarPedido(@QueryParam("idPedido") int idPedido, 
                                   @QueryParam("idCotizacion")int idCotizacion, 
                                   @QueryParam("fechaPedido")String fechaPedido, 
                                   @QueryParam("cantidadProducida")int cantidadProducida,
                                   @QueryParam("fechaInicio")String fechaInicio,
                                   @QueryParam("fechaTerminacion")String fechaTerminacion,
                                   @QueryParam("estadoProduccion")int estadoProduccion){
        PedidoDAO dao = new PedidoDAO();
        return dao.actualizarPedido(idPedido,idCotizacion,fechaPedido,cantidadProducida,
                                    fechaInicio,fechaTerminacion,estadoProduccion);
        
    }
}

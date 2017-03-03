/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.DetallePedidoCliente;
import co.com.ppi.modelo.DetallePedidoClienteDAO;
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
@Path("/detallePedidoCliente")
public class DetallePedidoClienteREST {
    
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
    public ArrayList<DetallePedidoCliente> listar(@QueryParam("seleccionar")String seleccionar,
                                                  @QueryParam("campos")String campos,
                                                  @QueryParam("valores")String valores,
                                                  @QueryParam("orden")String orden) {
        DetallePedidoClienteDAO dao = new DetallePedidoClienteDAO();
        return dao.consultarDetallePedidoClientes(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param idPedido
     * @param idProducto
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<DetallePedidoCliente> consultarDetallePedidoCliente(@QueryParam("idPedido")int idPedido,
                                                                         @QueryParam("idProducto")int idProducto) {
        DetallePedidoClienteDAO dao = new DetallePedidoClienteDAO();
        return dao.consultarDetallePedidoCliente(idPedido,idProducto);
    }
    
    /**
     *
     * @param id_Pedido
     * @param id_Producto
     * @param cantidad_Pedida
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarDetallePedidoCliente(@QueryParam("idPedido") int idPedido, 
                                               @QueryParam("idProducto") int idProducto,
                                               @QueryParam("cantidadPedida") int cantidadPedida){
        DetallePedidoClienteDAO dao = new DetallePedidoClienteDAO();
        return dao.insertarDetallePedidoCliente(idPedido,idProducto,cantidadPedida);
    }
    
    /**
     *
     * @param idPedido
     * @param idProducto
     * @param cantidadPedida
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarDetallePedidoCliente(@QueryParam("idPedido") int idPedido, 
                                                 @QueryParam("idProducto") int idProducto,
                                                 @QueryParam("cantidadPedida") int cantidadPedida){
        DetallePedidoClienteDAO dao = new DetallePedidoClienteDAO();
        return dao.actualizarDetallePedidoCliente(idPedido,idProducto,cantidadPedida);
    }
}

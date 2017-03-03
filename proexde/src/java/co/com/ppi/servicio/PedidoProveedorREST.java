/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.PedidoProveedor;
import co.com.ppi.modelo.PedidoProveedorDAO;
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
@Path("/pedidoProveedor")
public class PedidoProveedorREST {
    
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
    public ArrayList<PedidoProveedor> listar(@QueryParam("seleccionar")String seleccionar,
                                             @QueryParam("campos")String campos,
                                             @QueryParam("valores")String valores,
                                             @QueryParam("orden")String orden) {
        PedidoProveedorDAO dao = new PedidoProveedorDAO();
        return dao.consultarPedidoProveedores(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param idPedidoProveedor
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<PedidoProveedor> consultarPedidoProveedor(@QueryParam("idPedidoProveedor")int idPedidoProveedor) {
        PedidoProveedorDAO dao = new PedidoProveedorDAO();
        return dao.consultarPedidoProveedor(idPedidoProveedor);
    }
    
    /**
     *
     * @param idProveedor
     * @param fechaPedido
     * @param estadoProduccion
     * @param total
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarPedidoProveedor(/*@QueryParam("idPedidoProveedor") int idPedidoProveedor, */
                                           @QueryParam("idProveedor") int idProveedor, 
                                           @QueryParam("fechaPedido") String fechaPedido, 
                                           @QueryParam("estadoProduccion") int estadoProduccion,
                                           @QueryParam("total") int total){
        PedidoProveedorDAO dao = new PedidoProveedorDAO();
        return dao.insertarPedidoProveedor(/*idPedidoProveedor,*/idProveedor,fechaPedido,estadoProduccion,total);
    }
    
    /**
     *
     * @param idPedidoProveedor
     * @param idProveedor
     * @param fechaPedido
     * @param estadoProduccion
     * @param total
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarPedidoProveedor(@QueryParam("idPedidoProveedor") int idPedidoProveedor, 
                                            @QueryParam("idProveedor") int idProveedor, 
                                            @QueryParam("fechaPedido") String fechaPedido,
                                            @QueryParam("estadoProduccion") int estadoProduccion){
        PedidoProveedorDAO dao = new PedidoProveedorDAO();
        return dao.actualizarPedidoProveedor(idPedidoProveedor,idProveedor,fechaPedido,estadoProduccion);
    }
}

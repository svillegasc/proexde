/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.PedidoProveedor;
import co.com.ppi.modelo.PedidoProveedorDAO;
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
@Path("/pedidoProveedor")
public class PedidoProveedorREST {
    
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
    public ArrayList<PedidoProveedor> listar(@HeaderParam("seleccionar")String seleccionar,
                                             @HeaderParam("campos")String campos,
                                             @HeaderParam("valores")String valores,
                                             @HeaderParam("orden")String orden) {
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
    public ArrayList<PedidoProveedor> consultarPedidoProveedor(@HeaderParam("idPedidoProveedor")int idPedidoProveedor) {
        PedidoProveedorDAO dao = new PedidoProveedorDAO();
        return dao.consultarPedidoProveedor(idPedidoProveedor);
    }
    
    /**
     *
     * @param token
     * @param idProveedor
     * @param fechaPedido
     * @param estadoProduccion
     * @param total
     * @return
     * @throws java.lang.Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarPedidoProveedor(/*@HeaderParam("idPedidoProveedor") int idPedidoProveedor, */
                                           @HeaderParam("token")String token,
                                           @HeaderParam("idProveedor") int idProveedor, 
                                           @HeaderParam("fechaPedido") String fechaPedido, 
                                           @HeaderParam("estadoPedido") int estadoProduccion,
                                           @HeaderParam("total") int total) throws Exception{
        PedidoProveedorDAO dao = new PedidoProveedorDAO();
        if ( validador.validar_token(token) ){ 
            return dao.insertarPedidoProveedor(/*idPedidoProveedor,*/idProveedor,fechaPedido,estadoProduccion,total);
        }else{
            return validador.getMensajeToken();
        }
        
    }
    
    /**
     *
     * @param token
     * @param idPedidoProveedor
     * @param idProveedor
     * @param fechaPedido
     * @param estadoProduccion
     * @return
     * @throws java.lang.Exception
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarPedidoProveedor(@HeaderParam("token")String token,
                                            @HeaderParam("idPedidoProveedor") int idPedidoProveedor, 
                                            @HeaderParam("idProveedor") int idProveedor, 
                                            @HeaderParam("fechaPedido") String fechaPedido,
                                            @HeaderParam("estadoPedido") int estadoProduccion) throws Exception{
        PedidoProveedorDAO dao = new PedidoProveedorDAO();
        if ( validador.validar_token(token) ){ 
            return dao.actualizarPedidoProveedor(idPedidoProveedor,idProveedor,fechaPedido,estadoProduccion);
        }else{
            return validador.getMensajeToken();
        }
        
    }
}

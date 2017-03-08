/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.DetallePedidoProveedor;
import co.com.ppi.modelo.DetallePedidoProveedorDAO;
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
import javax.ws.rs.HeaderParam;

/**
 *
 * @author SANTI
 */
@Stateless
@Path("/detallePedidoProveedor")
public class DetallePedidoProveedorREST {
    
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
    public ArrayList<DetallePedidoProveedor> listar(@HeaderParam("seleccionar")String seleccionar,
                                                  @HeaderParam("campos")String campos,
                                                  @HeaderParam("valores")String valores,
                                                  @HeaderParam("orden")String orden) {
        DetallePedidoProveedorDAO dao = new DetallePedidoProveedorDAO();
        return dao.consultarDetallePedidoProveedores(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param idPedidoProveedor
     * @param idInsumo
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<DetallePedidoProveedor> consultarDetallePedidoProveedor(@HeaderParam("idPedidoProveedor")int idPedidoProveedor,
                                                                             @HeaderParam("idInsumo")int idInsumo) {
        DetallePedidoProveedorDAO dao = new DetallePedidoProveedorDAO();
        return dao.consultarDetallePedidoProveedor(idPedidoProveedor,idInsumo);
    }
    
    /**
     *
     * @param idPedidoProveedor
     * @param idInsumo
     * @param cantidad
     * @param precio
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarDetallePedidoProveedor(@HeaderParam("idPedidoProveedor")int idPedidoProveedor,
                                                 @HeaderParam("idInsumo")int idInsumo,
                                                 @HeaderParam("cantidad") int cantidad,
                                                 @HeaderParam("precio") int precio){
        DetallePedidoProveedorDAO dao = new DetallePedidoProveedorDAO();
        return dao.insertarDetallePedidoProveedor(idPedidoProveedor,idInsumo,cantidad,precio);
    }
    
    /**
     *
     * @param idPedidoProveedor
     * @param idInsumo
     * @param cantidad
     * @param precio
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarDetallePedidoProveedor(@HeaderParam("idPedidoProveedor")int idPedidoProveedor,
                                                   @HeaderParam("idInsumo")int idInsumo,
                                                   @HeaderParam("cantidad") int cantidad,
                                                   @HeaderParam("precio") int precio){
        DetallePedidoProveedorDAO dao = new DetallePedidoProveedorDAO();
        return dao.actualizarDetallePedidoProveedor(idPedidoProveedor,idInsumo,cantidad,precio);
    }
}

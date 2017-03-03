/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.Producto;
import co.com.ppi.modelo.ProductoDAO;
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
@Path("/producto")
public class ProductoREST {
    
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
    public ArrayList<Producto> listar(@QueryParam("seleccionar")String seleccionar,
                                      @QueryParam("campos")String campos,
                                      @QueryParam("valores")String valores,
                                      @QueryParam("orden")String orden) {
        ProductoDAO dao = new ProductoDAO();
        return dao.consultarProductos(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param idProducto
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<Producto> consultarProducto(@QueryParam("idProducto")int idProducto) {
        ProductoDAO dao = new ProductoDAO();
        return dao.consultarProducto(idProducto);
    }

    /**
     *
     * @param idProducto
     * @param nombre
     * @param descripcion
     * @param ultimaEntrada
     * @param ultimaSalida
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarProducto(/*@QueryParam("idProducto") int idProducto, */
                                   @QueryParam("nombre") String nombre, 
                                   @QueryParam("descripcion") String descripcion, 
                                   @QueryParam("ultimaEntrada") String ultimaEntrada,
                                   @QueryParam("ultimaSalida") String ultimaSalida){
        ProductoDAO dao = new ProductoDAO();
        return dao.insertarProducto(/*idProducto,*/nombre,descripcion,ultimaEntrada,ultimaSalida);
    }
    
    /**
     *
     * @param idProducto
     * @param nombre
     * @param descripcion
     * @param ultimaEntrada
     * @param ultimaSalida
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarProducto(@QueryParam("idProducto") int idProducto, 
                                     @QueryParam("nombre") String nombre, 
                                     @QueryParam("descripcion") String descripcion,
                                     @QueryParam("ultimaEntrada") String ultimaEntrada,
                                     @QueryParam("ultimaSalida") String ultimaSalida){
        ProductoDAO dao = new ProductoDAO();
        return dao.actualizarProducto(idProducto,nombre,descripcion,ultimaEntrada,ultimaSalida);
    }
    
    /**
     *
     * @param idProducto
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
    public String eliminarProducto(@QueryParam("idProducto") int idProducto){
        ProductoDAO dao = new ProductoDAO();
        return dao.eliminarProducto(idProducto);
    }
}

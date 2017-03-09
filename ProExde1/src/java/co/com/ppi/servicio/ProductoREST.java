/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.Producto;
import co.com.ppi.modelo.ProductoDAO;
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
@Path("/producto")
public class ProductoREST {
    
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
    public ArrayList<Producto> listar(@HeaderParam("seleccionar")String seleccionar,
                                      @HeaderParam("campos")String campos,
                                      @HeaderParam("valores")String valores,
                                      @HeaderParam("orden")String orden) {
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
    public ArrayList<Producto> consultarProducto(@HeaderParam("idProducto")int idProducto) {
        ProductoDAO dao = new ProductoDAO();
        return dao.consultarProducto(idProducto);
    }

    /**
     *
     * @param token
     * @param idProducto
     * @param nombre
     * @param descripcion
     * @param ultimaEntrada
     * @param ultimaSalida
     * @return
     * @throws java.lang.Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarProducto(/*@HeaderParam("idProducto") int idProducto, */
                                   @HeaderParam("token")String token,
                                   @HeaderParam("nombre") String nombre, 
                                   @HeaderParam("descripcion") String descripcion, 
                                   @HeaderParam("ultimaEntrada") String ultimaEntrada,
                                   @HeaderParam("ultimaSalida") String ultimaSalida) throws Exception{
        ProductoDAO dao = new ProductoDAO();
        if ( validador.validar_token(token) ){ 
            return dao.insertarProducto(/*idProducto,*/nombre,descripcion,ultimaEntrada,ultimaSalida);
        }else{
            return validador.getMensajeToken();
        }
        
    }
    
    /**
     *
     * @param token
     * @param idProducto
     * @param nombre
     * @param descripcion
     * @param ultimaEntrada
     * @param ultimaSalida
     * @return
     * @throws java.lang.Exception
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarProducto(@HeaderParam("token")String token,
                                     @HeaderParam("idProducto") int idProducto, 
                                     @HeaderParam("nombre") String nombre, 
                                     @HeaderParam("descripcion") String descripcion,
                                     @HeaderParam("ultimaEntrada") String ultimaEntrada,
                                     @HeaderParam("ultimaSalida") String ultimaSalida) throws Exception{
        ProductoDAO dao = new ProductoDAO();
        if ( validador.validar_token(token) ){ 
            return dao.actualizarProducto(idProducto,nombre,descripcion,ultimaEntrada,ultimaSalida);
        }else{
            return validador.getMensajeToken();
        }
        
    }
    
    /**
     *
     * @param token
     * @param idProducto
     * @return
     * @throws java.lang.Exception
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
    public String eliminarProducto(@HeaderParam("token")String token,
                                   @HeaderParam("idProducto") int idProducto) throws Exception{
        ProductoDAO dao = new ProductoDAO();
        if ( validador.validar_token(token) ){ 
            return dao.eliminarProducto(idProducto);
        }else{
            return validador.getMensajeToken();
        }
        
    }
}

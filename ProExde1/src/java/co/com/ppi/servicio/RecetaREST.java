/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.Receta;
import co.com.ppi.modelo.RecetaDAO;
import co.com.ppi.util.Validador;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.HeaderParam;

/**
 *
 * @author SANTI
 */
@Stateless
@Path("/receta")
public class RecetaREST {
    
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
    public ArrayList<Receta> listar(@HeaderParam("seleccionar")String seleccionar,
                                    @HeaderParam("campos")String campos,
                                    @HeaderParam("valores")String valores,
                                    @HeaderParam("orden")String orden) {
        RecetaDAO dao = new RecetaDAO();
        return dao.consultarRecetas(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param idProducto
     * @param idInsumo
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<Receta> consultarReceta(@HeaderParam("idProducto")int idProducto,
                                             @HeaderParam("idInsumo")int idInsumo) {
        RecetaDAO dao = new RecetaDAO();
        return dao.consultarReceta(idProducto,idInsumo);
    }
    
    /**
     *
     * @param token
     * @param idProducto
     * @param idInsumo
     * @param cantidadUtilizada
     * @return
     * @throws java.lang.Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarReceta(@HeaderParam("token")String token,
                                 @HeaderParam("idProducto") int idProducto, 
                                 @HeaderParam("idInsumo") int idInsumo, 
                                 @HeaderParam("cantidadUtilizada") int cantidadUtilizada) throws Exception{
        RecetaDAO dao = new RecetaDAO();
        if ( validador.validar_token(token) ){ 
            return dao.insertarReceta(idProducto,idInsumo,cantidadUtilizada);
        }else{
            return validador.getMensajeToken();
        } 
        
    }
    
    /**
     *
     * @param token
     * @param idProducto
     * @param idInsumo
     * @param cantidadUtilizada
     * @return
     * @throws java.lang.Exception
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarReceta(@HeaderParam("token")String token,
                                   @HeaderParam("idProducto") int idProducto, 
                                   @HeaderParam("idInsumo") int idInsumo, 
                                   @HeaderParam("cantidadUtilizada") int cantidadUtilizada) throws Exception{
        RecetaDAO dao = new RecetaDAO();
        if ( validador.validar_token(token) ){ 
           return dao.actualizarReceta(idProducto,idInsumo,cantidadUtilizada); 
        }else{
            return validador.getMensajeToken();
        } 
        
        
    }
    
    /**
     *
     * @param token
     * @param idProducto
     * @param idInsumo
     * @return
     * @throws java.lang.Exception
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
    public String eliminarReceta(@HeaderParam("token")String token,
                                 @HeaderParam("idProducto") int idProducto,
                                 @HeaderParam("idInsumo") int idInsumo) throws Exception{
        RecetaDAO dao = new RecetaDAO();
        if ( validador.validar_token(token) ){ 
            return dao.eliminarReceta(idProducto,idInsumo);
        }else{
            return validador.getMensajeToken();
        } 
        
    }
}

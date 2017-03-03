/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.Receta;
import co.com.ppi.modelo.RecetaDAO;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author SANTI
 */
@Stateless
@Path("/receta")
public class RecetaREST {
    
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
    public ArrayList<Receta> listar(@QueryParam("seleccionar")String seleccionar,
                                    @QueryParam("campos")String campos,
                                    @QueryParam("valores")String valores,
                                    @QueryParam("orden")String orden) {
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
    public ArrayList<Receta> consultarReceta(@QueryParam("idProducto")int idProducto,
                                             @QueryParam("idInsumo")int idInsumo) {
        RecetaDAO dao = new RecetaDAO();
        return dao.consultarReceta(idProducto,idInsumo);
    }
    
    /**
     *
     * @param idProducto
     * @param idInsumo
     * @param cantidadUtilizada
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarReceta(@QueryParam("idProducto") int idProducto, 
                                 @QueryParam("idInsumo") int idInsumo, 
                                 @QueryParam("cantidadUtilizada") int cantidadUtilizada){
        RecetaDAO dao = new RecetaDAO();
        return dao.insertarReceta(idProducto,idInsumo,cantidadUtilizada);
    }
    
    /**
     *
     * @param idProducto
     * @param idInsumo
     * @param cantidadUtilizada
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarReceta(@QueryParam("idProducto") int idProducto, 
                                   @QueryParam("idInsumo") int idInsumo, 
                                   @QueryParam("cantidadUtilizada") int cantidadUtilizada){
        RecetaDAO dao = new RecetaDAO();
        return dao.actualizarReceta(idProducto,idInsumo,cantidadUtilizada);
        
    }
    
    /**
     *
     * @param idProducto
     * @param idInsumo
     * @return
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
    public String eliminarReceta(@QueryParam("idProducto") int idProducto,
                                 @QueryParam("idInsumo") int idInsumo){
        RecetaDAO dao = new RecetaDAO();
        return dao.eliminarReceta(idProducto,idInsumo);
    }
}

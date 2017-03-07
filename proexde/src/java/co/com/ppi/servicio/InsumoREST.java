/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.util.Validador;
import co.com.ppi.entidades.Insumo;
import co.com.ppi.modelo.InsumoDAO;
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
@Path("/insumo")
public class InsumoREST {
    
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
    public ArrayList<Insumo> listar(@QueryParam("seleccionar")String seleccionar,
                                    @QueryParam("campos")String campos,
                                    @QueryParam("valores")String valores,
                                    @QueryParam("orden")String orden) {
        InsumoDAO dao = new InsumoDAO();
        return dao.consultarInsumos(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param idInsumo
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<Insumo> consultarInsumo(@QueryParam("idInsumo")int idInsumo) {
        InsumoDAO dao = new InsumoDAO();
        return dao.consultarInsumo(idInsumo);
    }
    
    /**
     *
     * @param idInsumo
     * @param nombre
     * @param descripcion
     * @param precioCompra
     * @param unidadMedida
     * @param ultimaEntrada
     * @param ultimaSalida
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarInsumo(/*@QueryParam("idInsumo") int idInsumo, */
                                 @QueryParam("token")String token,
                                 @QueryParam("nombre")String nombre, 
                                 @QueryParam("descripcion")String descripcion, 
                                 @QueryParam("precioCompra")int precioCompra,
                                 @QueryParam("unidadMedida")String unidadMedida,
                                 @QueryParam("ultimaEntrada")String ultimaEntrada,
                                 @QueryParam("ultimaSalida")String ultimaSalida){
        InsumoDAO dao = new InsumoDAO();
        
        if ( Validador.validar_token(token) ){
            return dao.insertarInsumo(/*idInsumo,*/nombre,descripcion,precioCompra, unidadMedida,ultimaEntrada,ultimaSalida);    
        }else{
            return Validador.getMensajeToken();
        }
        
        
    }
    
    /**
     *
     * @param idInsumo
     * @param nombre
     * @param descripcion
     * @param precioCompra
     * @param unidadMedida
     * @param ultimaEntrada
     * @param ultimaSalida
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarInsumo(@QueryParam("idInsumo") int idInsumo, 
                                   @QueryParam("nombre")String nombre, 
                                   @QueryParam("descripcion")String descripcion, 
                                   @QueryParam("precioCompra")int precioCompra,
                                   @QueryParam("unidadMedida")String unidadMedida,
                                   @QueryParam("ultimaEntrada")String ultimaEntrada,
                                   @QueryParam("ultimaSalida")String ultimaSalida){
        InsumoDAO dao = new InsumoDAO();
        return dao.actualizarInsumo(idInsumo,nombre,descripcion,precioCompra,
                                    unidadMedida,ultimaEntrada,ultimaSalida);
        
    }
    
    /**
     *
     * @param idInsumo
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
    public String eliminarInsumo(@QueryParam("idInsumo") int idInsumo){
        InsumoDAO dao = new InsumoDAO();
        return dao.eliminarInsumo(idInsumo);
    }
}


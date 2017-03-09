/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.ProveedorInsumo;
import co.com.ppi.modelo.ProveedorInsumoDAO;
import co.com.ppi.util.Validador;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("/proveedorInsumo")
public class ProveedorInsumoREST {
    
    
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
    public ArrayList<ProveedorInsumo> listar(@HeaderParam("seleccionar")String seleccionar,
                                            @HeaderParam("campos")String campos,
                                            @HeaderParam("valores")String valores,
                                            @HeaderParam("orden")String orden) {
        ProveedorInsumoDAO dao = new ProveedorInsumoDAO();
        return dao.consultarProveedorInsumos(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param idProveedor
     * @param idInsumo
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<ProveedorInsumo> consultarProveedorInsumo(@HeaderParam("idProveedor")int idProveedor,
                                                             @HeaderParam("idInsumo")int idInsumo) {
        ProveedorInsumoDAO dao = new ProveedorInsumoDAO();
        return dao.consultarProveedorInsumo(idProveedor,idInsumo);
    }
    
    /**
     *
     * @param token
     * @param idProveedor
     * @param idInsumo
     * @return
     * @throws java.lang.Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarProveedorInsumo(@HeaderParam("token")String token,
                                          @HeaderParam("idProveedor") int idProveedor, 
                                          @HeaderParam("idInsumo") int idInsumo) throws Exception{
        ProveedorInsumoDAO dao = new ProveedorInsumoDAO();
        if ( validador.validar_token(token) ){ 
            return dao.insertarProveedorInsumo(idProveedor,idInsumo);
        }else{
            return validador.getMensajeToken();
        }
        
    }
    
    /**
     *
     * @param token
     * @param idProveedor
     * @param idInsumo
     * @return
     * @throws java.lang.Exception
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
    public String eliminarProveedorInsumo(@HeaderParam("token")String token,
                                          @HeaderParam("idProveedor") int idProveedor,
                                          @HeaderParam("idInsumo") int idInsumo) throws Exception{
        ProveedorInsumoDAO dao = new ProveedorInsumoDAO();
        if ( validador.validar_token(token) ){ 
            return dao.eliminarProveedorInsumo(idProveedor,idInsumo);
        }else{
            return validador.getMensajeToken();
        }
        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.Perfil;
import co.com.ppi.modelo.PerfilDAO;
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
@Path("/perfil")
public class PerfilREST {
    
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
    public ArrayList<Perfil> listar(@HeaderParam("seleccionar")String seleccionar,
                                    @HeaderParam("campos")String campos,
                                    @HeaderParam("valores")String valores,
                                    @HeaderParam("orden")String orden) {
        PerfilDAO dao = new PerfilDAO();
        return dao.consultarPerfiles(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param idPerfil
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<Perfil> consultarPerfil(@HeaderParam("idPerfil")int idPerfil) {
        PerfilDAO dao = new PerfilDAO();
        return dao.consultarPerfil(idPerfil);
    }
   
    /**
     *
     * @param token
     * @param nombre
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarPerfil(/*@HeaderParam("idPerfil") int idPerfil,*/
                                 @HeaderParam("token")String token,
                                 @HeaderParam("nombre")String nombre){
        PerfilDAO dao = new PerfilDAO();
        if ( validador.validar_token(token) ){ 
            return dao.insertarPerfil(/*idPerfil,*/nombre);
        }else{
            return validador.getMensajeToken();
        }
        
    }
    
    /**
     *
     * @param token
     * @param idPerfil
     * @param nombre
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarPerfil(@HeaderParam("token")String token,
                                   @HeaderParam("idPerfil") int idPerfil,
                                   @HeaderParam("nombre") String nombre){
        PerfilDAO dao = new PerfilDAO();
        if ( validador.validar_token(token) ){ 
            return dao.actualizarPerfil(idPerfil,nombre);
        }else{
            return validador.getMensajeToken();
        }
        
    }
    
    /**
     *
     * @param token
     * @param idPerfil
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
    public String eliminarPerfil(@HeaderParam("token")String token,
                                 @HeaderParam("idPerfil") int idPerfil){
        PerfilDAO dao = new PerfilDAO();
        if ( validador.validar_token(token) ){ 
           return dao.eliminarPerfil(idPerfil); 
        }else{
            return validador.getMensajeToken();
        }
        
    }
}

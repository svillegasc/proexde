/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.Perfil;
import co.com.ppi.modelo.PerfilDAO;
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
@Path("/perfil")
public class PerfilREST {
    
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
    public ArrayList<Perfil> listar(@QueryParam("seleccionar")String seleccionar,
                                    @QueryParam("campos")String campos,
                                    @QueryParam("valores")String valores,
                                    @QueryParam("orden")String orden) {
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
    public ArrayList<Perfil> consultarPerfil(@QueryParam("idPerfil")int idPerfil) {
        PerfilDAO dao = new PerfilDAO();
        return dao.consultarPerfil(idPerfil);
    }
   
    /**
     *
     * @param idPerfil
     * @param nombre
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarPerfil(/*@QueryParam("idPerfil") int idPerfil,*/
                                 @QueryParam("nombre")String nombre){
        PerfilDAO dao = new PerfilDAO();
        return dao.insertarPerfil(/*idPerfil,*/nombre);
    }
    
    /**
     *
     * @param idPerfil
     * @param nombre
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarPerfil(@QueryParam("idPerfil") int idPerfil,
                                   @QueryParam("nombre") String nombre){
        PerfilDAO dao = new PerfilDAO();
        return dao.actualizarPerfil(idPerfil,nombre);
    }
    
    /**
     *
     * @param idPerfil
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
    public String eliminarPerfil(@QueryParam("idPerfil") int idPerfil){
        PerfilDAO dao = new PerfilDAO();
        return dao.eliminarPerfil(idPerfil);
    }
}

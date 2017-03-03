/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.servicio;

import co.com.ppi.entidades.Usuario;
import co.com.ppi.modelo.UsuarioDAO;
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
@Path("/usuario")
public class UsuarioREST {
    
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
    public ArrayList<Usuario> listar(@QueryParam("seleccionar")String seleccionar,
                                     @QueryParam("campos")String campos,
                                     @QueryParam("valores")String valores,
                                     @QueryParam("orden")String orden) {
        UsuarioDAO dao = new UsuarioDAO();
        return dao.consultarUsuarios(seleccionar,campos,valores,orden);
    }
    
    /**
     *
     * @param idUsuario
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar")
    public ArrayList<Usuario> consultarUsuario(@QueryParam("idUsuario")int idUsuario) {
        UsuarioDAO dao = new UsuarioDAO();
        return dao.consultarUsuario(idUsuario);
    }
    
    /**
     *
     * @param cuenta
     * @param primerNombre
     * @param segundoNombre
     * @param primerApellido
     * @param segundoApellido
     * @param identificacion
     * @param tipoIdentificacion
     * @param telefono
     * @param password
     * @param idPerfil
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertar")
    public String insertarUsuario(/*@QueryParam("idUsuario") int idUsuario, */
                                  @QueryParam("cuenta")String cuenta, 
                                  @QueryParam("primerNombre")String primerNombre, 
                                  @QueryParam("segundoNombre")String segundoNombre,
                                  @QueryParam("primerApellido")String primerApellido,
                                  @QueryParam("segundoApellido")String segundoApellido,
                                  @QueryParam("identificacion")String identificacion,
                                  @QueryParam("tipoIdentificacion")int tipoIdentificacion,
                                  @QueryParam("telefono")String telefono,
                                  @QueryParam("password")String password,
                                  @QueryParam("idPerfil")int idPerfil){
        UsuarioDAO dao = new UsuarioDAO();
        return dao.insertarUsuario(/*idUsuario,*/cuenta,primerNombre,segundoNombre,
                                   primerApellido,segundoApellido,identificacion,
                                   tipoIdentificacion,telefono,password,idPerfil);
    }
    
    /**
     *
     * @param idUsuario
     * @param cuenta
     * @param primerNombre
     * @param segundoNombre
     * @param primerApellido
     * @param segundoApellido
     * @param identificacion
     * @param tipoIdentificacion
     * @param telefono
     * @param password
     * @param idPerfil
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/actualizar")
    public String actualizarUsuario(@QueryParam("idUsuario") int idUsuario, 
                                    @QueryParam("cuenta")String cuenta, 
                                    @QueryParam("primerNombre")String primerNombre, 
                                    @QueryParam("segundoNombre")String segundoNombre,
                                    @QueryParam("primerApellido")String primerApellido,
                                    @QueryParam("segundoApellido")String segundoApellido,
                                    @QueryParam("identificacion")String identificacion,
                                    @QueryParam("tipoIdentificacion")int tipoIdentificacion,
                                    @QueryParam("telefono")String telefono,
                                    @QueryParam("password")String password,
                                    @QueryParam("idPerfil")int idPerfil){
        UsuarioDAO dao = new UsuarioDAO();
        return dao.actualizarUsuario(idUsuario,cuenta,primerNombre,segundoNombre,
                                     primerApellido,segundoApellido,identificacion,
                                     tipoIdentificacion,telefono,password,idPerfil);
    }
    
    /**
     *
     * @param idUsuario
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/eliminar")
    public String eliminarUsuario(@QueryParam("idUsuario") int idUsuario){
        UsuarioDAO dao = new UsuarioDAO();
        return dao.eliminarUsuario(idUsuario);
    }
    
    /**
     *
     * @param cuenta
     * @param password
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login")
    public String logueoUsuario(@QueryParam("cuenta") String cuenta,
                                @QueryParam("password") String password){
        UsuarioDAO dao = new UsuarioDAO();
        return dao.logueoUsuario(cuenta,password);
    }
}

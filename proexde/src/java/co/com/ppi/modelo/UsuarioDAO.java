/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.util.Validador;
import co.com.ppi.entidades.Usuario;
import co.com.ppi.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author SANTI
 */
public class UsuarioDAO {
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarUsuario(/*int idUsuario,*/String cuenta,String primerNombre,
                                  String segundoNombre,String primerApellido,
                                  String segundoApellido,String identificacion,
                                  int tipoIdentificacion,String telefono,
                                  String password,int idPerfil)
    {
        String sql="INSERT INTO USUARIO VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,null,null)";
        try
        {
            boolean valPass = validarPassword(password);
            if( cuenta == null || cuenta.trim().equals("")){
                return "Falta la cuenta.";
            }else
            if( primerNombre == null || primerNombre.trim().equals("")){
                return "Falta el primer nombre.";
            }else
            if( primerApellido == null || primerApellido.trim().equals("")){
                return "Falta el primer apellido.";
            }else
            if( identificacion == null || identificacion.trim().equals("")){
                return "Falta el número de identificación.";
            }else
            if( tipoIdentificacion == -1){
                return "Falta el tipo de identificación.";
            }else
            if( password == null || password.trim().equals("")){
                return "Falta la contraseña.";
            }else
            if(valPass == false){
                return "Password incorrecta, recuerde que debe llevar Mayusculas, "
                        + "Minusculas, números y ser mayor a 6 y menor a 15.";
            }else
            if( idPerfil == -1){
                return "Falta perfil.";
            }

            String sqlI="SELECT USU.NEXTVAL FROM DUAL";
            int idUsuario = 0;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                idUsuario = rs.getInt("NEXTVAL");
            }

            con=conex.conexion();
            pr=con.prepareStatement(sql);

            pr=con.prepareStatement(sql);
            pr.setInt(1,idUsuario);
            pr.setString(2,cuenta);
            pr.setString(3,primerNombre);
            pr.setString(4,segundoNombre);
            pr.setString(5,primerApellido);
            pr.setString(6,segundoApellido);
            pr.setString(7,identificacion);
            pr.setInt(8,tipoIdentificacion);
            pr.setString(9,telefono);
            pr.setString(10,password);
            pr.setInt(11,idPerfil);
            pr.setString(12,"A");
            pr.setString(13,null);
            pr.executeUpdate();
        }
        catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
        return "Se inserto correctamente";
    }
    
    public ArrayList<Usuario>consultarUsuarios(String sel, String cam, String val, String ord)
    {
        ArrayList<Usuario> result = new ArrayList<> ();
        Usuario usuario = new Usuario();
        String seleccionar = sel != null && !sel.trim().equals("") ? sel : "";
        String[] campos = cam != null && !cam.trim().equals("") ? cam.split(",") : null;
        String[] valores = val != null && !val.trim().equals("") ? val.split(",") : null;
        String orden = ord != null && !ord.trim().equals("") ? ord : "";
        
        try
        {
           con=conex.conexion();
           //String sql="SELECT ID_USUARIO,NOMBRE,DESCRIPCION,UNIDAD_MEDIDA FROM USUARIOS WHERE ESTADO=0";
           StringBuilder sql = new StringBuilder();
           
           
           if( !seleccionar.equals("") ){
               sql.append("SELECT ");
               sql.append(seleccionar);
               sql.append(" FROM USUARIO ");
           }else{
               usuario.setIdPerfil(-1);
               usuario.setIdUsuario(-1);
               usuario.setTipoIdentificacion(-1);
               usuario.setCuenta("ERROR: Faltan los campos a seleccionar en la consulta.");
               result.add(usuario);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if (campos[i].equals("ID_USUARIO") || campos[i].equals("ESTADO")){
                                sql.append(campos[i]);
                                sql.append(" = '");
                                sql.append(valores[i]);
                                sql.append("' AND ");
                            }else{
                                sql.append(campos[i]);
                                sql.append(" LIKE '%");
                                sql.append(valores[i]);
                                sql.append("%' AND ");
                            }
                        }

                        String aux = sql.toString();
                        aux = aux.substring(0, aux.length()-4);
                        sql = new StringBuilder();
                        sql.append(aux);
                   }
               }else{
                    usuario.setIdPerfil(-1);
                    usuario.setIdUsuario(-1);
                    usuario.setTipoIdentificacion(-1);
                    usuario.setCuenta("ERROR: Faltan los campos a seleccionar en la consulta.");
                    result.add(usuario);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                usuario.setIdPerfil(-1);
                usuario.setIdUsuario(-1);
                usuario.setTipoIdentificacion(-1);
                usuario.setCuenta("ERROR: Faltan los campos a seleccionar en la consulta.");
                result.add(usuario);
                return result;  
           }
           
           if(!orden.isEmpty()){
               sql.append(" ORDER BY ");
               sql.append(orden);
           }
           
           pr=con.prepareStatement(sql.toString());
           rs=pr.executeQuery();
           
           String[] select = seleccionar.split(",");
           
           while ( rs.next() )
           {    
               Usuario u = new Usuario();
               if(select.length == 1 && select[0].trim().equals("*")){
                   u.setIdUsuario(rs.getInt("ID_USUARIO"));
                   u.setCuenta(rs.getString("CUENTA"));
                   u.setPrimerNombre(rs.getString("PRIMER_NOMBRE"));
                   u.setSegundoNombre(rs.getString("SEGUNDO_NOMBRE"));
                   u.setPrimerApellido(rs.getString("PRIMER_APELLIDO"));
                   u.setSegundoApellido(rs.getString("SEGUNDO_APELLIDO"));
                   u.setIdentificacion(rs.getString("IDENTIFICACION"));
                   u.setTipoIdentificacion(rs.getInt("TIPO_IDENTIFICACION"));
                   u.setTelefono(rs.getString("TELEFONO"));
                   u.setPassword(rs.getString("PASSWORD"));
                   u.setIdPerfil(rs.getInt("ID_PERFIL"));
                   u.setEstado(rs.getString("ESTADO"));
                   
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if(select[j].toUpperCase().equals("ID_USUARIO")){
                            u.setIdUsuario(rs.getInt("ID_USUARIO"));
                        }  
                        if(select[j].toUpperCase().equals("CUENTA")){
                            u.setCuenta(rs.getString("CUENTA"));
                        }
                        if(select[j].toUpperCase().equals("PRIMER_NOMBRE")){
                            u.setPrimerNombre(rs.getString("PRIMER_NOMBRE"));
                        }
                        if(select[j].toUpperCase().equals("SEGUNDO_NOMBRE")){
                            u.setSegundoNombre(rs.getString("SEGUNDO_NOMBRE"));
                        }
                        if(select[j].toUpperCase().equals("PRIMER_APELLIDO")){
                            u.setPrimerApellido(rs.getString("PRIMER_APELLIDO"));
                        }
                        if(select[j].toUpperCase().equals("SEGUNDO_APELLIDO")){
                            u.setSegundoApellido(rs.getString("SEGUNDO_APELLIDO"));
                        }
                        if(select[j].toUpperCase().equals("IDENTIFICACION")){
                            u.setIdentificacion(rs.getString("IDENTIFICACION"));
                        }
                        if(select[j].toUpperCase().equals("TIPO_IDENTIFICACION")){
                            u.setTipoIdentificacion(rs.getInt("TIPO_IDENTIFICACION"));
                        }
                        if(select[j].toUpperCase().equals("TELEFONO")){
                            u.setTelefono(rs.getString("TELEFONO"));
                        }
                        if(select[j].toUpperCase().equals("PASSWORD")){
                            u.setPassword(rs.getString("PASSWORD"));
                        }
                        if(select[j].toUpperCase().equals("ID_PERFIL")){
                            u.setEstado(rs.getString("ID_PERFIL"));
                        }
                        if(select[j].toUpperCase().equals("ESTADO")){
                            u.setEstado(rs.getString("ESTADO"));
                        }
                    }
               }

               result.add(u);            
           }
        }
        catch(Exception ex){
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            usuario.setIdPerfil(-1);
            usuario.setIdUsuario(-1);
            usuario.setTipoIdentificacion(-1);
            usuario.setCuenta("ERROR: Faltan los campos a seleccionar en la consulta.");
            result.add(usuario);
            return result;
        }
       finally{
           try{
               rs.close();
               pr.close();
               con.close();
           }
           catch(Exception ex){}
       }
        return result;
    }
    
    public ArrayList<Usuario> consultarUsuario(int idUsuario)
    {
        String sql="SELECT CUENTA,PRIMER_NOMBRE,SEGUNDO_NOMBRE,PRIMER_APELLIDO,SEGUNDO_APELLIDO "
                  + "IDENTIFICACION,TIPO_IDENTIFICACION,TELEFONO,PASSWORD,ID_PERFIL,TOKEN "
                   + "FROM USUARIO WHERE ID_USUARIO='"+idUsuario+"' AND ESTADO='A'";
        ArrayList<Usuario> result = new ArrayList<>();
        Usuario u = new Usuario();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               u.setIdUsuario(idUsuario);
               u.setCuenta(rs.getString("CUENTA"));
               u.setPrimerNombre(rs.getString("PRIMER_NOMBRE"));
               u.setSegundoNombre(rs.getString("SEGUNDO_NOMBRE"));
               u.setPrimerApellido(rs.getString("PRIMER_APELLIDO"));
               u.setSegundoApellido(rs.getString("SEGUNDO_APELLIDO"));
               u.setIdentificacion(rs.getString("IDENTIFICACION"));
               u.setTipoIdentificacion(rs.getInt("TIPO_IDENTIFICACION"));
               u.setTelefono(rs.getString("TELEFONO"));
               u.setPassword(rs.getString("PASSWORD"));
               u.setIdPerfil(rs.getInt("ID_PERFIL"));
               u.setToken(rs.getInt("TOKEN"));
               result.add(u);
               
           }
        }
        catch(Exception ex)
       {Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);}
       finally
       {
           try
           {
               rs.close();
               pr.close();
               con.close();
           }
           catch(Exception ex){}
       }
        return result;
    }
    
    public String actualizarUsuario(int idUsuario,String cuenta,String primerNombre,
                                    String segundoNombre,String primerApellido,
                                    String segundoApellido,String identificacion,
                                    int tipoIdentificacion,String telefono,
                                    String password,int idPerfil){
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM USUARIO WHERE "
                      + "ID_USUARIO = '"+idUsuario+"' AND ESTADO='A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE USUARIO SET CUENTA='"+cuenta+"', PRIMER_NOMBRE='"+primerNombre+"', SEGUNDO_NOMBRE='"+segundoNombre+"', "
                    + "PRIMER_APELLIDO='"+primerApellido+"', SEGUNDO_APELLIDO='"+segundoApellido+"', IDENTIFICACION='"+identificacion+"', "
                    + "TIPO_IDENTIFICACION='"+tipoIdentificacion+"', TELEFONO='"+telefono+"', PASSWORD='"+password+"', ID_PERFIL='"+idPerfil+"' "
                    + "WHERE ID_USUARIO = '"+idUsuario+"' AND ESTADO='A'";
                    
                    if( cuenta == null || cuenta.trim().equals("")){
                        return "Falta la cuenta.";
                    }else
                    if( primerNombre == null || primerNombre.trim().equals("")){
                        return "Falta el primer nombre.";
                    }else
                    if( primerApellido == null || primerApellido.trim().equals("")){
                        return "Falta el primer apellido.";
                    }else
                    if( identificacion == null || identificacion.trim().equals("")){
                        return "Falta el número de identificación.";
                    }else
                    if( tipoIdentificacion == -1){
                        return "Falta el tipo de identificación.";
                    }else
                    if( password == null || password.trim().equals("")){
                        return "Falta la contraseña.";
                    }else
                    if( idPerfil == -1){
                        return "Falta perfil.";
                    }
                    
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas actualizadas";
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
       }   
       return "Se actualizo correctamente";
    }
    
    public String eliminarUsuario(int idUsuario)
    {
        try{
            String sqlI="SELECT COUNT (*) CONT FROM USUARIO WHERE ID_USUARIO = '"+idUsuario+"' AND ESTADO='A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE USUARIO SET ESTADO='I' WHERE ID_USUARIO = '"+idUsuario+"'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas eliminadas";
                }
            }
        }catch(Exception ex){
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
       finally
       {
           try
           {
               rs.close();
               pr.close();
               con.close();
           }
           catch(Exception ex){
           Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);}
       }    
//        return "El id del insumo #"+id_Usuario+" fue eliminado correctamente";
        return "Se elimino correctamente";
    }
    
    public String logueoUsuario (String cuenta, String password){
        try {
            String sql="SELECT COUNT (*) CONT FROM USUARIO WHERE CUENTA = ? AND PASSWORD = ? AND ESTADO='A' ";
            con=conex.conexion();
            pr=con.prepareStatement(sql);
            pr.setString(1,cuenta);
            pr.setString(2,password);

            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    
                    String token = Validador.crearToken();
                    sql="UPDATE USUARIO SET TOKEN = ? WHERE CUENTA = ? AND PASSWORD = ?";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setString(1,token);
                    pr.setString(2,cuenta);
                    pr.setString(3,password);
                    pr.executeUpdate();
                    Validador.actualizarFechaToken(token);

                }else{
                    return "Usuario y/o password incorrecto";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Se logueo Correctamente";
    }
    
    public boolean validarPassword(String pass){
        String PATTERN_EMAIL = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,15}$";

        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(pass);
        
        System.out.println(matcher.matches());
        return matcher.matches();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.Perfil;
import co.com.ppi.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SANTI
 */
public class PerfilDAO {
    
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarPerfil(/*int idPerfil,*/String nombre)
    {
        String sql="INSERT INTO PERFIL VALUES(?,?,?)";
        try
        {
            if( nombre == null || nombre.trim().equals("")){
                return "Falta el nombre.";
            }

            String sqlI="SELECT PERF.NEXTVAL FROM DUAL";
            int idPerfil = 0;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                idPerfil = rs.getInt("NEXTVAL");
            }

            con=conex.conexion();
            pr=con.prepareStatement(sql);

            pr=con.prepareStatement(sql);
            pr.setInt(1,idPerfil);
            pr.setString(2,nombre);
            pr.setString(3,"A");
            pr.executeUpdate();
        }
        catch (SQLException ex) {
            Logger.getLogger(PerfilDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
        return "Se inserto correctamente";
    }
    
    public ArrayList<Perfil> consultarPerfiles(String sel, String cam, String val, String ord)
    {
        ArrayList<Perfil> result = new ArrayList<> ();
        Perfil perfil = new Perfil();
        
        String seleccionar = sel != null && !sel.trim().equals("") ? sel : "";
        String[] campos = cam != null && !cam.trim().equals("") ? cam.split(",") : null;
        String[] valores = val != null && !val.trim().equals("") ? val.split(",") : null;
        String orden = ord != null && !ord.trim().equals("") ? ord : "";
        
        try
        {
           con=conex.conexion();
           //String sql="SELECT ID_INSUMO,NOMBRE,DESCRIPCION,UNIDAD_MEDIDA FROM INSUMOS WHERE ESTADO=0";
           StringBuilder sql = new StringBuilder();
           
           
           if( !seleccionar.equals("") ){
               sql.append("SELECT ");
               sql.append(seleccionar);
               sql.append(" FROM PERFIL ");
           }else{
               perfil.setIdPerfil(-1);
               perfil.setNombre("ERROR: Faltan los campos a seleccionar en la consulta.");
               result.add(perfil);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if (campos[i].equals("ID_PERFIL") || campos[i].equals("ESTADO")){
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
                    perfil.setIdPerfil(-1);
                    perfil.setNombre("ERROR: Los campos y los valores no tienen el mismo número de argumentos.");
                    result.add(perfil);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                perfil.setIdPerfil(-1);
                perfil.setNombre("ERROR: Los campos y los valores no tienen el mismo número de argumentos.");
                result.add(perfil);
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
               Perfil i = new Perfil();
               
               if(select.length == 1 && select[0].trim().equals("*")){
                   i.setIdPerfil(rs.getInt("ID_PERFIL"));
                   i.setNombre(rs.getString("NOMBRE"));
                   i.setEstado(rs.getString("ESTADO"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if(select[j].toUpperCase().equals("ID_PERFIL")){
                            i.setIdPerfil(rs.getInt("ID_PERFIL"));
                        }  
                        if(select[j].toUpperCase().equals("NOMBRE")){
                            i.setNombre(rs.getString("NOMBRE"));
                        }
                        if(select[j].toUpperCase().equals("ESTADO")){
                            i.setEstado(rs.getString("ESTADO"));
                        }
                    }
               }
               result.add(i);            
           }
        }
        catch(Exception ex){
            Logger.getLogger(PerfilDAO.class.getName()).log(Level.SEVERE, null, ex);
            perfil.setIdPerfil(-1);
            perfil.setNombre(ex.getMessage());
            result.add(perfil);
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
    
    public ArrayList<Perfil> consultarPerfil(int idPerfil)
    {
        String sql="SELECT NOMBRE "
                   + "FROM PERFIL WHERE ID_PERFIL='"+idPerfil+"' AND ESTADO='A'";
        ArrayList<Perfil> result = new ArrayList<>();
        Perfil p = new Perfil();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               p.setIdPerfil(idPerfil);
               p.setNombre(rs.getString("NOMBRE"));
               result.add(p);
               
           }
        }
        catch(Exception ex)
       {Logger.getLogger(PerfilDAO.class.getName()).log(Level.SEVERE, null, ex);}
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
    
    public String actualizarPerfil(int idPerfil,String nombre)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM PERFIL WHERE ID_PERFIL = '"+idPerfil+"' AND ESTADO='A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE PERFIL SET NOMBRE='"+nombre+"'"
                            + "WHERE ID_PERFIL = '"+idPerfil+"' AND ESTADO='A'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
            Logger.getLogger(PerfilDAO.class.getName()).log(Level.SEVERE, null, ex);
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
           catch(Exception ex){}
       }    
        return "Se actualizo correctamente";
    }
    
    public String eliminarPerfil(int idPerfil)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM PERFIL WHERE ID_PERFIL = '"+idPerfil+"' AND ESTADO='A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE PERFIL SET ESTADO='I' WHERE ID_PERFIL = '"+idPerfil+"'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
            Logger.getLogger(PerfilDAO.class.getName()).log(Level.SEVERE, null, ex);
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
           catch(Exception ex){}
       }    
        return "Se elimino correctamente";
    }
}

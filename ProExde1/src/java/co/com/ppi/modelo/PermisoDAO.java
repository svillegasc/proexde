/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.Permiso;
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
public class PermisoDAO {
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarPermiso(/*int idPermiso,*/String descripcion)
    {       
        String sql="INSERT INTO PERMISO VALUES(?,?,?)";
        try
        {
            if( descripcion == null || "".trim().equals(descripcion)){
                return "Falta la descripción del permiso.";
            }

            String sqlI="SELECT PERM.NEXTVAL FROM DUAL";
            int idPermiso = 0;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                idPermiso = rs.getInt("NEXTVAL");
            }

            con=conex.conexion();
            pr=con.prepareStatement(sql);

            pr=con.prepareStatement(sql);
            pr.setInt(1,idPermiso);
            pr.setString(2,descripcion);
            pr.setString(3,"A");
            pr.executeUpdate();
        }
        catch (SQLException ex) {
            Logger.getLogger(PermisoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }    
        return "Se inserto correctamente";
    }
    
    public ArrayList<Permiso> consultarPermisos(String sel, String cam, String val, String ord)
    {
        ArrayList<Permiso> result = new ArrayList<> ();
        Permiso permiso = new Permiso();
        
        String seleccionar = sel != null && !"".trim().equals(sel) ? sel : "";
        String[] campos = cam != null && !"".trim().equals(cam) ? cam.split(",") : null;
        String[] valores = val != null && !"".trim().equals(val) ? val.split(",") : null;
        String orden = ord != null && !"".trim().equals(ord) ? ord : "";
        
        try
        {
           con=conex.conexion();
           StringBuilder sql = new StringBuilder();
           
           
           if( !"".equals(seleccionar) ){
               sql.append("SELECT ");
               sql.append(seleccionar);
               sql.append(" FROM PERMISO ");
           }else{
               permiso.setIdPermiso(-1);
               permiso.setDescripcion("ERROR: Faltan los campos a seleccionar en la consulta.");
               result.add(permiso);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if ("ID_PERMISO".equals(campos[i]) || "ESTADO".equals(campos[i])){
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
                    permiso.setIdPermiso(-1);
                    permiso.setDescripcion("ERROR: Los campos y los valores no tienen el mismo número de argumentos.");
                    result.add(permiso);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                permiso.setIdPermiso(-1);
                permiso.setDescripcion("ERROR: Los campos y los valores no tienen el mismo número de argumentos.");
                result.add(permiso);
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
               Permiso p = new Permiso();
               
               if(select.length == 1 && "*".trim().equals(select[0])){
                   p.setIdPermiso(rs.getInt("ID_PERMISO"));
                   p.setDescripcion(rs.getString("DESCRIPCION"));
                   p.setEstado(rs.getString("ESTADO"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if("ID_PERMISO".toUpperCase().equals(select[j])){
                            p.setIdPermiso(rs.getInt("ID_PERMISO"));
                        }  
                        if("DESCRIPCION".toUpperCase().equals(select[j])){
                            p.setDescripcion(rs.getString("DESCRIPCION"));
                        }
                        if("ESTADO".toUpperCase().equals(select[j])){
                            p.setEstado(rs.getString("ESTADO"));
                        }
                    }
               }
               result.add(p);            
           }
        }
        catch(SQLException ex){
            Logger.getLogger(PermisoDAO.class.getName()).log(Level.SEVERE, null, ex);
            permiso.setIdPermiso(-1);
            permiso.setDescripcion(ex.getMessage());
            result.add(permiso);
            return result;
        }
       finally{
           try{
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(PermisoDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public ArrayList<Permiso> consultarPermiso(int idPermiso)
    {
        String sql="SELECT DESCRIPCION "
                   + "FROM PERMISO WHERE ID_DESCRIPCION='"+idPermiso+"' AND ESTADO='A'";
        ArrayList<Permiso> result = new ArrayList<>();
        Permiso p = new Permiso();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               p.setIdPermiso(idPermiso);
               p.setDescripcion(rs.getString("DESCRIPCION"));
               result.add(p);
               
           }
        }
        catch(SQLException ex)
       {Logger.getLogger(PermisoDAO.class.getName()).log(Level.SEVERE, null, ex);}
       finally
       {
           try
           {
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(PermisoDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public String actualizarPermiso(int idPermiso,String descripcion)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM PERMISO WHERE ID_PERMISO = ? AND ESTADO='A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, idPermiso);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE PERMISO SET DESCRIPCION='"+descripcion+"'"
                            + "WHERE ID_PERMISO = ? AND ESTADO='A'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setInt(1, idPermiso);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger(PermisoDAO.class.getName()).log(Level.SEVERE, null, ex);
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
           catch(SQLException ex){
               Logger.getLogger(PermisoDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }    
        return "Se actualizo correctamente";
    }
    
    public String eliminarPermiso(int idPermiso)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM PERMISO WHERE ID_PERMISO = ? AND ESTADO='A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, idPermiso);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE PERMISO SET ESTADO='I' WHERE ID_PERMISO = ?";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setInt(1, idPermiso);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger(PermisoDAO.class.getName()).log(Level.SEVERE, null, ex);
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
           catch(SQLException ex){
               Logger.getLogger(PermisoDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }    
        return "Se elimino correctamente";
    }
}

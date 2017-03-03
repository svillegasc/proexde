/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.DetallePermiso;
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
public class DetallePermisoDAO {
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarDetallePermiso(int idPermiso,int idPerfil)
    {
       try
       {  
            String sqlI="SELECT COUNT (*) CONT FROM PERFIL WHERE ID_PERFIL = '"+idPerfil+"' AND "
                        + "ESTADO = 'A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    sqlI="SELECT COUNT (*) CONT FROM PERMISO WHERE ID_PERMISO = '"+idPermiso+"' AND "
                        + "ESTADO = 'A'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sqlI);
                    rs=pr.executeQuery();
                    if(rs.next()){
                        if (rs.getInt("CONT")!= 0) {
                            String sql="INSERT INTO DETALLE_PERMISO VALUES(?,?,?)";
                            con=conex.conexion();
                            pr=con.prepareStatement(sql); 

                            pr=con.prepareStatement(sql); 
                            pr.setInt(1,idPermiso);
                            pr.setInt(2,idPerfil);
                            pr.setString(3,"A");
                            pr.executeUpdate();    
                        }else{
                            return "0 filas encontradas";
                        }
                    }
                }else{
                    return "0 filas encontradas";
                }
            }
       } 
       catch (SQLException ex) {
            Logger.getLogger(DetallePermisoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
       }
       return "Se inserto correctamente";
    }
    
    public ArrayList<DetallePermiso> consultarDetallePermisos(String sel, String cam, String val, String ord)
    {
        ArrayList<DetallePermiso> result = new ArrayList<> ();
        DetallePermiso dp = new DetallePermiso();
        
        String seleccionar = sel != null && !sel.trim().equals("") ? sel : "";
        String[] campos = cam != null && !cam.trim().equals("") ? cam.split(",") : null;
        String[] valores = val != null && !val.trim().equals("") ? val.split(",") : null;
        String orden = ord != null && !ord.trim().equals("") ? ord : "";
        
        try
        {
           con=conex.conexion();
           //String sql="SELECT ID_INSUMO,DESCRIPCION,DESCRIPCION,UNIDAD_MEDIDA FROM INSUMOS WHERE ESTADO=0";
           StringBuilder sql = new StringBuilder();
           
           
           if( !seleccionar.equals("") ){
               sql.append("SELECT ");
               sql.append(seleccionar);
               sql.append(" FROM DETALLE_PERMISO ");
           }else{
               dp.setIdPermiso(-1);
               dp.setIdPerfil(-1);
               dp.setEstado("ERROR: Faltan los campos a seleccionar en la consulta.");
               result.add(dp);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if (campos[i].equals("DETALLE_PERMISO") || campos[i].equals("ESTADO")){
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
                    dp.setIdPermiso(-1);
                    dp.setIdPerfil(-1);
                    dp.setEstado("ERROR: Faltan los campos a seleccionar en la consulta.");
                    result.add(dp);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                dp.setIdPermiso(-1);
                dp.setIdPerfil(-1);
                dp.setEstado("ERROR: Faltan los campos a seleccionar en la consulta.");
                result.add(dp);
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
               DetallePermiso dper = new DetallePermiso();
               
               if(select.length == 1 && select[0].trim().equals("*")){
                   dper.setIdPermiso(rs.getInt("ID_PERMISO"));
                   dper.setIdPerfil(rs.getInt("ID_PERFIL"));
                   dper.setEstado(rs.getString("ESTADO"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if(select[j].toUpperCase().equals("ID_PERMISO")){
                            dper.setIdPermiso(rs.getInt("ID_PERMISO"));
                        }  
                        if(select[j].toUpperCase().equals("ID_PERFIL")){
                            dper.setIdPerfil(rs.getInt("ID_PERFIL"));
                        }
                        if(select[j].toUpperCase().equals("ESTADO")){
                            dper.setEstado(rs.getString("ESTADO"));
                        }
                    }
               }
               result.add(dper);            
           }
        }
        catch(Exception ex){
            Logger.getLogger(DetallePermisoDAO.class.getName()).log(Level.SEVERE, null, ex);
            dp.setIdPermiso(-1);
            dp.setIdPerfil(-1);    
            dp.setEstado(ex.getMessage());
            result.add(dp);
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
    
    public ArrayList<DetallePermiso> consultarDetallePermiso(int idPermiso,int idPerfil)
    {
        String sql="SELECT "
                   + "FROM DETALLE_PERMISO WHERE ID_PERMISO='"+idPermiso+"' AND ID_PERFIL='"+idPerfil+"' AND ESTADO='A'";
        ArrayList<DetallePermiso> result = new ArrayList<>();
        DetallePermiso dp = new DetallePermiso();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               dp.setIdPermiso(idPermiso);
               dp.setIdPerfil(idPerfil);
               result.add(dp);
               
           }
        }
        catch(Exception ex)
       {Logger.getLogger(DetallePermisoDAO.class.getName()).log(Level.SEVERE, null, ex);}
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
    
    public String eliminarDetallePermiso(int idPermiso, int idPerfil)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM DETALLE_PERMISO WHERE ID_PERMISO = '"+idPermiso+"' AND "
                        + "ID_PERFIL = '"+idPerfil+"'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="DELETE FROM DETALLE_PERMISO WHERE ID_PERMISO = '"+idPermiso+"' "
                                + "AND ID_PERFIL = '"+idPerfil+"'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
            Logger.getLogger(DetallePermisoDAO.class.getName()).log(Level.SEVERE, null, ex);
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

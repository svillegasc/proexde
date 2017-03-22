/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.TipoIdentificacion;
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
public class TipoIdentificacionDAO {
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarTipoIdentificacion(/*int tipoIdentificacion,*/String descripcion)
    {
        String sql="INSERT INTO TIPO_IDENTIFICACION VALUES(?,?,?)";
        try
        {
            if( descripcion == null || "".trim().equals(descripcion)){
                return "Falta la descripción del tipo de identificación.";
            }
            
            String sqlI="SELECT TIDENT.NEXTVAL FROM DUAL";
            int tipoIdentificacion = 0;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                tipoIdentificacion = rs.getInt("NEXTVAL");
            }
            
            con=conex.conexion();
            pr=con.prepareStatement(sql);

            pr=con.prepareStatement(sql);
            pr.setInt(1,tipoIdentificacion);
            pr.setString(2,descripcion);
            pr.setString(3,"A");
            pr.executeUpdate();
        }
        catch (SQLException ex) {
            Logger.getLogger(TipoIdentificacionDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
        return "Se inserto correctamente";
    }
    
    public ArrayList<TipoIdentificacion> consultarTipoIdentificaciones(String sel, String cam, String val, String ord)
    {
        ArrayList<TipoIdentificacion> result = new ArrayList<> ();
        TipoIdentificacion ti = new TipoIdentificacion();
        
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
               sql.append(" FROM TIPO_IDENTIFICACION ");
           }else{
               ti.setTipoIdentificacion(-1);
               ti.setDescripcion("ERROR: Faltan los campos a seleccionar en la consulta.");
               result.add(ti);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if ("TIPO_IDENTIFICACION".equals(campos[i]) || "ESTADO".equals(campos[i])){
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
                    ti.setTipoIdentificacion(-1);
                    ti.setDescripcion("ERROR: Los campos y los valores no tienen el mismo número de argumentos.");
                    result.add(ti);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                ti.setTipoIdentificacion(-1);
                ti.setDescripcion("ERROR: Los campos y los valores no tienen el mismo número de argumentos.");
                result.add(ti);
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
               TipoIdentificacion p = new TipoIdentificacion();
               
               if(select.length == 1 && "*".trim().equals(select[0])){
                   p.setTipoIdentificacion(rs.getInt("TIPO_IDENTIFICACION"));
                   p.setDescripcion(rs.getString("DESCRIPCION"));
                   p.setEstado(rs.getString("ESTADO"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if("TIPO_IDENTIFICACION".equalsIgnoreCase(select[j])){
                            p.setTipoIdentificacion(rs.getInt("TIPO_IDENTIFICACION"));
                        }  
                        if("DESCRIPCION".equalsIgnoreCase(select[j])){
                            p.setDescripcion(rs.getString("DESCRIPCION"));
                        }
                        if("ESTADO".equalsIgnoreCase(select[j])){
                            p.setEstado(rs.getString("ESTADO"));
                        }
                    }
               }
               result.add(p);            
           }
        }
        catch(SQLException ex){
            Logger.getLogger(TipoIdentificacionDAO.class.getName()).log(Level.SEVERE, null, ex);
            ti.setTipoIdentificacion(-1);
            ti.setDescripcion(ex.getMessage());
            result.add(ti);
            return result;
        }
       finally{
           try{
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){Logger.getLogger(TipoIdentificacionDAO.class.getName()).log(Level.SEVERE, null, ex);}
       }
        return result;
    }
    
    public ArrayList<TipoIdentificacion> consultarTipoIdentificacion(int tipoIdentificacion)
    {
        String sql="SELECT DESCRIPCION "
                   + "FROM TIPO_IDENTIFICACION WHERE TIPO_IDENTIFICACION='"+tipoIdentificacion+"' AND ESTADO='A'";
        ArrayList<TipoIdentificacion> result = new ArrayList<>();
        TipoIdentificacion ti = new TipoIdentificacion();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               ti.setTipoIdentificacion(tipoIdentificacion);
               ti.setDescripcion(rs.getString("DESCRIPCION"));
               result.add(ti);
               
           }
        }
        catch(SQLException ex)
       {Logger.getLogger(TipoIdentificacionDAO.class.getName()).log(Level.SEVERE, null, ex);}
       finally
       {
           try
           {
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){Logger.getLogger(TipoIdentificacionDAO.class.getName()).log(Level.SEVERE, null, ex);}
       }
        return result;
    }
    
    public String actualizarTipoIdentificacion(int tipoIdentificacion,String descripcion)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM TIPO_IDENTIFICACION "
                      + "WHERE TIPO_IDENTIFICACION = ? AND ESTADO='A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, tipoIdentificacion);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE TIPO_IDENTIFICACION SET DESCRIPCION='"+descripcion+"' "
                            + "WHERE TIPO_IDENTIFICACION = ? AND ESTADO='A'";
                    
                    if( tipoIdentificacion == -1){
                        return "Falta el tipo de identificación.";
                    }
                    if( descripcion == null || "".trim().equals(descripcion)){
                        return "Falta la descripción del tipo de identificación.";
                    }
                    
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setInt(1, tipoIdentificacion);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger(TipoIdentificacionDAO.class.getName()).log(Level.SEVERE, null, ex);
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
           catch(SQLException ex){Logger.getLogger(TipoIdentificacionDAO.class.getName()).log(Level.SEVERE, null, ex);}
       }    
        return "Se actualizo correctamente";
    }
    
    public String eliminarTipoIdentificacion(int tipoIdentificacion)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM TIPO_IDENTIFICACION "
                      + "WHERE TIPO_IDENTIFICACION = ? AND ESTADO='A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, tipoIdentificacion);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE TIPO_IDENTIFICACION SET ESTADO='I' "
                             + "WHERE TIPO_IDENTIFICACION = ?";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setInt(1, tipoIdentificacion);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger(TipoIdentificacionDAO.class.getName()).log(Level.SEVERE, null, ex);
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
           catch(SQLException ex){Logger.getLogger(TipoIdentificacionDAO.class.getName()).log(Level.SEVERE, null, ex);}
       }    
        return "Se elimino correctamente";
    }
}

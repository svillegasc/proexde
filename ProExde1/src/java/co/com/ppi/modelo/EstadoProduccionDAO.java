/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.EstadoProduccion;
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
public class EstadoProduccionDAO {
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarEstadoProduccion(/*String estadoProduccion,*/String descripcion)
    {
        String sql="INSERT INTO ESTADO_PRODUCCION VALUES(?,?)";
        try
        {
            if( descripcion == null || "".trim().equals(descripcion)){
                return "Falta la descripción del estado.";
            }
            String sqlI="SELECT EPRODUC.NEXTVAL FROM DUAL";
            int estadoProduccion = 0;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                estadoProduccion = rs.getInt("NEXTVAL");
            }
            con=conex.conexion();
            pr=con.prepareStatement(sql);

            pr=con.prepareStatement(sql);
            pr.setInt(1,estadoProduccion);
            pr.setString(2,descripcion);
            pr.executeUpdate();
        }
        catch (SQLException ex) {
            Logger.getLogger(EstadoProduccionDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
        return "Se inserto correctamente";
    }
    
    public ArrayList<EstadoProduccion> consultarEstadoProducciones(String sel, String cam, String val, String ord)
    {
        ArrayList<EstadoProduccion> result = new ArrayList<> ();
        EstadoProduccion ep = new EstadoProduccion();
        
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
               sql.append(" FROM ESTADO_PRODUCCION ");
           }else{
               ep.setEstadoProduccion(-1);
               ep.setDescripcion("ERROR: Faltan los campos a seleccionar en la consulta.");
               result.add(ep);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if ("ESTADO_PRODUCCION".equals(campos[i]) || "ESTADO".equals(campos[i])){
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
                    ep.setEstadoProduccion(-1);
                    ep.setDescripcion("ERROR: Los campos y los valores no tienen el mismo número de argumentos.");
                    result.add(ep);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                ep.setEstadoProduccion(-1);
                ep.setDescripcion("ERROR: Los campos y los valores no tienen el mismo número de argumentos.");
                result.add(ep);
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
               EstadoProduccion p = new EstadoProduccion();
               
               if(select.length == 1 && "*".trim().equals(select[0])){
                   p.setEstadoProduccion(rs.getInt("ESTADO_PRODUCCION"));
                   p.setDescripcion(rs.getString("DESCRIPCION"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if("ESTADO_PRODUCCION".equalsIgnoreCase(select[j])){
                            p.setEstadoProduccion(rs.getInt("ESTADO_PRODUCCION"));
                        }  
                        if("DESCRIPCION".equalsIgnoreCase(select[j])){
                            p.setDescripcion(rs.getString("DESCRIPCION"));
                        }
                    }
               }
               result.add(p);            
           }
        }
        catch(SQLException ex){
            Logger.getLogger(EstadoProduccionDAO.class.getName()).log(Level.SEVERE, null, ex);
            ep.setEstadoProduccion(-1);
            ep.setDescripcion(ex.getMessage());
            result.add(ep);
            return result;
        }
       finally{
           try{
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(EstadoProduccionDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public ArrayList<EstadoProduccion> consultarEstadoProduccion(int estadoProduccion)
    {
        String sql="SELECT DESCRIPCION "
                 + "FROM ESTADO_PRODUCCION WHERE ESTADO_PRODUCCION='"+estadoProduccion+"'";
        ArrayList<EstadoProduccion> result = new ArrayList<>();
        EstadoProduccion ep = new EstadoProduccion();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               ep.setEstadoProduccion(estadoProduccion);
               ep.setDescripcion(rs.getString("DESCRIPCION"));
               result.add(ep);
               
           }
        }
        catch(SQLException ex)
       {Logger.getLogger(EstadoProduccionDAO.class.getName()).log(Level.SEVERE, null, ex);}
       finally
       {
           try
           {
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(EstadoProduccionDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public String actualizarEstadoProduccion(int estadoProduccion,String descripcion)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM ESTADO_PRODUCCION WHERE ESTADO_PRODUCCION = ?";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, estadoProduccion);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE ESTADO_PRODUCCION SET DESCRIPCION=? "
                            + "WHERE ESTADO_PRODUCCION = ?";
                    
                    if( estadoProduccion == -1){
                        return "Falta el estado de producción.";
                    }else
                    if( descripcion == null || "".trim().equals(descripcion)){
                        return "Falta la descripción del estado.";
                    }
                    
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setString(1, descripcion);
                    pr.setInt(2, estadoProduccion);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger(EstadoProduccionDAO.class.getName()).log(Level.SEVERE, null, ex);
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
               Logger.getLogger(EstadoProduccionDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }    
        return "Se actualizo correctamente";
    }
    
    public String eliminarEstadoProduccion(int estadoProduccion)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM ESTADO_PRODUCCION "
                    + "WHERE ESTADO_PRODUCCION = ?";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, estadoProduccion);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="DELETE FROM ESTADO_PRODUCCION WHERE ESTADO_PRODUCCION = ?";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setInt(1, estadoProduccion);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger(EstadoProduccionDAO.class.getName()).log(Level.SEVERE, null, ex);
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
               Logger.getLogger(EstadoProduccionDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }    
        return "Se elimino correctamente";
    }   
}

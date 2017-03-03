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
            if( descripcion == null || descripcion.trim().equals("")){
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
                            if (campos[i].equals("ESTADO_PRODUCCION") || campos[i].equals("ESTADO")){
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
               
               if(select.length == 1 && select[0].trim().equals("*")){
                   p.setEstadoProduccion(rs.getInt("ESTADO_PRODUCCION"));
                   p.setDescripcion(rs.getString("DESCRIPCION"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if(select[j].toUpperCase().equals("ESTADO_PRODUCCION")){
                            p.setEstadoProduccion(rs.getInt("ESTADO_PRODUCCION"));
                        }  
                        if(select[j].toUpperCase().equals("DESCRIPCION")){
                            p.setDescripcion(rs.getString("DESCRIPCION"));
                        }
                    }
               }
               result.add(p);            
           }
        }
        catch(Exception ex){
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
           catch(Exception ex){}
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
        catch(Exception ex)
       {Logger.getLogger(EstadoProduccionDAO.class.getName()).log(Level.SEVERE, null, ex);}
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
    
    public String actualizarEstadoProduccion(int estadoProduccion,String descripcion)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM ESTADO_PRODUCCION WHERE ESTADO_PRODUCCION = '"+estadoProduccion+"'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE ESTADO_PRODUCCION SET DESCRIPCION='"+descripcion+"' "
                            + "WHERE ESTADO_PRODUCCION = '"+estadoProduccion+"'";
                    
                    if( estadoProduccion == -1){
                        return "Falta el estado de producción.";
                    }else
                    if( descripcion == null || descripcion.trim().equals("")){
                        return "Falta la descripción del estado.";
                    }
                    
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
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
           catch(Exception ex){}
       }    
        return "Se actualizo correctamente";
    }
    
    public String eliminarEstadoProduccion(int estadoProduccion)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM ESTADO_PRODUCCION "
                    + "WHERE ESTADO_PRODUCCION = '"+estadoProduccion+"'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="DELETE FROM ESTADO_PRODUCCION WHERE ESTADO_PRODUCCION = '"+estadoProduccion+"'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
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
           catch(Exception ex){}
       }    
        return "Se elimino correctamente";
    }   
}

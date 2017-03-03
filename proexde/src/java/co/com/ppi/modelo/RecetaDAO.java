/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.Receta;
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
public class RecetaDAO {
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarReceta(int idProducto,int idInsumo,int cantidadUtilizada)
    {
       String sql="INSERT INTO RECETA VALUES(?,?,?)";
       try
       {   
            if( cantidadUtilizada == -1){
                return "Falta la cantidad a utilizar";
            }
            con=conex.conexion();
            pr=con.prepareStatement(sql); 

            pr=con.prepareStatement(sql); 
            pr.setInt(1,idProducto);
            pr.setInt(2,idInsumo);
            pr.setInt(3,cantidadUtilizada);
            pr.executeUpdate();    
       } 
       catch (SQLException ex) {
            Logger.getLogger(RecetaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
       }
       return "Se inserto correctamente";
    }
    
    public ArrayList<Receta> consultarRecetas(String sel, String cam, String val, String ord)
    {
        ArrayList<Receta> result = new ArrayList<> ();
        Receta receta = new Receta();
        
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
               sql.append(" FROM RECETA ");
           }else{
               receta.setIdProducto(-1);
               receta.setIdInsumo(-1);
               receta.setCantidadUtilizada(-1);
               result.add(receta);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if (campos[i].equals("ID_RECETA") || campos[i].equals("ESTADO")){
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
                    receta.setIdProducto(-1);
                    receta.setIdInsumo(-1);
                    receta.setCantidadUtilizada(-1);
                    result.add(receta);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                receta.setIdProducto(-1);
                receta.setIdInsumo(-1);
                receta.setCantidadUtilizada(-1);
                result.add(receta);
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
               Receta r = new Receta();
               
               if(select.length == 1 && select[0].trim().equals("*")){
                   r.setIdProducto(rs.getInt("ID_PRODUCTO"));
                   r.setIdInsumo(rs.getInt("ID_INSUMO"));
                   r.setCantidadUtilizada(rs.getInt("CANTIDAD_UTILIZADA"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if(select[j].toUpperCase().equals("ID_PRODUCTO")){
                            r.setIdProducto(rs.getInt("ID_PRODUCTO"));
                        }  
                        if(select[j].toUpperCase().equals("ID_INSUMO")){
                            r.setIdInsumo(rs.getInt("ID_INSUMO"));
                        }
                        if(select[j].toUpperCase().equals("CANTIDAD_UTILIZADA")){
                            r.setCantidadUtilizada(rs.getInt("CANTIDAD_UTILIZADA"));
                        }
                    }
               }
               result.add(r);            
           }
        }
        catch(Exception ex){
            receta.setIdProducto(-1);
            receta.setIdInsumo(-1);
            receta.setCantidadUtilizada(-1);
            result.add(receta);
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
    
    public ArrayList<Receta> consultarReceta(int idProducto, int idInsumo)
    {
        String sql="SELECT CANTIDAD_UTILIZADA "
                   + "FROM PROVEEDOR WHERE ID_PRODUCTO='"+idProducto+"' AND ID_INSUMO='"+idInsumo+"'";
        ArrayList<Receta> result = new ArrayList<>();
        Receta r = new Receta();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               r.setIdProducto(idProducto);
               r.setIdInsumo(idInsumo);
               r.setCantidadUtilizada(rs.getInt("CANTIDAD_UTILIZADA"));
               result.add(r);
               
           }
        }
        catch(Exception ex)
       {Logger.getLogger(RecetaDAO.class.getName()).log(Level.SEVERE, null, ex);}
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
    
    public String actualizarReceta(int idProducto,int idInsumo,int cantidadUtilizada)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM RECETA WHERE ID_PRODUCTO = '"+idProducto+"' "
                    + "AND ID_INSUMO='"+idInsumo+"' ";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE RECETA SET CANTIDAD_UTILIZADA='"+cantidadUtilizada+"'"
                            + "WHERE ID_PRODUCTO = '"+idProducto+"' "
                            + "AND ID_INSUMO='"+idInsumo+"' ";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
            Logger.getLogger(RecetaDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public String eliminarReceta(int idProducto,int idInsumo)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM RECETA WHERE ID_PRODUCTO = '"+idProducto+"' "
                    + "AND ID_INSUMO='"+idInsumo+"' ";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="DELETE FROM RECETA WHERE ID_PRODUCTO = '"+idProducto+"' "
                                + "AND ID_INSUMO = '"+idInsumo+"'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
            Logger.getLogger(RecetaDAO.class.getName()).log(Level.SEVERE, null, ex);
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

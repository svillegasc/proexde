/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.ProveedorInsumo;
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
public class ProveedorInsumoDAO {
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarProveedorInsumo(int idProveedor,int idInsumo)
    {
       try
       {  
            String sqlI="SELECT COUNT (*) CONT FROM PROVEEDOR WHERE ID_PROVEEDOR= ? AND "
                        + "ESTADO = 'A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, idProveedor);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    sqlI="SELECT COUNT (*) CONT FROM INSUMO WHERE ID_INSUMO = ? AND "
                        + "ESTADO = 'A'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sqlI);
                    pr.setInt(1, idInsumo);
                    rs=pr.executeQuery();
                    if(rs.next()){
                        if (rs.getInt("CONT")!= 0) {
                            String sql="INSERT INTO PROVEEDOR_INSUMO VALUES(?,?)";
                            con=conex.conexion();
                            pr=con.prepareStatement(sql); 

                            pr=con.prepareStatement(sql); 
                            pr.setInt(1,idProveedor);
                            pr.setInt(2,idInsumo);
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
            Logger.getLogger(ProveedorInsumoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
       }
       return "Se inserto correctamente";
    }
    
    public ArrayList<ProveedorInsumo> consultarProveedorInsumos(String sel, String cam, String val, String ord)
    {
        ArrayList<ProveedorInsumo> result = new ArrayList<> ();
        ProveedorInsumo prIn = new ProveedorInsumo();
        
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
               sql.append(" FROM PROVEEDOR_INSUMO ");
           }else{
               prIn.setIdProveedor(-1);
               prIn.setIdInsumo(-1);
               result.add(prIn);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if ("PROVEEDOR_INSUMO".equals(campos[i]) || "ESTADO".equals(campos[i])){
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
                    prIn.setIdProveedor(-1);
                    prIn.setIdInsumo(-1);
                    result.add(prIn);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                prIn.setIdProveedor(-1);
                prIn.setIdInsumo(-1);
                result.add(prIn);
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
               ProveedorInsumo pi = new ProveedorInsumo();
               
               if(select.length == 1 && "*".trim().equals(select[0])){
                   pi.setIdProveedor(rs.getInt("ID_PROVEEDOR"));
                   pi.setIdInsumo(rs.getInt("ID_INSUMO"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if("ID_PROVEEDOR".equalsIgnoreCase(select[j])){
                            pi.setIdProveedor(rs.getInt("ID_PROVEEDOR"));
                        }  
                        if("ID_INSUMO".equalsIgnoreCase(select[j])){
                            pi.setIdInsumo(rs.getInt("ID_INSUMO"));
                        }
                    }
               }
               result.add(pi);            
           }
        }
        catch(SQLException ex){
            Logger.getLogger(ProveedorInsumoDAO.class.getName()).log(Level.SEVERE, null, ex);
            prIn.setIdProveedor(-1);
            prIn.setIdInsumo(-1);
            result.add(prIn);
            return result;
        }
       finally{
           try{
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(ProveedorInsumoDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public ArrayList<ProveedorInsumo> consultarProveedorInsumo(int idProveedor,int idInsumo)
    {
        String sql="SELECT FROM PROVEEDOR_INSUMO WHERE ID_PROVEEDOR='"+idProveedor+"' "
                + "AND ID_INSUMO='"+idInsumo+"'";
        ArrayList<ProveedorInsumo> result = new ArrayList<>();
        ProveedorInsumo pi = new ProveedorInsumo();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               pi.setIdProveedor(idProveedor);
               pi.setIdInsumo(idInsumo);
               result.add(pi);
               
           }
        }
        catch(SQLException ex)
       {Logger.getLogger(ProveedorInsumoDAO.class.getName()).log(Level.SEVERE, null, ex);}
       finally
       {
           try
           {
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(ProveedorInsumoDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public String eliminarProveedorInsumo(int idProveedor, int idInsumo)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM PROVEEDOR_INSUMO WHERE ID_PROVEEDOR = ? AND "
                        + "ID_INSUMO = ?";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1,idProveedor);
            pr.setInt(2, idInsumo);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="DELETE FROM PROVEEDOR_INSUMO WHERE ID_PROVEEDOR = ? "
                                + "AND ID_INSUMO = ?";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setInt(1,idProveedor);
                    pr.setInt(2, idInsumo);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger(ProveedorInsumoDAO.class.getName()).log(Level.SEVERE, null, ex);
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
               Logger.getLogger(ProveedorInsumoDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }    
        return "Se elimino correctamente";
    }
}

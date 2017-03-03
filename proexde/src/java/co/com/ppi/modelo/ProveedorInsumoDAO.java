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
            String sqlI="SELECT COUNT (*) CONT FROM PROVEEDOR WHERE ID_PROVEEDOR= '"+idProveedor+"' AND "
                        + "ESTADO = 'A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    sqlI="SELECT COUNT (*) CONT FROM INSUMO WHERE ID_INSUMO = '"+idInsumo+"' AND "
                        + "ESTADO = 'A'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sqlI);
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
               sql.append(" FROM PROVEEDOR_INSUMO ");
           }else{
               prIn.setIdProveedor(-1);
               prIn.setIdInsumo(-1);
//               dp.setEstado("ERROR: Faltan los campos a seleccionar en la consulta.");
               result.add(prIn);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if (campos[i].equals("PROVEEDOR_INSUMO") || campos[i].equals("ESTADO")){
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
     //               dp.setEstado("ERROR: Faltan los campos a seleccionar en la consulta.");
                    result.add(prIn);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                prIn.setIdProveedor(-1);
                prIn.setIdInsumo(-1);
 //               dp.setEstado("ERROR: Faltan los campos a seleccionar en la consulta.");
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
               
               if(select.length == 1 && select[0].trim().equals("*")){
                   pi.setIdProveedor(rs.getInt("ID_PROVEEDOR"));
                   pi.setIdInsumo(rs.getInt("ID_INSUMO"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if(select[j].toUpperCase().equals("ID_PROVEEDOR")){
                            pi.setIdProveedor(rs.getInt("ID_PROVEEDOR"));
                        }  
                        if(select[j].toUpperCase().equals("ID_INSUMO")){
                            pi.setIdInsumo(rs.getInt("ID_INSUMO"));
                        }
                    }
               }
               result.add(pi);            
           }
        }
        catch(Exception ex){
            Logger.getLogger(ProveedorInsumoDAO.class.getName()).log(Level.SEVERE, null, ex);
            prIn.setIdProveedor(-1);
            prIn.setIdInsumo(-1);
        //               dp.setEstado("ERROR: Faltan los campos a seleccionar en la consulta.");
            result.add(prIn);
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
        catch(Exception ex)
       {Logger.getLogger(ProveedorInsumoDAO.class.getName()).log(Level.SEVERE, null, ex);}
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
    
    public String eliminarProveedorInsumo(int idProveedor, int idInsumo)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM PROVEEDOR_INSUMO WHERE ID_PROVEEDOR = '"+idProveedor+"' AND "
                        + "ID_INSUMO = '"+idInsumo+"'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="DELETE FROM PROVEEDOR_INSUMO WHERE ID_PROVEEDOR = '"+idProveedor+"' "
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
           catch(Exception ex){}
       }    
        return "Se elimino correctamente";
    }
}

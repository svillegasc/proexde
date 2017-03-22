/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.DetallePedidoProveedor;
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
public class DetallePedidoProveedorDAO {
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarDetallePedidoProveedor(int idPedidoProveedor,int idInsumo, int cantidad, int precio)
    {
       try
       {  
            String sql="INSERT INTO DETALLE_PEDIDO_PROVEEDOR VALUES(?,?,?,?)";
            con=conex.conexion();
            pr=con.prepareStatement(sql); 

            pr=con.prepareStatement(sql); 
            pr.setInt(1,idPedidoProveedor);
            pr.setInt(2,idInsumo);
            pr.setInt(3,cantidad);
            pr.setInt(4,precio);
            pr.executeUpdate();
       } 
       catch (SQLException ex) {
            Logger.getLogger(DetallePedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
       }
       return "Se inserto correctamente";
    }
    
    public ArrayList<DetallePedidoProveedor> consultarDetallePedidoProveedores(String sel, String cam, String val, String ord)
    {
        ArrayList<DetallePedidoProveedor> result = new ArrayList<> ();
        DetallePedidoProveedor dpp = new DetallePedidoProveedor();
        
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
               sql.append(" FROM DETALLE_PEDIDO_PROVEEDOR ");
           }else{
               dpp.setIdPedidoProveedor(-1);
               dpp.setIdInsumo(-1);
               dpp.setCantidad(-1);
               dpp.setPrecio(-1);
               result.add(dpp);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if ("DETALLE_PEDIDO_PROVEEDOR".equals(campos[i]) || "ESTADO".equals(campos[i])){
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
                    dpp.setIdPedidoProveedor(-1);
                    dpp.setIdInsumo(-1);
                    dpp.setCantidad(-1);
                    dpp.setPrecio(-1);
                    result.add(dpp);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                dpp.setIdPedidoProveedor(-1);
                dpp.setIdInsumo(-1);
                dpp.setCantidad(-1);
                dpp.setPrecio(-1);
                result.add(dpp);
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
               DetallePedidoProveedor dPedProv = new DetallePedidoProveedor();
               
               if(select.length == 1 && "*".trim().equals(select[0])){
                   dPedProv.setIdPedidoProveedor(rs.getInt("ID_PEDIDO_PROVEEDOR"));
                   dPedProv.setIdInsumo(rs.getInt("ID_INSUMO"));
                   dPedProv.setCantidad(rs.getInt("CANTIDAD"));
                   dPedProv.setPrecio(rs.getInt("PRECIO"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if("ID_PEDIDO_PROVEEDOR".toUpperCase().equals(select[j])){
                            dPedProv.setIdPedidoProveedor(rs.getInt("ID_PEDIDO_PROVEEDOR"));
                        }  
                        if("ID_INSUMO".equalsIgnoreCase((select[j]))){
                            dPedProv.setIdInsumo(rs.getInt("ID_INSUMO"));
                        }
                        if("CANTIDAD".equalsIgnoreCase((select[j]))){
                            dPedProv.setCantidad(rs.getInt("CANTIDAD"));
                        }
                        if("PRECIO".equalsIgnoreCase((select[j]))){
                            dPedProv.setPrecio(rs.getInt("PRECIO"));
                        }
                    }
               }
               result.add(dPedProv);            
           }
        }
        catch(SQLException ex){
            Logger.getLogger(DetallePedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
            dpp.setIdPedidoProveedor(-1);
            dpp.setIdInsumo(-1);
            dpp.setCantidad(-1);
            dpp.setPrecio(-1);
            result.add(dpp);
            return result;
        }
       finally{
           try{
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(DetallePedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public ArrayList<DetallePedidoProveedor> consultarDetallePedidoProveedor(int idPedidoProveedor,int idInsumo)
    {
        String sql="SELECT CANTIDAD_PEDIDA "
                   + "FROM DETALLE_PEDIDO_PROVEEDOR WHERE ID_PEDIDO_PROVEEDOR='"+idPedidoProveedor+"' "
                   + "AND ID_INSUMO='"+idInsumo+"'";
        ArrayList<DetallePedidoProveedor> result = new ArrayList<>();
        DetallePedidoProveedor dpp = new DetallePedidoProveedor();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               dpp.setIdPedidoProveedor(idPedidoProveedor);
               dpp.setIdInsumo(idInsumo);
               dpp.setCantidad(rs.getInt("CANTIDAD"));
               dpp.setPrecio(rs.getInt("PRECIO"));
               result.add(dpp);
               
           }
        }
        catch(Exception ex)
       {Logger.getLogger(DetallePedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);}
       finally
       {
           try
           {
               rs.close();
               pr.close();
               con.close();
           }
           catch(Exception ex){
               Logger.getLogger(DetallePedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public String actualizarDetallePedidoProveedor(int idPedidoProveedor,int idInsumo,int cantidad,int precio)
    {
        int cantidadPed = 0;
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM DETALLE_PEDIDO_PROVEEDOR WHERE "
                    + "ID_PEDIDO_PROVEEDOR = ? AND ID_INSUMO= ? ";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1,idPedidoProveedor);
            pr.setInt(2,idInsumo);
            rs=pr.executeQuery();
            
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE DETALLE_PEDIDO_PROVEEDOR SET CANTIDAD='"+cantidad+"', PRECIO='"+precio+"' "
                            + "WHERE ID_PEDIDO_PROVEEDOR = ? AND ID_INSUMO= ?";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setInt(1,idPedidoProveedor);
                    pr.setInt(2,idInsumo);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger(DetallePedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
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
               Logger.getLogger(DetallePedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }    
        return "Se actualizo correctamente";
    }
}

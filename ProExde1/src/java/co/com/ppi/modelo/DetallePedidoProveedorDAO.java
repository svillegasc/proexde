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
               sql.append(" FROM DETALLE_PEDIDO_PROVEEDOR ");
           }else{
               dpp.setIdPedidoProveedor(-1);
               dpp.setIdInsumo(-1);
               dpp.setCantidad(-1);
               dpp.setPrecio(-1);
//               dcc.setEstado("ERROR: Faltan los campos a seleccionar en la consulta.");
               result.add(dpp);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if (campos[i].equals("DETALLE_PEDIDO_PROVEEDOR") || campos[i].equals("ESTADO")){
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
     //               dcc.setEstado("ERROR: Faltan los campos a seleccionar en la consulta.");
                    result.add(dpp);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                dpp.setIdPedidoProveedor(-1);
                dpp.setIdInsumo(-1);
                dpp.setCantidad(-1);
                dpp.setPrecio(-1);
 //               dcc.setEstado("ERROR: Faltan los campos a seleccionar en la consulta.");
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
               
               if(select.length == 1 && select[0].trim().equals("*")){
                   dPedProv.setIdPedidoProveedor(rs.getInt("ID_PEDIDO_PROVEEDOR"));
                   dPedProv.setIdInsumo(rs.getInt("ID_INSUMO"));
                   dPedProv.setCantidad(rs.getInt("CANTIDAD"));
                   dPedProv.setPrecio(rs.getInt("PRECIO"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if(select[j].toUpperCase().equals("ID_PEDIDO_PROVEEDOR")){
                            dPedProv.setIdPedidoProveedor(rs.getInt("ID_PEDIDO_PROVEEDOR"));
                        }  
                        if(select[j].toUpperCase().equals("ID_INSUMO")){
                            dPedProv.setIdInsumo(rs.getInt("ID_INSUMO"));
                        }
                        if(select[j].toUpperCase().equals("CANTIDAD")){
                            dPedProv.setCantidad(rs.getInt("CANTIDAD"));
                        }
                        if(select[j].toUpperCase().equals("PRECIO")){
                            dPedProv.setPrecio(rs.getInt("PRECIO"));
                        }
                    }
               }
               result.add(dPedProv);            
           }
        }
        catch(Exception ex){
            Logger.getLogger(DetallePedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
            dpp.setIdPedidoProveedor(-1);
            dpp.setIdInsumo(-1);
            dpp.setCantidad(-1);
            dpp.setPrecio(-1);
//               dcc.setEstado("ERROR: Faltan los campos a seleccionar en la consulta.");
            result.add(dpp);
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
           catch(Exception ex){}
       }
        return result;
    }
    
    public String actualizarDetallePedidoProveedor(int idPedidoProveedor,int idInsumo,int cantidad,int precio)
    {
        int cantidadPed = 0;
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM DETALLE_PEDIDO_PROVEEDOR WHERE "
                    + "ID_PEDIDO_PROVEEDOR = '"+idPedidoProveedor+"' AND ID_INSUMO='"+idInsumo+"'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE DETALLE_PEDIDO_PROVEEDOR SET CANTIDAD='"+cantidad+"', PRECIO='"+precio+"' "
                            + "WHERE ID_PEDIDO_PROVEEDOR = '"+idPedidoProveedor+"' AND ID_INSUMO='"+idInsumo+"'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
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
           catch(Exception ex){}
       }    
        return "Se actualizo correctamente";
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.DetallePedidoCliente;
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
public class DetallePedidoClienteDAO {
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarDetallePedidoCliente(int idPedido,int idProducto, int cantidadPedida)
    {
       try
       {  
            String sql="INSERT INTO DETALLE_PEDIDO_CLIENTE VALUES(?,?,?)";
            con=conex.conexion(); 

            pr=con.prepareStatement(sql); 
            pr.setInt(1,idPedido);
            pr.setInt(2,idProducto);
            pr.setInt(3,cantidadPedida);
            pr.executeUpdate();
            valorTotalPedido(idPedido,idProducto);
       } 
       catch (SQLException ex) {
            Logger.getLogger(DetallePedidoClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
       }
       return "Se inserto correctamente";
    }
    
    public ArrayList<DetallePedidoCliente> consultarDetallePedidoClientes(String sel, String cam, String val, String ord)
    {
        ArrayList<DetallePedidoCliente> result = new ArrayList<> ();
        DetallePedidoCliente dcc = new DetallePedidoCliente();
        
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
               sql.append(" FROM DETALLE_PEDIDO_CLIENTE ");
           }else{
               dcc.setIdPedido(-1);
               dcc.setIdProducto(-1);
               result.add(dcc);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if ("DETALLE_PEDIDO_CLIENTE".equals(campos[i]) || "ESTADO".equals(campos[i])){
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
                    dcc.setIdPedido(-1);
                    dcc.setIdProducto(-1);
                    result.add(dcc);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                dcc.setIdPedido(-1);
                dcc.setIdProducto(-1);
                result.add(dcc);
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
               DetallePedidoCliente dCotCl = new DetallePedidoCliente();
               
               if(select.length == 1 && "*".trim().equals(select[0])){
                   dCotCl.setIdPedido(rs.getInt("ID_PEDIDO"));
                   dCotCl.setIdProducto(rs.getInt("ID_PRODUCTO"));
                   dCotCl.setCantidadPedida(rs.getInt("CANTIDAD_PEDIDA"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if("ID_PEDIDO".equalsIgnoreCase((select[j]))){
                            dCotCl.setIdPedido(rs.getInt("ID_PEDIDO"));
                        }  
                        if("ID_PRODUCTO".equalsIgnoreCase((select[j]))){
                            dCotCl.setIdProducto(rs.getInt("ID_PRODUCTO"));
                        }
                        if("CANTIDAD_PEDIDA".equalsIgnoreCase((select[j]))){
                            dCotCl.setCantidadPedida(rs.getInt("CANTIDAD_PEDIDA"));
                        }
                    }
               }
               result.add(dCotCl);            
           }
        }
        catch(SQLException ex){
            Logger.getLogger(DetallePedidoClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            dcc.setIdPedido(-1);
            dcc.setIdProducto(-1);
            result.add(dcc);
            return result;
        }
       finally{
           try{
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(DetallePedidoClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public ArrayList<DetallePedidoCliente> consultarDetallePedidoCliente(int idPedido,int idProducto)
    {
        String sql="SELECT CANTIDAD_PEDIDA "
                   + "FROM DETALLE_PEDIDO_CLIENTE WHERE ID_PEDIDO='"+idPedido+"' AND ID_PRODUCTO='"+idProducto+"'";
        ArrayList<DetallePedidoCliente> result = new ArrayList<>();
        DetallePedidoCliente dpc = new DetallePedidoCliente();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               dpc.setIdPedido(idPedido);
               dpc.setIdProducto(idProducto);
               dpc.setCantidadPedida(rs.getInt("CANTIDAD_PEDIDA"));
               result.add(dpc);
               
           }
        }
        catch(SQLException ex)
       {Logger.getLogger(DetallePedidoClienteDAO.class.getName()).log(Level.SEVERE, null, ex);}
       finally
       {
           try
           {
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(DetallePedidoClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public String actualizarDetallePedidoCliente(int idPedido,int idProducto,int cantidadPedida)
    {
        int cantidadPed = 0;
        String sqlI="SELECT COUNT (*) CONT FROM DETALLE_PEDIDO_CLIENTE WHERE "
                    + "ID_PEDIDO = ? AND ID_PRODUCTO= ? ";
        
        String sql1="SELECT CANTIDAD_PEDIDA FROM DETALLE_PEDIDO_CLIENTE WHERE "
                    + "ID_PEDIDO = ? AND ID_PRODUCTO = ? ";
        try
        {
            
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, idPedido);
            pr.setInt(2, idProducto);
            rs=pr.executeQuery();
            
            
            con=conex.conexion();
            pr=con.prepareStatement(sql1);
            pr.setInt(1, idPedido);
            pr.setInt(2, idProducto);
            rs=pr.executeQuery();
            
            while ( rs.next() )
            {
                cantidadPed = rs.getInt("CANTIDAD_PEDIDA");
            }
            
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE DETALLE_PEDIDO_CLIENTE SET CANTIDAD_PEDIDA='"+cantidadPedida+"'"
                            + "WHERE ID_PEDIDO = ? AND ID_PRODUCTO= ?";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setInt(1, idPedido);
                    pr.setInt(2, idProducto);
                    pr.executeUpdate();
                    if(cantidadPed != cantidadPedida){
                        valorTotalPedido(idPedido,idProducto);
                    }
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger(DetallePedidoClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
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
               Logger.getLogger(DetallePedidoClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }    
        return "Se actualizo correctamente";
    }
    
    public void valorTotalPedido(int idPedido, int idProducto){
        int idCotizacion = 0;
        int valorTotal = 0;
        DetalleCotizacionClienteDAO dcc = new DetalleCotizacionClienteDAO();
        try {
            String sql1="SELECT ID_COTIZACION, VALOR_TOTAL FROM PEDIDO "
                      + "WHERE ID_Pedido = ?";
            con=conex.conexion();
            pr=con.prepareStatement(sql1);
            pr.setInt(1, idPedido);
            rs=pr.executeQuery();
            if ( rs.next() )
            {
                idCotizacion = rs.getInt("ID_COTIZACION");
                valorTotal = rs.getInt("VALOR_TOTAL");
            }
            
            String sql="SELECT CANTIDAD_PEDIDA FROM DETALLE_PEDIDO_CLIENTE "
                     + "WHERE ID_PEDIDO = ? AND ID_PRODUCTO = ?";
            con=conex.conexion();
            pr=con.prepareStatement(sql);
            pr.setInt(1, idPedido);
            pr.setInt(2, idProducto);
            rs=pr.executeQuery();
            
            if ( rs.next() )
            {
                int cantidadPedida = rs.getInt("CANTIDAD_PEDIDA");
                int precio = dcc.valorCotizacion(idCotizacion,idProducto);
                valorTotal = valorTotal + (cantidadPedida*precio);
            }
            String sqlI="UPDATE PEDIDO SET VALOR_TOTAL='"+valorTotal+"'"
                    + "WHERE ID_PEDIDO = ?";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, idPedido);
            pr.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DetalleCotizacionClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

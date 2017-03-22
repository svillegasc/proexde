/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.PedidoProveedor;
import co.com.ppi.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SANTI
 */
public class PedidoProveedorDAO {
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarPedidoProveedor(/*int idPedidoProveedor,*/int idProveedor,String fechaPed,
                                          int estadoProduccion, int total)
    {
        String sql="INSERT INTO PEDIDO_PROVEEDOR VALUES(?,?,?,?,?)";
        Date fechaPedidoPP = null;
        java.sql.Date fechaPedido;
        try
        {
            if( idProveedor == -1){
                return "Falta el id del proveedor";
            }else
            if( fechaPed == null || "".equals(fechaPed)){
                return "Falta la fecha de pedido.";
            }else
            if( estadoProduccion == -1){
                return "Falta el estado del pedido.";
            }else
            if( total == -1){
                return "Falta el total";
            }

            try{
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                fechaPedidoPP = df.parse(fechaPed);
                fechaPedido = new java.sql.Date(fechaPedidoPP.getTime());
                java.util.Date fechaAct = new java.util.Date();
                String fechaActual = df.format(fechaAct);
                fechaActual.split(fechaActual, 11);
                fechaAct = df.parse(fechaActual);
                
                if ( fechaPedido.before(fechaAct)) {
                    return "La fecha de pedido no puede ser menor a la fecha actual.";
                }
                if ( fechaPedido.after(fechaAct)) {
                    return "La fecha de pedido no puede ser mayor a la fecha actual.";
                }

            }catch(ParseException e){
                return "Fecha no válida";
            }

            String sqlI="SELECT PPROVEEDOR.NEXTVAL FROM DUAL";
            int idPedidoProveedor = 0;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                idPedidoProveedor = rs.getInt("NEXTVAL");
            }

            con=conex.conexion();
            pr=con.prepareStatement(sql);

            pr=con.prepareStatement(sql);
            pr.setInt(1,idPedidoProveedor);
            pr.setInt(2,idProveedor);
            pr.setDate(3,fechaPedido);
            pr.setInt(4,estadoProduccion);
            pr.setInt(5,total);
            pr.executeUpdate();
        }
        catch (SQLException ex) {
            Logger.getLogger(PedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
        return "Se inserto correctamente";
    }
    
    public ArrayList<PedidoProveedor> consultarPedidoProveedores(String sel, String cam, String val, String ord)
    {
        ArrayList<PedidoProveedor> result = new ArrayList<> ();
        PedidoProveedor pedido_proveedor = new PedidoProveedor();
        
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
               sql.append(" FROM PEDIDO_PROVEEDOR ");
           }else{
               pedido_proveedor.setIdPedidoProveedor(-1);
               pedido_proveedor.setIdProveedor(-1);
               pedido_proveedor.setEstado_produccion(-1);
               pedido_proveedor.setTotal(-1);
               result.add(pedido_proveedor);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if ("ID_PEDIDO_PROVEEDOR".equals(campos[i]) || "ESTADO".equals(campos[i])){
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
                    pedido_proveedor.setIdPedidoProveedor(-1);
                    pedido_proveedor.setIdProveedor(-1);
                    pedido_proveedor.setEstado_produccion(-1);
                    pedido_proveedor.setTotal(-1);
                    result.add(pedido_proveedor);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                pedido_proveedor.setIdPedidoProveedor(-1);
                pedido_proveedor.setIdProveedor(-1);
                pedido_proveedor.setEstado_produccion(-1);
                pedido_proveedor.setTotal(-1);
                result.add(pedido_proveedor);
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
               PedidoProveedor pp = new PedidoProveedor();
               
               if(select.length == 1 && "*".trim().equals(select[0])){
                   pp.setIdPedidoProveedor(rs.getInt("ID_PEDIDO_PROVEEDOR"));
                   pp.setIdProveedor(rs.getInt("ID_PROVEEDOR"));
                   pp.setFechaPedido(rs.getDate("FECHA_PEDIDO"));
                   pp.setEstado_produccion(rs.getInt("ESTADO_PRODUCCION"));
                   pp.setTotal(rs.getInt("TOTAL"));
                   
                   
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if("ID_PEDIDO_PROVEEDOR".equalsIgnoreCase(select[j])){
                            pp.setIdPedidoProveedor(rs.getInt("ID_PEDIDO_PROVEEDOR"));
                        }  
                        if("ID_PROVEEDOR".equalsIgnoreCase(select[j])){
                            pp.setIdProveedor(rs.getInt("ID_PROVEEDOR"));
                        }
                        if("FECHA_PEDIDO".equalsIgnoreCase(select[j])){
                            pp.setFechaPedido(rs.getDate("FECHA_PEDIDO"));
                        }
                        if("ESTADO_PRODUCCION".equalsIgnoreCase(select[j])){
                            pp.setEstado_produccion(rs.getInt("ESTADO_PRODUCCION"));
                        }
                        if("TOTAL".equalsIgnoreCase(select[j])){
                            pp.setTotal(rs.getInt("TOTAL"));
                        }
                    }
               }

               result.add(pp);            
           }
        }
        catch(SQLException ex){
            Logger.getLogger(PedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
            pedido_proveedor.setIdPedidoProveedor(-1);
            pedido_proveedor.setIdProveedor(-1);
            pedido_proveedor.setEstado_produccion(-1);
            pedido_proveedor.setTotal(-1);
            result.add(pedido_proveedor);
            return result;
        }
       finally{
           try{
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(PedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public ArrayList<PedidoProveedor> consultarPedidoProveedor(int idPedidoProveedor)
    {
        String sql="SELECT ID_PROVEEDOR,FECHA_PEDIDO,ESTADO_PRODUCCION,TOTAL "
                   + "FROM PEDIDO_PROVEEDOR WHERE ID_PEDIDO_PROVEEDOR='"+idPedidoProveedor+"'";
        ArrayList<PedidoProveedor> result = new ArrayList<>();
        PedidoProveedor pp = new PedidoProveedor();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               pp.setIdPedidoProveedor(idPedidoProveedor);
               pp.setIdProveedor(rs.getInt("ID_PROVEEDOR"));
               pp.setFechaPedido(rs.getDate("FECHA_PEDIDO"));
               pp.setEstado_produccion(rs.getInt("ESTADO_PRODUCCION"));
               pp.setTotal(rs.getInt("TOTAL"));
               result.add(pp);
               
           }
        }
        catch(SQLException ex)
       {Logger.getLogger(PedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);}
       finally
       {
           try
           {
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(PedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public String actualizarPedidoProveedor(int idPedidoProveedor,int idProveedor,String fechaPed,
                                            int estadoProduccion)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM PEDIDO_PROVEEDOR "
                      + "WHERE ID_PEDIDO_PROVEEDOR = ?";
            Date fechaPedidoPP = null;
            java.sql.Date fechaPedido;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, idPedidoProveedor);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    
                    if( idProveedor == -1){
                        return "Falta el id del proveedor";
                    }else
                    if( fechaPed == null || "".equals(fechaPed)){
                        return "Falta la fecha de pedido.";
                    }else
                    if( estadoProduccion == -1){
                        return "Falta el estado del pedido";
                    }

                    try{
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        fechaPedidoPP = df.parse(fechaPed);
                        fechaPedido = new java.sql.Date(fechaPedidoPP.getTime());
                        java.util.Date fechaAct = new java.util.Date();
                        String fechaActual = df.format(fechaAct);
                        fechaActual.split(fechaActual, 11);
                        fechaAct = df.parse(fechaActual);
                        
                        if ( fechaPedido.before(fechaAct)) {
                            return "La fecha de pedido no puede ser menor a la fecha actual.";
                        }
                        if ( fechaPedido.after(fechaAct)) {
                            return "La fecha de pedido no puede ser mayor a la fecha actual.";
                        }

                    }catch(ParseException e){
                        return "Fecha no válida";
                    }
                    
                    String sql="UPDATE PEDIDO_PROVEEDOR SET ID_PEDIDO_PROVEEDOR='"+idPedidoProveedor+"', "
                        + "ID_PROVEEDOR='"+idProveedor+"', FECHA_PEDIDO=TO_DATE('"+fechaPedido+"','yyyy-mm-dd'), "
                        + "ESTADO_PRODUCCION='"+estadoProduccion+"' "
                        + "WHERE ID_PEDIDO_PROVEEDOR = ?";         
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setInt(1, idPedidoProveedor);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger(PedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
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
               Logger.getLogger(PedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }    
        return "Se actualizo correctamente";
    }
}

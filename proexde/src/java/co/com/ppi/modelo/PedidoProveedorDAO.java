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
            if( fechaPed == null || fechaPed.equals("")){
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

                if ( fechaPedido.after(fechaAct)) {
                    return "La fecha de pedido no puede ser mayor a la fecha actual.";
                }

            }catch(Exception e){
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
        
        String seleccionar = sel != null && !sel.trim().equals("") ? sel : "";
        String[] campos = cam != null && !cam.trim().equals("") ? cam.split(",") : null;
        String[] valores = val != null && !val.trim().equals("") ? val.split(",") : null;
        String orden = ord != null && !ord.trim().equals("") ? ord : "";
        
        try
        {
           con=conex.conexion();
           //String sql="SELECT ID_PEDIDO_PROVEEDOR,NOMBRE,DESCRIPCION,UNIDAD_MEDIDA FROM PEDIDO_PROVEEDORS WHERE ESTADO=0";
           StringBuilder sql = new StringBuilder();
           
           
           if( !seleccionar.equals("") ){
               sql.append("SELECT ");
               sql.append(seleccionar);
               sql.append(" FROM PEDIDO_PROVEEDOR ");
           }else{
               pedido_proveedor.setIdPedidoProveedor(-1);
               pedido_proveedor.setIdProveedor(-1);
               pedido_proveedor.setEstado_produccion(-1);
               pedido_proveedor.setTotal(-1);
//               pedido_proveedor.setDescripcion("ERROR: Faltan los campos a seleccionar en la consulta.");
               result.add(pedido_proveedor);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if (campos[i].equals("ID_PEDIDO_PROVEEDOR") || campos[i].equals("ESTADO")){
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
//               pedido_proveedor.setDescripcion("ERROR: Faltan los campos a seleccionar en la consulta.");
                    result.add(pedido_proveedor);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                pedido_proveedor.setIdPedidoProveedor(-1);
                pedido_proveedor.setIdProveedor(-1);
                pedido_proveedor.setEstado_produccion(-1);
                pedido_proveedor.setTotal(-1);
//               pedido_proveedor.setDescripcion("ERROR: Faltan los campos a seleccionar en la consulta.");
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
               
               if(select.length == 1 && select[0].trim().equals("*")){
                   pp.setIdPedidoProveedor(rs.getInt("ID_PEDIDO_PROVEEDOR"));
                   pp.setIdProveedor(rs.getInt("ID_PROVEEDOR"));
                   pp.setFechaPedido(rs.getDate("FECHA_PEDIDO"));
                   pp.setEstado_produccion(rs.getInt("ESTADO_PRODUCCION"));
                   pp.setTotal(rs.getInt("TOTAL"));
                   
                   
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if(select[j].toUpperCase().equals("ID_PEDIDO_PROVEEDOR")){
                            pp.setIdPedidoProveedor(rs.getInt("ID_PEDIDO_PROVEEDOR"));
                        }  
                        if(select[j].toUpperCase().equals("ID_PROVEEDOR")){
                            pp.setIdProveedor(rs.getInt("ID_PROVEEDOR"));
                        }
                        if(select[j].toUpperCase().equals("FECHA_PEDIDO")){
                            pp.setFechaPedido(rs.getDate("FECHA_PEDIDO"));
                        }
                        if(select[j].toUpperCase().equals("ESTADO_PRODUCCION")){
                            pp.setEstado_produccion(rs.getInt("ESTADO_PRODUCCION"));
                        }
                        if(select[j].toUpperCase().equals("TOTAL")){
                            pp.setTotal(rs.getInt("TOTAL"));
                        }
                    }
               }

               result.add(pp);            
           }
        }
        catch(Exception ex){
            Logger.getLogger(PedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
            pedido_proveedor.setIdPedidoProveedor(-1);
            pedido_proveedor.setIdProveedor(-1);
            pedido_proveedor.setEstado_produccion(-1);
            pedido_proveedor.setTotal(-1);
//            pedido_proveedor.setDescripcion(ex.getMessage());
            result.add(pedido_proveedor);
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
        catch(Exception ex)
       {Logger.getLogger(PedidoProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);}
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
    
    public String actualizarPedidoProveedor(int idPedidoProveedor,int idProveedor,String fechaPed,
                                            int estadoProduccion)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM PEDIDO_PROVEEDOR "
                      + "WHERE ID_PEDIDO_PROVEEDOR = '"+idPedidoProveedor+"'";
            Date fechaPedidoPP = null;
            java.sql.Date fechaPedido;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    
                    if( idProveedor == -1){
                        return "Falta el id del proveedor";
                    }else
                    if( fechaPed == null || fechaPed.equals("")){
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

                        if ( fechaPedido.after(fechaAct)) {
                            return "La fecha de pedido no puede ser mayor a la fecha actual.";
                        }

                    }catch(Exception e){
                        return "Fecha no válida";
                    }
                    
                    String sql="UPDATE PEDIDO_PROVEEDOR SET ID_PEDIDO_PROVEEDOR='"+idPedidoProveedor+"', "
                        + "ID_PROVEEDOR='"+idProveedor+"', FECHA_PEDIDO=TO_DATE('"+fechaPedido+"','yyyy-mm-dd'), "
                        + "ESTADO_PRODUCCION='"+estadoProduccion+"' "
                        + "WHERE ID_PEDIDO_PROVEEDOR = '"+idPedidoProveedor+"'";         
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
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
           catch(Exception ex){}
       }    
//        return "El id del producto #"+id_PedidoProveedor+" fue actualizado correctamente";
        return "Se actualizo correctamente";
    }
}

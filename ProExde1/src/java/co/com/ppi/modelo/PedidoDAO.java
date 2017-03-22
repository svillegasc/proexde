/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.Pedido;
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
public class PedidoDAO {
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarPedido(/*int idPedido,*/int idCotizacion,String fechaPed,
                                 String fechaIni,String fechaTer,int estadoProduccion)
    {
        String sql="INSERT INTO PEDIDO VALUES(?,?,?,?,?,?,?,?)";
        Date fechaPedidoP = null;
        Date fechaInicioP = null;
        Date fechaTerminacionP = null;
        java.sql.Date fechaPedido;
        java.sql.Date fechaInicio;
        java.sql.Date fechaTerminacion;
        try
        {
            if( idCotizacion == -1){
                return "Falta el id de cotización.";
            }else
            if( fechaPed == null || "".trim().equals(fechaPed)){
                return "Falta la fecha de pedido.";
            }else
            if( fechaIni == null || "".trim().equals(fechaIni)){
                return "Falta la fecha de inicio de producción.";
            }else
            if( fechaTer == null || "".equals(fechaTer)){
                return "Falta la fecha de terminación de la producción.";
            }else
            if( estadoProduccion == -1){
                return "Falta el estado de producción.";
            } 
            try{
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                fechaPedidoP = df.parse(fechaPed);
                fechaPedido = new java.sql.Date(fechaPedidoP.getTime());
                fechaInicioP = df.parse(fechaIni);
                fechaInicio = new java.sql.Date(fechaInicioP.getTime());
                fechaTerminacionP = df.parse(fechaTer);
                fechaTerminacion = new java.sql.Date(fechaTerminacionP.getTime());
                java.util.Date fechaAct = new java.util.Date();
                String fechaActual = df.format(fechaAct);
                fechaActual.split(fechaActual, 11);
                fechaAct = df.parse(fechaActual);

                if ( fechaPedido.after(fechaAct)) {
                    return "La fecha de pedido no puede ser mayor a la fecha actual.";
                }else
                if( fechaPedido.before(fechaAct) && !fechaPedido.equals(fechaAct)) {
                    return "La fecha de pedido no puede ser menor a la fecha actual.";
                }else
                if( fechaInicio.before(fechaPedido) && !fechaInicio.equals(fechaPedido)) {
                    return "La fecha de inicio no puede ser menor a la fecha de pedido.";
                }else
                if( fechaInicio.after(fechaTerminacion)) {
                    return "La fecha de inicio no puede ser mayor a la fecha de terminación.";
                }

            }catch(ParseException e){
                return "Fecha no válida";
            }
            
            String sqlI="SELECT PED.NEXTVAL FROM DUAL";
            int idPedido = 0;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                idPedido = rs.getInt("NEXTVAL");
            }
            con=conex.conexion();
            pr=con.prepareStatement(sql);

            pr=con.prepareStatement(sql);
            pr.setInt(1,idPedido);
            pr.setInt(2,idCotizacion);
            pr.setDate(3,fechaPedido);
            pr.setInt(4,0);
            pr.setDate(5,fechaInicio);
            pr.setDate(6,fechaTerminacion);
            pr.setInt(7,estadoProduccion);
            pr.setInt(8,0);
            pr.executeUpdate();
        }
        catch (SQLException ex) {
            Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
        return "Se inserto correctamente";
    }
    
    public ArrayList<Pedido>consultarPedidos(String sel, String cam, String val, String ord)
    {
        ArrayList<Pedido> result = new ArrayList<> ();
        Pedido pedido = new Pedido();
        
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
               sql.append(" FROM PEDIDO ");
           }else{
               pedido.setIdPedido(-1);
               pedido.setIdCotizacion(-1);
               pedido.setCantidadProducida(-1);
               pedido.setEstadoProduccion(-1);
               pedido.setValorTotal(-1);
               result.add(pedido);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if ("ID_PEDIDO".equals(campos[i]) || "ESTADO".equals(campos[i])){
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
                    pedido.setIdPedido(-1);
                    pedido.setIdCotizacion(-1);
                    pedido.setCantidadProducida(-1);
                    pedido.setEstadoProduccion(-1);
                    pedido.setValorTotal(-1);
                    result.add(pedido);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                pedido.setIdPedido(-1);
                pedido.setIdCotizacion(-1);
                pedido.setCantidadProducida(-1);
                pedido.setEstadoProduccion(-1);
                pedido.setValorTotal(-1);
                result.add(pedido);
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
               Pedido p = new Pedido();
               if(select.length == 1 && "*".trim().equals(select[0])){
                   p.setIdPedido(rs.getInt("ID_PEDIDO"));
                   p.setIdCotizacion(rs.getInt("ID_COTIZACION"));
                   p.setFechaPedido(rs.getDate("FECHA_PEDIDO"));
                   p.setCantidadProducida(rs.getInt("CANTIDAD_PRODUCIDA"));
                   p.setFechaInicio(rs.getDate("FECHA_INICIO"));
                   p.setFechaTerminacion(rs.getDate("FECHA_TERMINACION"));
                   p.setEstadoProduccion(rs.getInt("ESTADO_PRODUCCION"));
                   p.setValorTotal(rs.getInt("VALOR_TOTAL"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if("ID_PEDIDO".toUpperCase().equals(select[j])){
                            p.setIdPedido(rs.getInt("ID_PEDIDO"));
                        }  
                        if("ID_COTIZACION".toUpperCase().equals(select[j])){
                            p.setIdCotizacion(rs.getInt("ID_COTIZACION"));
                        }
                        if("FECHA_PEDIDO".toUpperCase().equals(select[j])){
                            p.setFechaPedido(rs.getDate("FECHA_PEDIDO"));
                        }
                        if("CANTIDAD_PRODUCIDA".toUpperCase().equals(select[j])){
                            p.setCantidadProducida(rs.getInt("CANTIDAD_PRODUCIDA"));
                        }
                        if("FECHA_INICIO".toUpperCase().equals(select[j])){
                            p.setFechaInicio(rs.getDate("FECHA_INICIO"));
                        }
                        if("FECHA_TERMINACION".toUpperCase().equals(select[j])){
                            p.setFechaTerminacion(rs.getDate("FECHA_TERMINACION"));
                        }
                        if("ESTADO_PRODUCCION".toUpperCase().equals(select[j])){
                            p.setEstadoProduccion(rs.getInt("ESTADO_PRODUCCION"));
                        }
                        if("VALOR_TOTAL".toUpperCase().equals(select[j])){
                            p.setValorTotal(rs.getInt("VALOR_TOTAL"));
                        }
                    }
               }

               result.add(p);            
           }
        }
        catch(SQLException ex){
            Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, null, ex);
            pedido.setIdPedido(-1);
            pedido.setIdCotizacion(-1);
            pedido.setCantidadProducida(-1);
            pedido.setEstadoProduccion(-1);
            pedido.setValorTotal(-1);
            result.add(pedido);
            return result;
        }
       finally{
           try{
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public ArrayList<Pedido> consultarPedido(int idPedido)
    {
        String sql="SELECT ID_COTIZACION,FECHA_PEDIDO,CANTIDAD_PRODUCIDA,FECHA_INICIO,"
                 + "FECHA_TERMINACION,ESTADO_PRODUCCION,VALOR_TOTAL "
                 + "FROM PEDIDO WHERE ID_PEDIDO='"+idPedido+"'";
        ArrayList<Pedido> result = new ArrayList<>();
        Pedido p = new Pedido();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               p.setIdPedido(idPedido);
               p.setIdCotizacion(rs.getInt("ID_COTIZACION"));
               p.setFechaPedido(rs.getDate("FECHA_PEDIDO"));
               p.setCantidadProducida(rs.getInt("CANTIDAD_PRODUCIDA"));
               p.setFechaInicio(rs.getDate("FECHA_INICIO"));
               p.setFechaTerminacion(rs.getDate("FECHA_TERMINACION"));
               p.setEstadoProduccion(rs.getInt("ESTADO_PRODUCCION"));
               p.setValorTotal(rs.getInt("VALOR_TOTAL"));
               result.add(p);
               
           }
        }
        catch(SQLException ex)
       {Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, null, ex);}
       finally
       {
           try
           {
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public String actualizarPedido(int idPedido,int idCotizacion,String fechaPed,
                                   int cantidadProducida,String fechaIni,
                                   String fechaTer,int estadoProduccion){
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM PEDIDO WHERE ID_PEDIDO = ? ";
            Date fechaPedidoP;
            Date fechaInicioP;
            Date fechaTerminacionP;
            java.sql.Date fechaPedido;
            java.sql.Date fechaInicio;
            java.sql.Date fechaTerminacion;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, idPedido);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    
                    if( idCotizacion == -1){
                        return "Falta el id de cotización.";
                    }else
                    if( fechaPed == null || "".trim().equals(fechaPed)){
                        return "Falta la fecha de pedido.";
                    }else
                    if( fechaIni == null || "".trim().equals(fechaIni)){
                        return "Falta la fecha de inicio de producción.";
                    }else
                    if( fechaTer == null || "".equals(fechaTer)){
                        return "Falta la fecha de terminación de la producción.";
                    }else
                    if( estadoProduccion == -1){
                        return "Falta el estado de producción.";
                    }

                    try{
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        fechaPedidoP = df.parse(fechaPed);
                        fechaPedido = new java.sql.Date(fechaPedidoP.getTime());
                        fechaInicioP = df.parse(fechaIni);
                        fechaInicio = new java.sql.Date(fechaInicioP.getTime());
                        fechaTerminacionP = df.parse(fechaTer);
                        fechaTerminacion = new java.sql.Date(fechaTerminacionP.getTime());
                        java.util.Date fechaAct = new java.util.Date();
                        String fechaActual = df.format(fechaAct);
                        fechaActual.split(fechaActual, 11);
                        fechaAct = df.parse(fechaActual);

                        if ( fechaPedido.after(fechaAct)) {
                            return "La fecha de pedido no puede ser mayor a la fecha actual.";
                        }else
                        if( fechaPedido.before(fechaAct) && !fechaPedido.equals(fechaAct)) {
                            return "La fecha de pedido no puede ser menor a la fecha actual.";
                        }else
                        if( fechaInicio.before(fechaPedido) && !fechaInicio.equals(fechaPedido)) {
                            return "La fecha de inicio no puede ser menor a la fecha de pedido.";
                        }else
                        if( fechaInicio.after(fechaTerminacion)) {
                            return "La fecha de inicio no puede ser mayor a la fecha de terminación.";
                        }

                    }catch(ParseException e){
                        return "Fecha no válida";
                    }
                    
                    String sql="UPDATE PEDIDO SET ID_COTIZACION='"+idCotizacion+"', "
                            + "FECHA_PEDIDO=TO_DATE('"+fechaPedido+"', 'yyyy-mm-dd'), "
                            + "CANTIDAD_PRODUCIDA='"+cantidadProducida+"', "
                            + "FECHA_INICIO=TO_DATE('"+fechaInicio+"', 'yyyy-mm-dd'), "
                            + "FECHA_TERMINACION=TO_DATE('"+fechaTerminacion+"','yyyy-mm-dd'), "
                            + "ESTADO_PRODUCCION='"+estadoProduccion+"' "
                            + "WHERE ID_PEDIDO = ?";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setInt(1, idPedido);
                    pr.executeUpdate();
                }else{
                    return "0 filas actualizadas";
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
       }   
       return "Se actualizo correctamente";
    }
}

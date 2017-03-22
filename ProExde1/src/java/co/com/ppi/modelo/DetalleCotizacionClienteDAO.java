/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.DetalleCotizacionCliente;
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
public class DetalleCotizacionClienteDAO {
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarDetalleCotizacionCliente(int idCotizacion,int idProducto, int precio)
    {
       try
       {  
            String sql="INSERT INTO DETALLE_COTIZACION_CLIENTE VALUES(?,?,?)";
            con=conex.conexion();
            pr=con.prepareStatement(sql); 

            pr.setInt(1,idCotizacion);
            pr.setInt(2,idProducto);
            pr.setInt(3,precio);
            pr.executeUpdate();
       } 
       catch (SQLException ex) {
            Logger.getLogger(DetalleCotizacionClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
       }
       return "Se inserto correctamente";
    }
    
    public ArrayList<DetalleCotizacionCliente> consultarDetalleCotizacionClientes(String sel, String cam, String val, String ord)
    {
        ArrayList<DetalleCotizacionCliente> result = new ArrayList<> ();
        DetalleCotizacionCliente dcc = new DetalleCotizacionCliente();
        
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
               sql.append(" FROM DETALLE_COTIZACION_CLIENTE ");
           }else{
               dcc.setIdCotizacion(-1);
               dcc.setIdProducto(-1);
               result.add(dcc);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if ("DETALLE_COTIZACION_CLIENTE".equals(campos[i]) || "ESTADO".equals(campos[i])){
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
                    dcc.setIdCotizacion(-1);
                    dcc.setIdProducto(-1);
                    result.add(dcc);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                dcc.setIdCotizacion(-1);
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
               DetalleCotizacionCliente dCotCl = new DetalleCotizacionCliente();
               
               if(select.length == 1 && "*".trim().equals(select[0])){
                   dCotCl.setIdCotizacion(rs.getInt("ID_COTIZACION"));
                   dCotCl.setIdProducto(rs.getInt("ID_PRODUCTO"));
                   dCotCl.setPrecio(rs.getInt("PRECIO"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if("ID_COTIZACION".equalsIgnoreCase(select[j])){
                            dCotCl.setIdCotizacion(rs.getInt("ID_COTIZACION"));
                        }  
                        if("ID_PRODUCTO".equalsIgnoreCase(select[j])){
                            dCotCl.setIdProducto(rs.getInt("ID_PRODUCTO"));
                        }
                        if("PRECIO".equalsIgnoreCase(select[j])){
                            dCotCl.setPrecio(rs.getInt("PRECIO"));
                        }
                    }
               }
               result.add(dCotCl);            
           }
        }
        catch(SQLException ex){
            Logger.getLogger(DetalleCotizacionClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            dcc.setIdCotizacion(-1);
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
               Logger.getLogger(DetalleCotizacionClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public ArrayList<DetalleCotizacionCliente> consultarDetalleCotizacionCliente(int idCotizacion,int idProducto)
    {
        String sql="SELECT PRECIO "
                   + "FROM DETALLE_COTIZACION_CLIENTE WHERE ID_COTIZACION='"+idCotizacion+"' AND ID_PRODUCTO='"+idProducto+"' ";
        ArrayList<DetalleCotizacionCliente> result = new ArrayList<>();
        DetalleCotizacionCliente dcc = new DetalleCotizacionCliente();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               dcc.setIdCotizacion(idCotizacion);
               dcc.setIdProducto(idProducto);
               dcc.setPrecio(rs.getInt("PRECIO"));
               result.add(dcc);
               
           }
        }
        catch(SQLException ex)
       {Logger.getLogger(DetalleCotizacionClienteDAO.class.getName()).log(Level.SEVERE, null, ex);}
       finally
       {
           try
           {
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(DetalleCotizacionClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public String actualizarDetalleCotizacionCliente(int idCotizacion,int idProducto,int precio)
    {
        String sqlI="SELECT COUNT (*) CONT FROM DETALLE_COTIZACION_CLIENTE WHERE "
                    + "ID_COTIZACION = ? AND ID_PRODUCTO= ?";
        try
        {
            
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, idCotizacion);
            pr.setInt(2, idProducto);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE DETALLE_COTIZACION_CLIENTE SET PRECIO= ? "
                            + "WHERE ID_COTIZACION = ? AND ID_PRODUCTO= ?";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setInt(1, precio);
                    pr.setInt(2, idCotizacion);
                    pr.setInt(3, idProducto);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger(DetalleCotizacionClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
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
               Logger.getLogger(DetalleCotizacionClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }    
        return "Se actualizo correctamente";
    }
    
    public int valorCotizacion(int idCotizacion, int idProducto){
        int precio = 0;
        String sql="SELECT PRECIO FROM DETALLE_COTIZACION_CLIENTE WHERE "
                    + "ID_COTIZACION = ? AND ID_PRODUCTO = ?";
        try {
            con=conex.conexion();
            pr=con.prepareStatement(sql);
            pr.setInt(1, idCotizacion);
            pr.setInt(2, idProducto);
            rs=pr.executeQuery();
            
            if ( rs.next() )
            {
                precio = rs.getInt("PRECIO");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DetalleCotizacionClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return precio;
    }
}

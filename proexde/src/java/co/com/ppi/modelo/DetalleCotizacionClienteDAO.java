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
               sql.append(" FROM DETALLE_COTIZACION_CLIENTE ");
           }else{
               dcc.setIdCotizacion(-1);
               dcc.setIdProducto(-1);
//               dcc.setEstado("ERROR: Faltan los campos a seleccionar en la consulta.");
               result.add(dcc);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if (campos[i].equals("DETALLE_COTIZACION_CLIENTE") || campos[i].equals("ESTADO")){
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
     //               dcc.setEstado("ERROR: Faltan los campos a seleccionar en la consulta.");
                    result.add(dcc);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                dcc.setIdCotizacion(-1);
                dcc.setIdProducto(-1);
//               dcc.setEstado("ERROR: Faltan los campos a seleccionar en la consulta.");
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
               
               if(select.length == 1 && select[0].trim().equals("*")){
                   dCotCl.setIdCotizacion(rs.getInt("ID_COTIZACION"));
                   dCotCl.setIdProducto(rs.getInt("ID_PRODUCTO"));
                   dCotCl.setPrecio(rs.getInt("PRECIO"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if(select[j].toUpperCase().equals("ID_COTIZACION")){
                            dCotCl.setIdCotizacion(rs.getInt("ID_COTIZACION"));
                        }  
                        if(select[j].toUpperCase().equals("ID_PRODUCTO")){
                            dCotCl.setIdProducto(rs.getInt("ID_PRODUCTO"));
                        }
                        if(select[j].toUpperCase().equals("PRECIO")){
                            dCotCl.setPrecio(rs.getInt("PRECIO"));
                        }
                    }
               }
               result.add(dCotCl);            
           }
        }
        catch(Exception ex){
            Logger.getLogger(DetalleCotizacionClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            dcc.setIdCotizacion(-1);
            dcc.setIdProducto(-1);
//            dcc.setEstado(ex.getMessage());
            result.add(dcc);
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
        catch(Exception ex)
       {Logger.getLogger(DetalleCotizacionClienteDAO.class.getName()).log(Level.SEVERE, null, ex);}
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
    
    public String actualizarDetalleCotizacionCliente(int idCotizacion,int idProducto,int precio)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM DETALLE_COTIZACION_CLIENTE WHERE "
                    + "ID_COTIZACION = '"+idCotizacion+"' AND ID_PRODUCTO='"+idProducto+"'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE DETALLE_COTIZACION_CLIENTE SET PRECIO='"+precio+"'"
                            + "WHERE ID_COTIZACION = '"+idCotizacion+"' AND ID_PRODUCTO='"+idProducto+"'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
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
           catch(Exception ex){}
       }    
        return "Se actualizo correctamente";
    }
    
    public int valorCotizacion(int idCotizacion, int idProducto){
        int precio = 0;
        try {
            String sql="SELECT PRECIO FROM DETALLE_COTIZACION_CLIENTE WHERE "
                    + "ID_COTIZACION = '"+idCotizacion+"' AND ID_PRODUCTO = '"+idProducto+"'";
            con=conex.conexion();
            pr=con.prepareStatement(sql);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.Cotizacion;
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
public class CotizacionDAO {
    
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarCotizacion(/*int idCotizacion,*/int idUsuario,String fechaCrea)
    {
        String sql="INSERT INTO COTIZACION VALUES(?,?,?,?)";
        Date fechaCreacionC = null;
        java.sql.Date fechaCreacion;
        try
        {
            if(fechaCrea.equals("") || fechaCrea == null){
                return "Falta la fecha de creación.";
            }
            try{
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                fechaCreacionC = df.parse(fechaCrea);
                fechaCreacion = new java.sql.Date(fechaCreacionC.getTime());
                java.util.Date fechaAct = new java.util.Date();
                String fechaActual = df.format(fechaAct);
                fechaActual.split(fechaActual, 11);
                fechaAct = df.parse(fechaActual);

                if ( fechaCreacion.after(fechaAct)) {
                    return "La fecha de creación no puede ser mayor a la fecha actual.";
                }
            }catch(Exception e){
                return "Fecha no válida";
            }

            String sqlI="SELECT COT.NEXTVAL FROM DUAL";
            int idCotizacion = 0;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                idCotizacion = rs.getInt("NEXTVAL");
            }
            con=conex.conexion();
            pr=con.prepareStatement(sql);

            pr=con.prepareStatement(sql);
            pr.setInt(1,idCotizacion);
            pr.setInt(2,idUsuario);
            pr.setDate(3,fechaCreacion);
            pr.setString(4,"A");
            pr.executeUpdate();
        }
        catch (SQLException ex) {
            Logger.getLogger(CotizacionDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
        return "Se inserto correctamente";
    }
    
    public List<Cotizacion> consultarCotizaciones(String sel, String cam, String val, String ord)
    {
        ArrayList<Cotizacion> result = new ArrayList<> ();
        Cotizacion producto = new Cotizacion();
        
        String seleccionar = !sel.trim().equals("") ? sel : "" && sel != null;
        String[] campos = !cam.trim().equals("") ? cam.split(",") : null && cam != null;
        String[] valores = !val.trim().equals("") ? val.split(",") : null && val != null;
        String orden = !ord.trim().equals("") ? ord : "" && ord != null;
        
        try
        {
           con=conex.conexion();
           StringBuilder sql = new StringBuilder();
           
           
           if(!seleccionar.equals("") ){
               sql.append("SELECT ");
               sql.append(seleccionar);
               sql.append(" FROM COTIZACION ");
           }else{
               producto.setIdCotizacion(-1);
               producto.setIdUsuario(-1);
               result.add(producto);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if (campos[i].equals("ID_COTIZACION") || campos[i].equals("ESTADO")){
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
                    producto.setIdCotizacion(-1);
                    producto.setIdUsuario(-1);
                    result.add(producto);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                producto.setIdCotizacion(-1);
                producto.setIdUsuario(-1);
                result.add(producto);
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
               Cotizacion c = new Cotizacion();
               
               if(select.length == 1 && select[0].trim().equals("*")){
                   c.setIdCotizacion(rs.getInt("ID_COTIZACION"));
                   c.setIdUsuario(rs.getInt("ID_USUARIO"));
                   c.setFechaCreacion(rs.getDate("FECHA_CREACION"));
                   c.setEstado(rs.getString("ESTADO"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if(select[j].equalsIgnoreCase().equals("ID_COTIZACION")){
                            c.setIdCotizacion(rs.getInt("ID_COTIZACION"));
                        }  
                        if(select[j].equalsIgnoreCase().equals("ID_USUARIO")){
                            c.setIdUsuario(rs.getInt("ID_USUARIO"));
                        }
                        if(select[j].equalsIgnoreCase().equals("FECHA_CREACION")){
                            c.setFechaCreacion(rs.getDate("FECHA_CREACION"));
                        }
                        if(select[j].equalsIgnoreCase().equals("ESTADO")){
                            c.setEstado(rs.getString("ESTADO"));
                        }
                    }
               }

               result.add(c);            
           }
        }
        catch(Exception ex){
            Logger.getLogger(CotizacionDAO.class.getName()).log(Level.SEVERE, null, ex);
            producto.setIdCotizacion(-1);
            producto.setIdUsuario(-1);
            result.add(producto);
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
    
    public List<Cotizacion> consultarCotizacion(int id_Cotizacion)
    {
        String sql="SELECT ID_USUARIO,FECHA_CREACION "
                   + "FROM COTIZACION WHERE ID_COTIZACION='"+id_Cotizacion+"' AND ESTADO='A'";
        ArrayList<Cotizacion> result = new ArrayList<>();
        Cotizacion c = new Cotizacion();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               c.setIdCotizacion(id_Cotizacion);
               c.setIdUsuario(rs.getInt("ID_USUARIO"));
               c.setFechaCreacion(rs.getDate("FECHA_CREACION"));
               result.add(c);
               
           }
        }
        catch(Exception ex)
       {Logger.getLogger(CotizacionDAO.class.getName()).log(Level.SEVERE, null, ex);}
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
    
    public String actualizarCotizacion(int idCotizacion,int idUsuario,String fechaCrea)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM COTIZACION WHERE ID_COTIZACION = '"+idCotizacion+"' AND ESTADO='A'";
            Date fechaCreacionC;
            java.sql.Date fechaCreacion;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    
                    if( fechaCrea == null || fechaCrea.equals("")){
                        return "Falta la fecha de creación.";
                    }

                    try{
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        fechaCreacionC = df.parse(fechaCrea);
                        fechaCreacion = new java.sql.Date(fechaCreacionC.getTime());
                        java.util.Date fechaAct = new java.util.Date();
                        String fechaActual = df.format(fechaAct);
                        fechaActual.split(fechaActual, 11);
                        fechaAct = df.parse(fechaActual);

                        if ( fechaCreacion.after(fechaAct)) {
                            return "La fecha de creación no puede ser mayor a la fecha actual.";
                        }

                    }catch(Exception e){
                        return "Fecha no válida";
                    }
                    
                    String sql="UPDATE COTIZACION SET ID_USUARIO='"+idUsuario+"', FECHA_CREACION=TO_DATE('"+fechaCreacion+"','yyyy-mm-dd') "
                        + "WHERE ID_COTIZACION = '"+idCotizacion+"' AND ESTADO='A'";         
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
            Logger.getLogger(CotizacionDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public String eliminarCotizacion(int idCotizacion)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM COTIZACION WHERE ID_COTIZACION = '"+idCotizacion+"' AND ESTADO='A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE COTIZACIONS SET ESTADO='I' WHERE ID_COTIZACION = '"+idCotizacion+"'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate(); 
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
            Logger.getLogger(CotizacionDAO.class.getName()).log(Level.SEVERE, null, ex);
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

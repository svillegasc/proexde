/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.Producto;
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
public class ProductoDAO {
    
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarProducto(/*int idProducto,*/String nombre,String descripcion,
                                   String ultimaEnt,String ultimaSal)
    {
        String sql="INSERT INTO PRODUCTO VALUES(?,?,?,?,?,?,?)";
        Date ultimaEntradaP = null;
        Date ultimaSalidaP = null;
        java.sql.Date ultimaEntrada;
        java.sql.Date ultimaSalida;
        try
        {
            if( nombre == null || nombre.trim().equals("")){
                return "Falta el nombre.";
            }else
            if( descripcion == null || descripcion.trim().equals("")){
                return "Falta la descripcion.";
            }else
            if( ultimaEnt == null || ultimaEnt.equals("")){
                return "Falta la fecha de ultima entrada.";
            }else
            if( ultimaSal == null || ultimaSal.equals("")){
                return "Falta la fecha de ultima salida.";
            }

            try{
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                ultimaEntradaP = df.parse(ultimaEnt);
                ultimaEntrada = new java.sql.Date(ultimaEntradaP.getTime());
                ultimaSalidaP = df.parse(ultimaSal);
                ultimaSalida = new java.sql.Date(ultimaSalidaP.getTime());
                java.util.Date fechaAct = new java.util.Date();
                String fechaActual = df.format(fechaAct);
                fechaActual.split(fechaActual, 11);
                fechaAct = df.parse(fechaActual);

                if ( ultimaEntrada.after(fechaAct)) {
                    return "La ultima fecha de entrada no puede ser mayor a la fecha actual.";
                }else
                if( ultimaSalida.before(fechaAct) && !ultimaSalida.equals(fechaAct)) {
                    return "La ultima fecha de salida no puede ser menor a la fecha actual.";
                }else
                if( ultimaSalida.before(ultimaEntrada) && !ultimaSalida.equals(ultimaEntrada)) {
                    return "La ultima fecha de salida no puede ser menor a la ultima fecha de entrada.";
                }  
            }catch(Exception e){
                return "Fecha no válida";
            }

            String sqlI="SELECT PROD.NEXTVAL FROM DUAL";
            int idProducto = 0;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                idProducto = rs.getInt("NEXTVAL");
            }

            con=conex.conexion();
            pr=con.prepareStatement(sql);

            pr=con.prepareStatement(sql);
            pr.setInt(1,idProducto);
            pr.setString(2,nombre);
            pr.setString(3,descripcion);
            pr.setInt(4,0);
            pr.setDate(5,ultimaEntrada);
            pr.setDate(6,ultimaSalida);
            pr.setString(7,"A");
            pr.executeUpdate();
        }
        catch (SQLException ex) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
        return "Se inserto correctamente";
    }
    
    public ArrayList<Producto> consultarProductos(String sel, String cam, String val, String ord)
    {
        ArrayList<Producto> result = new ArrayList<> ();
        Producto producto = new Producto();
        
        String seleccionar = sel != null && !sel.trim().equals("") ? sel : "";
        String[] campos = cam != null && !cam.trim().equals("") ? cam.split(",") : null;
        String[] valores = val != null && !val.trim().equals("") ? val.split(",") : null;
        String orden = ord != null && !ord.trim().equals("") ? ord : "";
        
        try
        {
           con=conex.conexion();
           //String sql="SELECT ID_PRODUCTO,NOMBRE,DESCRIPCION,UNIDAD_MEDIDA FROM PRODUCTOS WHERE ESTADO=0";
           StringBuilder sql = new StringBuilder();
           
           
           if( !seleccionar.equals("") ){
               sql.append("SELECT ");
               sql.append(seleccionar);
               sql.append(" FROM PRODUCTO ");
           }else{
               producto.setIdProducto(-1);
               producto.setStock(-1);
               producto.setDescripcion("ERROR: Faltan los campos a seleccionar en la consulta.");
               result.add(producto);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if (campos[i].equals("ID_PRODUCTO") || campos[i].equals("ESTADO")){
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
                    producto.setIdProducto(-1);
                    producto.setStock(-1);
                    producto.setDescripcion("ERROR: Los campos y los valores no tienen el mismo número de argumentos.");
                    result.add(producto);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                producto.setIdProducto(-1);
                producto.setStock(-1);
                producto.setDescripcion("ERROR: Los campos y los valores no tienen el mismo número de argumentos.");
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
               Producto i = new Producto();
               
               if(select.length == 1 && select[0].trim().equals("*")){
                   i.setIdProducto(rs.getInt("ID_PRODUCTO"));
                   i.setNombre(rs.getString("NOMBRE"));
                   i.setDescripcion(rs.getString("DESCRIPCION"));
                   i.setStock(rs.getInt("STOCK"));
                   i.setUltimaEntrada(rs.getDate("ULTIMA_ENTRADA"));
                   i.setUltimaSalida(rs.getDate("ULTIMA_SALIDA"));
                   i.setEstado(rs.getString("ESTADO"));
                   
                   
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if(select[j].toUpperCase().equals("ID_PRODUCTO")){
                            i.setIdProducto(rs.getInt("ID_PRODUCTO"));
                        }  
                        if(select[j].toUpperCase().equals("NOMBRE")){
                            i.setNombre(rs.getString("NOMBRE"));
                        }
                        if(select[j].toUpperCase().equals("DESCRIPCION")){
                            i.setDescripcion(rs.getString("DESCRIPCION"));
                        }
                        if(select[j].toUpperCase().equals("STOCK")){
                            i.setStock(rs.getInt("STOCK"));
                        }
                        if(select[j].toUpperCase().equals("ULTIMA_ENTRADA")){
                            i.setUltimaEntrada(rs.getDate("ULTIMA_ENTRADA"));
                        }
                        if(select[j].toUpperCase().equals("ULTIMA_SALIDA")){
                            i.setUltimaSalida(rs.getDate("ULTIMA_SALIDA"));
                        }
                        if(select[j].toUpperCase().equals("ESTADO")){
                            i.setEstado(rs.getString("ESTADO"));
                        }
                    }
               }

               result.add(i);            
           }
        }
        catch(Exception ex){
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
            producto.setIdProducto(-1);
            producto.setStock(-1);
            producto.setDescripcion(ex.getMessage());
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
    
    public ArrayList<Producto> consultarProducto(int idProducto)
    {
        String sql="SELECT NOMBRE,DESCRIPCION,STOCK,ULTIMA_ENTRADA,ULTIMA_SALIDA "
                   + "FROM PRODUCTO WHERE ID_INSUMO='"+idProducto+"' AND ESTADO='A'";
        ArrayList<Producto> result = new ArrayList<>();
        Producto p = new Producto();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               p.setIdProducto(idProducto);
               p.setNombre(rs.getString("NOMBRE"));
               p.setDescripcion(rs.getString("DESCRIPCION"));
               p.setStock(rs.getInt("STOCK"));
               p.setUltimaEntrada(rs.getDate("ULTIMA_ENTRADA"));
               p.setUltimaSalida(rs.getDate("ULTIMA_SALIDA"));
               result.add(p);
               
           }
        }
        catch(Exception ex)
       {Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);}
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
      
    public String actualizarProducto(int idProducto,String nombre,String descripcion,
                                     String ultimaEnt,String ultimaSal)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM PRODUCTO WHERE ID_PRODUCTO = '"+idProducto+"' AND ESTADO='A'";
            Date ultimaEntradaA = null;
            Date ultimaSalidaA = null;
            java.sql.Date ultimaEntrada;
            java.sql.Date ultimaSalida;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    
                    if( nombre == null || nombre.trim().equals("")){
                        return "Falta el nombre.";
                    }else
                    if( descripcion == null || descripcion.trim().equals("")){
                        return "Falta la descripcion.";
                    }else
                    if( ultimaEnt == null || ultimaEnt.equals("")){
                        return "Falta la fecha de ultima entrada.";
                    }else
                    if( ultimaSal == null || ultimaSal.equals("")){
                        return "Falta la fecha de ultima salida.";
                    }

                    try{
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        ultimaEntradaA = df.parse(ultimaEnt);
                        ultimaEntrada = new java.sql.Date(ultimaEntradaA.getTime());
                        ultimaSalidaA = df.parse(ultimaSal);
                        ultimaSalida = new java.sql.Date(ultimaSalidaA.getTime());
                        java.util.Date fechaAct = new java.util.Date();
                        String fechaActual = df.format(fechaAct);
                        fechaActual.split(fechaActual, 11);
                        fechaAct = df.parse(fechaActual);

                        if ( ultimaEntrada.after(fechaAct)) {
                            return "La ultima fecha de entrada no puede ser mayor a la fecha actual.";
                        }else
                        if( ultimaSalida.before(fechaAct) && !ultimaSalida.equals(fechaAct)) {
                            return "La ultima fecha de salida no puede ser menor a la fecha actual.";
                        }else
                        if( ultimaSalida.before(ultimaEntrada) && !ultimaSalida.equals(fechaAct)) {
                            return "La ultima fecha de salida no puede ser menor a la ultima fecha de entrada.";
                        }

                    }catch(Exception e){
                        return "Fecha no válida";
                    }
                    
                    String sql="UPDATE PRODUCTO SET NOMBRE='"+nombre+"', DESCRIPCION='"+descripcion+"', "
                        + "ULTIMA_ENTRADA=TO_DATE('"+ultimaEntrada+"','yyyy-mm-dd'), ULTIMA_SALIDA=TO_DATE('"+ultimaSalida+"','yyyy-mm-dd') "
                        + "WHERE ID_PRODUCTO = '"+idProducto+"' AND ESTADO='A'";         
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
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
//        return "El id del producto #"+id_Producto+" fue actualizado correctamente";
        return "Se actualizo correctamente";
    }
    
    public String eliminarProducto(int idProducto)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM PRODUCTO WHERE ID_PRODUCTO = '"+idProducto+"' AND ESTADO='A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE PRODUCTO SET ESTADO='I' WHERE ID_PRODUCTO = '"+idProducto+"'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate(); 
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
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
//        return "El id del producto #"+id_Producto+" fue eliminado correctamente";
        return "Se elimino correctamente";
    }
}

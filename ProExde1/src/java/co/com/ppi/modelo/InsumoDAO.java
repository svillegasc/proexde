/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.Insumo;
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
public class InsumoDAO {
    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarInsumo(/*int idInsumo,*/String nombre,String descripcion,
                                 int precioCompra,String unidadMedida,String ultimaEnt,String ultimaSal){
        String sql="INSERT INTO INSUMO VALUES(?,?,?,?,?,?,?,?,?)";
        Date ultimaEntradaP = null;
        Date ultimaSalidaP = null;
        java.sql.Date ultimaEntrada;
        java.sql.Date ultimaSalida;
        try
        {
            if( nombre == null || "".trim().equals(nombre)){
                return "Falta el nombre.";
            }else
            if( descripcion == null || "".trim().equals(descripcion)){
                return "Falta la descripcion.";
            }else
            if( precioCompra == -1){
                return "Falta el precio de compra";
            }else
            if( unidadMedida == null || "".trim().equals(unidadMedida)){
                return "Falta la unidad de medida.";
            }else
            if( ultimaEnt == null || "".equals(ultimaEnt)){
                return "Falta la fecha de ultima entrada.";
            }else
            if( ultimaSal == null || "".equals(ultimaSal)){
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
                if( ultimaSalida.before(ultimaEntrada)) {
                    return "La ultima fecha de salida no puede ser menor a la ultima fecha de entrada.";
                }

            }catch(ParseException e){
                return "Fecha no válida";
            }

            String sqlI="SELECT INS.NEXTVAL FROM DUAL";
            int idInsumo = 0;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                idInsumo = rs.getInt("NEXTVAL");
            }

            con=conex.conexion();
            pr=con.prepareStatement(sql);

            pr=con.prepareStatement(sql);
            pr.setInt(1,idInsumo);
            pr.setString(2,nombre);
            pr.setString(3,descripcion);
            pr.setInt(4,precioCompra);
            pr.setString(5,unidadMedida);
            pr.setInt(6,0);
            pr.setDate(7,ultimaEntrada);
            pr.setDate(8,ultimaSalida);
            pr.setString(9,"A");
            pr.executeUpdate();
        }
        catch (SQLException ex) {
            Logger.getLogger(InsumoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
        return "Se inserto correctamente";
    }
    
    public ArrayList<Insumo>consultarInsumos(String sel, String cam, String val, String ord)
    {
        ArrayList<Insumo> result = new ArrayList<> ();
        Insumo insumo = new Insumo();
        
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
               sql.append(" FROM INSUMO ");
           }else{
               insumo.setIdInsumo(-1);
               insumo.setDescripcion("ERROR: Faltan los campos a seleccionar en la consulta.");
               result.add(insumo);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if ("ID_INSUMO".equals(campos[i]) || "ESTADO".equals(campos[i])){
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
                    insumo.setIdInsumo(-1);
                    insumo.setDescripcion("ERROR: Los campos y los valores no tienen el mismo número de argumentos.");
                    result.add(insumo);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                insumo.setIdInsumo(-1);
                insumo.setDescripcion("ERROR: Los campos y los valores no tienen el mismo número de argumentos.");
                result.add(insumo);
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
               Insumo i = new Insumo();
               if(select.length == 1 && "*".trim().equals(select[0])){
                   i.setIdInsumo(rs.getInt("ID_INSUMO"));
                   i.setNombre(rs.getString("NOMBRE"));
                   i.setDescripcion(rs.getString("DESCRIPCION"));
                   i.setPrecioCompra(rs.getInt("PRECIO_COMPRA"));
                   i.setUnidadMedida(rs.getString("UNIDAD_MEDIDA"));
                   i.setStock(rs.getInt("STOCK"));
                   i.setUltimaEntrada(rs.getDate("ULTIMA_ENTRADA"));
                   i.setUltimaSalida(rs.getDate("ULTIMA_SALIDA"));
                   i.setEstado(rs.getString("ESTADO"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if("ID_INSUMO".equalsIgnoreCase(select[j])){
                            i.setIdInsumo(rs.getInt("ID_INSUMO"));
                        }  
                        if("NOMBRE".equalsIgnoreCase(select[j])){
                            i.setNombre(rs.getString("NOMBRE"));
                        }
                        if("DESCRIPCION".equalsIgnoreCase(select[j])){
                            i.setDescripcion(rs.getString("DESCRIPCION"));
                        }
                        if("PRECIO_COMPRA".equalsIgnoreCase(select[j])){
                            i.setPrecioCompra(rs.getInt("PRECIO_COMPRA"));
                        }
                        if("UNIDAD_MEDIDA".equalsIgnoreCase(select[j])){
                            i.setUnidadMedida(rs.getString("UNIDAD_MEDIDA"));
                        }
                        if("STOCK".equalsIgnoreCase(select[j])){
                            i.setStock(rs.getInt("STOCK"));
                        }
                        if("ULTIMA_ENTRADA".equalsIgnoreCase(select[j])){
                            i.setUltimaEntrada(rs.getDate("ULTIMA_ENTRADA"));
                        }
                        if("ULTIMA_SALIDA".equalsIgnoreCase(select[j])){
                            i.setUltimaSalida(rs.getDate("ULTIMA_SALIDA"));
                        }
                        if("ESTADO".equalsIgnoreCase(select[j])){
                            i.setEstado(rs.getString("ESTADO"));
                        }
                    }
               }

               result.add(i);            
           }
        }
        catch(SQLException ex){
            Logger.getLogger(InsumoDAO.class.getName()).log(Level.SEVERE, null, ex);
            insumo.setIdInsumo(-1);
            insumo.setDescripcion(ex.getMessage());
            result.add(insumo);
            return result;
        }
       finally{
           try{
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(InsumoDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public ArrayList<Insumo> consultarInsumo(int idInsumo)
    {
        String sql="SELECT NOMBRE,DESCRIPCION,PRECIO_COMPRA,UNIDAD_MEDIDA,STOCK,ULTIMA_ENTRADA,ULTIMA_SALIDA "
                   + "FROM INSUMO WHERE ID_INSUMO='"+idInsumo+"' AND ESTADO='A'";
        ArrayList<Insumo> result = new ArrayList<>();
        Insumo i = new Insumo();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               i.setIdInsumo(idInsumo);
               i.setNombre(rs.getString("NOMBRE"));
               i.setDescripcion(rs.getString("DESCRIPCION"));
               i.setPrecioCompra(rs.getInt("PRECIO_COMPRA"));
               i.setUnidadMedida(rs.getString("UNIDAD_MEDIDA"));
               i.setStock(rs.getInt("STOCK"));
               i.setUltimaEntrada(rs.getDate("ULTIMA_ENTRADA"));
               i.setUltimaSalida(rs.getDate("ULTIMA_SALIDA"));
               result.add(i);
               
           }
        }
        catch(SQLException ex)
       {Logger.getLogger(InsumoDAO.class.getName()).log(Level.SEVERE, null, ex);}
       finally
       {
           try
           {
               rs.close();
               pr.close();
               con.close();
           }
           catch(SQLException ex){
               Logger.getLogger(InsumoDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public String actualizarInsumo(int idInsumo,String nombre,String descripcion,
                                   int precioCompra,String unidadMedida,
                                   String ultimaEnt,String ultimaSal){
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM INSUMO WHERE ID_INSUMO = ? AND ESTADO='A'";
            Date ultimaEntradaA;
            Date ultimaSalidaA;
            java.sql.Date ultimaEntrada;
            java.sql.Date ultimaSalida;
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, idInsumo);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    
                    if( nombre == null || "".trim().equals(nombre)){
                        return "Falta el nombre.";
                    }else
                    if( descripcion == null || "".trim().equals(descripcion)){
                        return "Falta la descripcion.";
                    }else
                    if( precioCompra == -1){
                        return "Falta el precio de compra";
                    }else
                    if( unidadMedida == null || "".trim().equals(unidadMedida)){
                        return "Falta la unidad de medida.";
                    }else
                    if( ultimaEnt == null || "".equals(ultimaEnt)){
                        return "Falta la fecha de ultima entrada.";
                    }else
                    if( ultimaSal == null || "".equals(ultimaSal)){
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
                        if( ultimaSalida.before(ultimaEntrada)) {
                            return "La ultima fecha de salida no puede ser menor a la ultima fecha de entrada.";
                        }

                    }catch(ParseException e){
                        return "Fecha no válida";
                    }
                    
                    String sql="UPDATE INSUMO SET NOMBRE='"+nombre+"', DESCRIPCION='"+descripcion+"', PRECIO_COMPRA='"+precioCompra+"', "
                    + "UNIDAD_MEDIDA='"+unidadMedida+"', ULTIMA_ENTRADA=TO_DATE('"+ultimaEntrada+"','yyyy-mm-dd'), ULTIMA_SALIDA=TO_DATE('"+ultimaSalida+"', 'yyyy-mm-dd') "
                    + "WHERE ID_INSUMO = ? AND ESTADO='A'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setInt(1, idInsumo);
                    pr.executeUpdate();
                }else{
                    return "0 filas actualizadas";
                }
            }
        }
        catch(SQLException ex){
            Logger.getLogger(InsumoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
       }   
       return "Se actualizo correctamente";
    }
    
    public String eliminarInsumo(int idInsumo)
    {
        try{
            String sqlI="SELECT COUNT (*) CONT FROM INSUMO WHERE ID_INSUMO = ? AND ESTADO='A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, idInsumo);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE INSUMO SET ESTADO='I' WHERE ID_INSUMO = ?";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setInt(1, idInsumo);
                    pr.executeUpdate();
                }else{
                    return "0 filas eliminadas";
                }
            }
        }catch(SQLException ex){
            Logger.getLogger(InsumoDAO.class.getName()).log(Level.SEVERE, null, ex);
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
               Logger.getLogger(InsumoDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }    
        return "Se elimino correctamente";
    }
}


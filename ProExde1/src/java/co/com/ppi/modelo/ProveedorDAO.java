/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.modelo;

import co.com.ppi.entidades.Proveedor;
import co.com.ppi.util.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author SANTI
 */
public class ProveedorDAO {

    private Connection con=null;
    private PreparedStatement pr=null;
    private ResultSet rs=null;
    Conexion conex = new Conexion();
    
    public String insertarProveedor(/*int idProveedor,*/String nombreEmpresa,String nit,
                                    String direccion,String telefono,String nombreContacto,
                                    String email)
    {
        String sql="INSERT INTO PROVEEDOR VALUES(?,?,?,?,?,?,?,?)";
        try
        {
            boolean valEmail = validarEmail(email);
            boolean valNit = validarNit(nit);
            if( nombreEmpresa == null || nombreEmpresa.trim().equals("")){
                return "Falta el nombre de la empresa.";
            }else
            if( nit == null || nit.trim().equals("")){
                return "Falta el nit.";
            }else
            if( valNit == false){
                return "Nit ya existente";
            }else
            if( direccion == null || direccion.equals("")){
                return "Falta la dirección.";
            }else
            if( telefono == null || telefono.equals("")){
                return "Falta el télefono.";
            }else
            if( nombreContacto == null || nombreContacto.equals("")){
                return "Falta el nombre del contacto.";
            }else
            if( email == null || email.equals("")){
                return "Falta el email.";
            }else
            if( valEmail == false){
                return "Email invalido";
            }
            
            String sqlI="SELECT PROV.NEXTVAL FROM DUAL";
            int idProveedor = 0; 
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                idProveedor = rs.getInt("NEXTVAL");
            }

            con=conex.conexion();
            pr=con.prepareStatement(sql);

            pr=con.prepareStatement(sql);
            pr.setInt(1,idProveedor);
            pr.setString(2,nombreEmpresa);
            pr.setString(3,nit);
            pr.setString(4,direccion);
            pr.setString(5,telefono);
            pr.setString(6,nombreContacto);
            pr.setString(7,email);
            pr.setString(8,"A");
            pr.executeUpdate();
        }
        catch (SQLException ex) {
            Logger.getLogger(ProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
        return "Se inserto correctamente";
    }
   
    public ArrayList<Proveedor> consultarProveedores(String sel, String cam, String val, String ord)
    {
        ArrayList<Proveedor> result = new ArrayList<> ();
        Proveedor proveedor = new Proveedor();
        
        String seleccionar = sel != null && !sel.trim().equals("") ? sel : "";
        String[] campos = cam != null && !cam.trim().equals("") ? cam.split(",") : null;
        String[] valores = val != null && !val.trim().equals("") ? val.split(",") : null;
        String orden = ord != null && !ord.trim().equals("") ? ord : "";
        
        try
        {
           con=conex.conexion();
           //String sql="SELECT ID_PROVEEDOR,NOMBRE,DESCRIPCION,UNIDAD_MEDIDA FROM PROVEEDORES WHERE ESTADO=0";
           StringBuilder sql = new StringBuilder();
           
           
           if( !seleccionar.equals("") ){
               sql.append("SELECT ");
               sql.append(seleccionar);
               sql.append(" FROM PROVEEDOR ");
           }else{
               proveedor.setIdProveedor(-1);
               proveedor.setNit("ERROR: Faltan los campos a seleccionar en la consulta.");
               result.add(proveedor);
               return result;
           }
           
           if ( campos != null && valores != null ){
               if (campos.length == valores.length){
                   if (campos.length > 0){
                   
                        sql.append("WHERE ");

                        for (int i = 0; i < campos.length; i++) {
                            if (campos[i].equals("ID_PROVEEDOR") || campos[i].equals("ESTADO")){
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
                    proveedor.setIdProveedor(-1);
                    proveedor.setNit("ERROR: Los campos y los valores no tienen el mismo número de argumentos.");
                    result.add(proveedor);
                    return result;
                }        
           }else
           if( (campos != null && valores == null) || (campos == null && valores != null) ){
                proveedor.setIdProveedor(-1);
                proveedor.setNit("ERROR: Los campos y los valores no tienen el mismo número de argumentos.");
                result.add(proveedor);
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
               Proveedor i = new Proveedor();
               
               if(select.length == 1 && select[0].trim().equals("*")){
                   i.setIdProveedor(rs.getInt("ID_PROVEEDOR"));
                   i.setNombre(rs.getString("NOMBRE"));
                   i.setNit(rs.getString("NIT"));
                   i.setDireccion(rs.getString("DIRECCION"));
                   i.setTelefono(rs.getString("TELEFONO"));
                   i.setNombreContacto(rs.getString("NOMBRE_CONTACTO"));
                   i.setEmail(rs.getString("EMAIL"));
                   i.setEstado(rs.getString("ESTADO"));
               }else{ 
                    for (int j = 0; j < select.length; j++) {
                        if(select[j].toUpperCase().equals("ID_PROVEEDOR")){
                            i.setIdProveedor(rs.getInt("ID_PROVEEDOR"));
                        }  
                        if(select[j].toUpperCase().equals("NOMBRE")){
                            i.setNombre(rs.getString("NOMBRE"));
                        }
                        if(select[j].toUpperCase().equals("NIT")){
                            i.setNit(rs.getString("NIT"));
                        }
                        if(select[j].toUpperCase().equals("DIRECCION")){
                            i.setDireccion(rs.getString("DIRECCION"));
                        }
                        if(select[j].toUpperCase().equals("TELEFONO")){
                            i.setTelefono(rs.getString("TELEFONO"));
                        }
                        if(select[j].toUpperCase().equals("NOMBRE_CONTACTO")){
                            i.setNombreContacto(rs.getString("NOMBRE_CONTACTO"));
                        }
                        if(select[j].toUpperCase().equals("EMAIL")){
                            i.setEmail(rs.getString("EMAIL"));
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
            Logger.getLogger(ProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
            proveedor.setIdProveedor(-1);
            proveedor.setNit(ex.getMessage());
            result.add(proveedor);
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
    
    public ArrayList<Proveedor> consultarProveedor(int idProveedor)
    {
        String sql="SELECT NOMBRE,NIT,DIRECCION,TELEFONO,NOMBRE_CONTACTO,EMAIL "
                   + "FROM PROVEEDOR WHERE ID_PROVEEDOR='"+idProveedor+"' AND ESTADO='A'";
        ArrayList<Proveedor> result = new ArrayList<>();
        Proveedor p = new Proveedor();
        try
        {
           con=conex.conexion();
      
           pr=con.prepareStatement(sql);
           rs=pr.executeQuery();
           
           if ( rs.next() )
           {             
               p.setIdProveedor(idProveedor);
               p.setNombre(rs.getString("NOMBRE"));
               p.setNit(rs.getString("NIT"));
               p.setDireccion(rs.getString("DIRECCION"));
               p.setTelefono(rs.getString("TELEFONO"));
               p.setNombreContacto(rs.getString("NOMBRE_CONTACTO"));
               p.setEmail(rs.getString("EMAIL"));
               result.add(p);
               
           }
        }
        catch(Exception ex)
       {Logger.getLogger(ProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);}
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
    
    public String actualizarProveedor(int idProveedor,String nombreEmpresa,String nit,
                                      String direccion,String telefono,String nombreContacto,
                                      String email)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM PROVEEDOR WHERE ID_PROVEEDOR = '"+idProveedor+"' AND ESTADO='A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    boolean valEmail = validarEmail(email);
                    if( nombreEmpresa == null || nombreEmpresa.trim().equals("")){
                        return "Falta el nombre de la empresa.";
                    }else
                    if( nit == null || nit.trim().equals("")){
                        return "Falta el nit.";
                    }else
                    if( direccion == null || direccion.equals("")){
                        return "Falta la dirección.";
                    }else
                    if( telefono == null || telefono.equals("")){
                        return "Falta el télefono.";
                    }else
                    if( nombreContacto == null || nombreContacto.equals("")){
                        return "Falta el nombre del contacto.";
                    }else
                    if( email == null || email.equals("")){
                        return "Falta el email.";
                    }else
                    if( valEmail == false){
                        return "Email invalido";
                    }
                    String sql="UPDATE PROVEEDOR SET NOMBRE='"+nombreEmpresa+"', NIT='"+nit+"', "
                            + "DIRECCION='"+direccion+"', TELEFONO='"+telefono+"', NOMBRE_CONTACTO='"+nombreContacto+"', "
                            + "EMAIL='"+email+"' "
                            + "WHERE ID_PROVEEDOR = '"+idProveedor+"' AND ESTADO='A'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
            Logger.getLogger(ProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
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
//        return "El id del proveedor #"+id_Proveedor+" fue actualizado correctamente";
        return "Se actualizo correctamente";
    }
    
    public String eliminarProveedor(int idProveedor)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM PROVEEDOR WHERE ID_PROVEEDOR = '"+idProveedor+"' AND ESTADO='A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE PROVEEDOR SET ESTADO='I' WHERE ID_PROVEEDOR = '"+idProveedor+"'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(Exception ex){
            Logger.getLogger(ProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
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
//        return "El id del proveedor #"+id_Proveedor+" fue eliminado correctamente";
        return "Se elimino correctamente";
    }
    
    public boolean validarEmail(String email){
        String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
           + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        
        System.out.println(matcher.matches());
        return matcher.matches();
    }
    
    public boolean validarNit(String nit){
        try {
            String sql = "SELECT COUNT(*) CONT FROM PROVEEDOR WHERE NIT = '"+nit+"'";
            con=conex.conexion();
            pr=con.prepareStatement(sql);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    return false;
                }
            }       
        } catch (SQLException ex) {
            Logger.getLogger(ProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}

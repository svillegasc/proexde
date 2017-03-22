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
            if( nombreEmpresa == null || "".trim().equals(nombreEmpresa)){
                return "Falta el nombre de la empresa.";
            }else
            if( nit == null || "".trim().equals(nit)){
                return "Falta el nit.";
            }else
            if( !valNit){
                return "Nit ya existente";
            }else
            if( direccion == null || "".equals(direccion)){
                return "Falta la dirección.";
            }else
            if( telefono == null || "".equals(telefono)){
                return "Falta el télefono.";
            }else
            if( nombreContacto == null || "".equals(nombreContacto)){
                return "Falta el nombre del contacto.";
            }else
            if( email == null || "".equals(email)){
                return "Falta el email.";
            }else
            if(!valEmail){
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
                            if ("ID_PROVEEDOR".equals(campos[i]) || "ESTADO".equals(campos[i])){
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
               
               if(select.length == 1 && "*".trim().equals(select[0])){
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
                        if("ID_PROVEEDOR".equalsIgnoreCase(select[j])){
                            i.setIdProveedor(rs.getInt("ID_PROVEEDOR"));
                        }  
                        if("NOMBRE".equalsIgnoreCase(select[j])){
                            i.setNombre(rs.getString("NOMBRE"));
                        }
                        if("NIT".equalsIgnoreCase(select[j])){
                            i.setNit(rs.getString("NIT"));
                        }
                        if("DIRECCION".equalsIgnoreCase(select[j])){
                            i.setDireccion(rs.getString("DIRECCION"));
                        }
                        if("TELEFONO".equalsIgnoreCase(select[j])){
                            i.setTelefono(rs.getString("TELEFONO"));
                        }
                        if("NOMBRE_CONTACTO".equalsIgnoreCase(select[j])){
                            i.setNombreContacto(rs.getString("NOMBRE_CONTACTO"));
                        }
                        if("EMAIL".equalsIgnoreCase(select[j])){
                            i.setEmail(rs.getString("EMAIL"));
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
           catch(SQLException ex){
               Logger.getLogger(ProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
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
        catch(SQLException ex)
       {Logger.getLogger(ProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);}
       finally
       {
           try
           {
               rs.close();
               pr.close();
               con.close();
           }
           catch(Exception ex){
                Logger.getLogger(ProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return result;
    }
    
    public String actualizarProveedor(int idProveedor,String nombreEmpresa,String nit,
                                      String direccion,String telefono,String nombreContacto,
                                      String email)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM PROVEEDOR WHERE ID_PROVEEDOR = ? AND ESTADO='A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, idProveedor);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    boolean valEmail = validarEmail(email);
                    boolean valNit = validarNit(nit);
                    if( idProveedor == -1){
                        return "Falta el id proveedor.";
                    }else
                    if( nombreEmpresa == null || "".trim().equals(nombreEmpresa)){
                        return "Falta el nombre de la empresa.";
                    }else
                    if( nit == null || "".trim().equals(nit)){
                        return "Falta el nit.";
                    }else
                    if(!valNit){
                        return "Nit ya existente";
                    }else
                    if( direccion == null || "".equals(direccion)){
                        return "Falta la dirección.";
                    }else
                    if( telefono == null || "".equals(telefono)){
                        return "Falta el télefono.";
                    }else
                    if( nombreContacto == null || "".equals(nombreContacto)){
                        return "Falta el nombre del contacto.";
                    }else
                    if( email == null || "".equals(email)){
                        return "Falta el email.";
                    }else
                    if(!valEmail){
                        return "Email invalido";
                    }
                    String sql="UPDATE PROVEEDOR SET NOMBRE=?, NIT=?, "
                            + "DIRECCION=?, TELEFONO=?, NOMBRE_CONTACTO=?, "
                            + "EMAIL=? "
                            + "WHERE ID_PROVEEDOR = ? AND ESTADO='A'";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setString(1, nombreEmpresa);
                    pr.setString(2, nit);
                    pr.setString(3, direccion);
                    pr.setString(4, telefono);
                    pr.setString(5, nombreContacto);
                    pr.setString(6, email);
                    pr.setInt(7, idProveedor);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(SQLException ex){
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
           catch(SQLException ex){
               Logger.getLogger(ProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }    
        return "Se actualizo correctamente";
    }
    
    public String eliminarProveedor(int idProveedor)
    {
        try
        {
            String sqlI="SELECT COUNT (*) CONT FROM PROVEEDOR WHERE ID_PROVEEDOR = ? AND ESTADO='A'";
            con=conex.conexion();
            pr=con.prepareStatement(sqlI);
            pr.setInt(1, idProveedor);
            rs=pr.executeQuery();
            if(rs.next()){
                if (rs.getInt("CONT")!= 0) {
                    String sql="UPDATE PROVEEDOR SET ESTADO='I' WHERE ID_PROVEEDOR = ?";
                    con=conex.conexion();
                    pr=con.prepareStatement(sql);
                    pr.setInt(1, idProveedor);
                    pr.executeUpdate();
                }else{
                    return "0 filas encontradas";
                }
            }
        }
        catch(SQLException ex){
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
           catch(SQLException ex){
               Logger.getLogger(ProveedorDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }    
        return "Se elimino correctamente";
    }
    
    public boolean validarEmail(String email){
        String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
           + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        
        return matcher.matches();
    }
    
    public boolean validarNit(String nit){
        try {
            String sql = "SELECT COUNT(*) CONT FROM PROVEEDOR WHERE NIT = ?";
            con=conex.conexion();
            pr=con.prepareStatement(sql);
            pr.setString(1, nit);
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

package co.com.ppi.util;

import co.com.ppi.util.Conexion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public static class Validador {

    private Connection con = null;
    private PreparedStatement pr = null;
    private ResultSet rs = null;
    Conexion conex = new Conexion();
    
    
    public boolean validar_token(String token)throws Exception{
        
        CallableStatement sentencia = con.prepareCall("{?=call validar_token(?)}");
        sentencia.registerOutParameter(1, Types.INTEGER ); 
        sentencia.setString(2,token);                                             
        sentencia.executeQuery(); 
        
        if(sentencia.getInt(1) != -1){
            actualizarFechaToken(token);
            return true;
        }else{
            return false;
        }    
    }
    
    
    public boolean actualizarFechaToken(String token)throws Exception{
       
        con = conex.conexion();
        pr = con.prepareStatement("UPDATE usuario u set u.token_fecha = ? WHERE u.token = ?");
        pr.setString(1,formatoFechaToken());
        pr.setString(2,token);
        pr.executeUpdate();
        return true;
    }


    public String formatoFechaToken()throws Exception{       
        
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmm");
        java.util.Date fechaAct = new java.util.Date();
        return df.format(fechaAct);     
    }

   
    public String crearToken()throws Exception{

        String sql= "SELECT (dbms_random.string('X',15)) token FROM dual";
        con=conex.conexion();
        pr = con.prepareStatement(sql);
        rs = pr.executeQuery();

        return rs.next() ? rs.getString("token") : null;
    }
    
    
    public String getMensajeToken(){
        return "La sesión ha expirado.";
    }

}

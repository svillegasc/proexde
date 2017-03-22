/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.util;

import co.com.ppi.modelo.ProveedorDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SANTI
 */
public class Conexion {
    
    public Connection conexion() 
    {
        Connection conex = null;
        try 
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            conex = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "proexde", "root");
            System.err.println("Se establecio la conexión");
        } catch (ClassNotFoundException e1) {
            System.err.println("ERROR:No encuentro el driver de la BD: " + e1.getMessage());
            Logger.getLogger(ProveedorDAO.class.getName()).log(Level.SEVERE, null, e1);
        } catch (SQLException e2) {
            System.err.println("ERROR:Fallo en SQL: " + e2.getMessage());
            Logger.getLogger(ProveedorDAO.class.getName()).log(Level.SEVERE, null, e2);
        }
        return conex;
    }
}

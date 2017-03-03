/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

            //conex = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "proExdeR", "snow");
            conex = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "proexde", "root");
            System.out.println("Se establecio la conexión");
        } catch (ClassNotFoundException e1) {
            System.out.println("ERROR:No encuentro el driver de la BD: " + e1.getMessage());
        } catch (SQLException e2) {
            System.out.println("ERROR:Fallo en SQL: " + e2.getMessage());
        }
        return conex;
    }
}

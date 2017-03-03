/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author SANTI
 */
@javax.ws.rs.ApplicationPath("ws")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
            return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(co.com.ppi.servicio.CotizacionREST.class);
        resources.add(co.com.ppi.servicio.DetalleCotizacionClienteREST.class);
        resources.add(co.com.ppi.servicio.DetallePedidoClienteREST.class);
        resources.add(co.com.ppi.servicio.DetallePedidoProveedorREST.class);
        resources.add(co.com.ppi.servicio.DetallePermisoREST.class);
        resources.add(co.com.ppi.servicio.EstadoProduccionREST.class);
        resources.add(co.com.ppi.servicio.InsumoREST.class);
        resources.add(co.com.ppi.servicio.PedidoProveedorREST.class);
        resources.add(co.com.ppi.servicio.PedidoREST.class);
        resources.add(co.com.ppi.servicio.PerfilREST.class);
        resources.add(co.com.ppi.servicio.PermisoREST.class);
        resources.add(co.com.ppi.servicio.ProductoREST.class);
        resources.add(co.com.ppi.servicio.ProveedorInsumoREST.class);
        resources.add(co.com.ppi.servicio.ProveedorREST.class);
        resources.add(co.com.ppi.servicio.RecetaREST.class);
        resources.add(co.com.ppi.servicio.TipoIdentificacionREST.class);
        resources.add(co.com.ppi.servicio.UsuarioREST.class);
    }
    
}

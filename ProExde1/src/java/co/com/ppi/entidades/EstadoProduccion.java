/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.entidades;

/**
 *
 * @author SANTI
 */
public class EstadoProduccion {
    private int estadoProduccion = -1;
    private String descripcion = null;
    private String mensajeError = null;

    public int getEstadoProduccion() {
        return estadoProduccion;
    }

    public void setEstadoProduccion(int estadoProduccion) {
        this.estadoProduccion = estadoProduccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    
    
}

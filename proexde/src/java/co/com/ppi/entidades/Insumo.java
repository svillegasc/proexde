/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ppi.entidades;

import java.sql.Date;

/**
 *
 * @author SANTI
 */
public class Insumo {
    private int idInsumo = -1;
    private String nombre = null;
    private String descripcion = null;
    private int precioCompra = -1;
    private String unidadMedida = null;
    private int stock = -1;
    private Date ultimaEntrada = null;
    private Date ultimaSalida = null;
    private String estado = null;

    public int getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(int idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(int precioCompra) {
        this.precioCompra = precioCompra;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getUltimaEntrada() {
        return ultimaEntrada;
    }

    public void setUltimaEntrada(Date ultimaEntrada) {
        this.ultimaEntrada = ultimaEntrada;
    }

    public Date getUltimaSalida() {
        return ultimaSalida;
    }

    public void setUltimaSalida(Date ultimaSalida) {
        this.ultimaSalida = ultimaSalida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
    
}

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
public class PedidoProveedor {
    private int idPedidoProveedor = -1;
    private int idProveedor = -1;
    private Date fechaPedido;
    private int estado_produccion = -1;
    private int total;

    public int getIdPedidoProveedor() {
        return idPedidoProveedor;
    }

    public void setIdPedidoProveedor(int idPedidoProveedor) {
        this.idPedidoProveedor = idPedidoProveedor;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public int getEstado_produccion() {
        return estado_produccion;
    }

    public void setEstado_produccion(int estado_produccion) {
        this.estado_produccion = estado_produccion;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    
    
    
}

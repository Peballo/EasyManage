package org.example.easymanage.Modelo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Producto {

    private PropertyChangeSupport propertyChangeSupport;
    private String id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;

    public Producto() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public Producto(String nombre, String descripcion, Double precio, Integer stock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombreNuevo) {
        String oldNombre = nombre;
        nombre = nombreNuevo;
        propertyChangeSupport.firePropertyChange("nombre", oldNombre, nombre);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcionNuevo) {
        String oldDescripcion = descripcion;
        descripcion = descripcionNuevo;
        propertyChangeSupport.firePropertyChange("descripcion", oldDescripcion, descripcion);
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precioNuevo) {
        Double oldPrecio = precio;
        precio = precioNuevo;
        propertyChangeSupport.firePropertyChange("precio", oldPrecio, precio);
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stockNuevo) {
        Integer oldStock = stock;
        stock = stockNuevo;
        propertyChangeSupport.firePropertyChange("stock", oldStock, stock);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


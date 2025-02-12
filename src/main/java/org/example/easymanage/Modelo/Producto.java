package org.example.easymanage.Modelo;

import javafx.beans.property.*;

public class Producto {
    private final StringProperty id;
    private final StringProperty nombre;
    private final StringProperty descripcion;
    private final DoubleProperty precio;
    private final IntegerProperty stock;

    public Producto(String nombre, String descripcion, double precio, int stock) {
        this.id = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty(nombre);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.precio = new SimpleDoubleProperty(precio);
        this.stock = new SimpleIntegerProperty(stock);
    }

    // Métodos getters y setters
    public StringProperty idProperty() {
        return id;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public double getPrecio() {
        return precio.get();
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    public IntegerProperty stockProperty() {
        return stock;
    }

    public int getStock() {
        return stock.get();
    }

    public void setStock(int stock) {
        this.stock.set(stock);
    }
}


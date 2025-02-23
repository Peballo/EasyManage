package org.example.easymanage.Modelo;

import javafx.beans.property.*;
import java.util.Date;

public class Cliente {
    private final StringProperty id;
    private final StringProperty nombre;
    private final StringProperty direccion;
    private final StringProperty telefono;
    private final StringProperty email;
    private final ObjectProperty<Date> fechaDeRegistro;

    public Cliente(String nombre, String direccion, String telefono, String email, Date fechaDeRegistro) {
        this.id = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty(nombre);
        this.direccion = new SimpleStringProperty(direccion);
        this.telefono = new SimpleStringProperty(telefono);
        this.email = new SimpleStringProperty(email);
        this.fechaDeRegistro = new SimpleObjectProperty<>(fechaDeRegistro);
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

    public StringProperty direccionProperty() {
        return direccion;
    }

    public String getDireccion() {
        return direccion.get();
    }

    public void setDireccion(String direccion) {
        this.direccion.set(direccion);
    }

    public StringProperty telefonoProperty() {
        return telefono;
    }

    public String getTelefono() {
        return telefono.get();
    }

    public void setTelefono(String telefono) {
        this.telefono.set(telefono);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public ObjectProperty<Date> fechaDeRegistroProperty() {
        return fechaDeRegistro;
    }

    public Date getFechaDeRegistro() {
        return fechaDeRegistro.get();
    }

    public void setFechaDeRegistro(Date fechaDeRegistro) {
        this.fechaDeRegistro.set(fechaDeRegistro);
    }
}
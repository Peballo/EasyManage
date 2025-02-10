package org.example.easymanage.Modelo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

public class Cliente {

    private PropertyChangeSupport propertyChangeSupport;

    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private Date fechaDeRegistro;

    public Cliente() {}

    public Cliente(String nombre, String direccion, String telefono, String email, Date fechaDeRegistro) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.fechaDeRegistro = new Date();
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombreNuevo) {
        String nombreViejo = this.nombre;
        this.nombre = nombreNuevo;

        propertyChangeSupport.firePropertyChange("name", nombreViejo, nombreNuevo);
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccionNuevo) {
        String direccionViejo = this.direccion;
        this.direccion = direccionNuevo;

        propertyChangeSupport.firePropertyChange("direccion", direccionViejo, direccionNuevo);
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefonoNuevo) {
        String telefonoViejo = this.telefono;
        this.telefono = telefonoNuevo;
        propertyChangeSupport.firePropertyChange("telefono", telefonoViejo, telefonoNuevo);
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String emailNuevo) {
        String emailViejo = this.email;
        this.email = emailNuevo;
        propertyChangeSupport.firePropertyChange("email", emailViejo, emailNuevo);
    }
    public Date getFechaDeRegistro() {
        return fechaDeRegistro;
    }
    public void setFechaDeRegistro(Date fechaDeRegistroNuevo) {
        Date fechaDeRegistroViejo = this.fechaDeRegistro;
        this.fechaDeRegistro = fechaDeRegistroNuevo;
        propertyChangeSupport.firePropertyChange("fecha", fechaDeRegistroViejo, fechaDeRegistroNuevo);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}

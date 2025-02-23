package org.example.easymanage.Logica;

import com.mongodb.client.MongoDatabase;
import org.example.easymanage.DAO.ConnectionsDB.ClienteDAOConnector;
import org.example.easymanage.DAO.ConnectionsDB.MongoDBConnector;
import org.example.easymanage.Modelo.Cliente;

import java.util.List;


public class ClienteLogica {
    private MongoDatabase database;
    private ClienteDAOConnector clienteDAO;

    public ClienteLogica() {
        this.database = MongoDBConnector.getDatabase();
        this.clienteDAO = new ClienteDAOConnector();
    }

    private void validarCampo(String campo, String mensajeError) throws IllegalArgumentException {
        if (campo == null || campo.trim().isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    private void validarEmail(String email) throws IllegalArgumentException {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico no puede estar vacío.");
        }

        // Verifica si el correo contiene un @
        if (!email.contains("@")) {
            throw new IllegalArgumentException("El correo electrónico debe contener un @.");
        }

        // Verifica si termina con un dominio válido, es decir, algo que tenga un punto al final
        if (!email.matches(".*@.*\\.[a-zA-Z]+$")) {
            throw new IllegalArgumentException("El correo electrónico debe terminar con un dominio válido.");
        }
    }


    public String insertarCliente(Cliente cliente) throws IllegalArgumentException {
        // Validación
        validarCampo(cliente.getNombre(), "El nombre del cliente está vacío.");
        validarCampo(cliente.getDireccion(), "La dirección del cliente está vacía.");
        validarCampo(cliente.getTelefono(), "El teléfono del cliente está vacío.");
        validarCampo(cliente.getEmail(), "El email del cliente está vacío.");
        validarEmail(cliente.getEmail());
        if (cliente.getFechaDeRegistro() == null) {
            throw new IllegalArgumentException("La fecha de registro del cliente es inválida.");
        }

        // Llamada al DAO para insertar el cliente
        String idGenerado = clienteDAO.insertar(cliente);
        return idGenerado;
    }

    public void actualizarCliente(String id, Cliente cliente) throws IllegalArgumentException {
        // Validaciones
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID seleccionado está vacío.");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no válido.");
        }

        // Llamada al DAO para actualizar cliente
        clienteDAO.actualizar(id, cliente);
    }

    public void eliminarCliente(String id) throws IllegalArgumentException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID seleccionado está vacío.");
        }

        // Llamada al DAO para eliminar el cliente
        clienteDAO.eliminar(id);
    }

    public Cliente buscarCliente(String id) throws IllegalArgumentException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID seleccionado está vacío.");
        }

        return clienteDAO.buscar(id);
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return clienteDAO.obtenerTodos();
    }

    public List<Cliente> obtenerTodosLosProductos() {
        return clienteDAO.obtenerTodos();
    }
}

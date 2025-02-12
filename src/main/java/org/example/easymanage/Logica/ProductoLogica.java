package org.example.easymanage.Logica;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.easymanage.DAO.ConnectionsDB.MongoDBConnector;
import org.example.easymanage.DAO.ConnectionsDB.ProductoDAOConnector;
import org.example.easymanage.Modelo.Producto;

import java.util.ArrayList;
import java.util.List;

public class ProductoLogica {
    private MongoDatabase database;
    private ProductoDAOConnector productoDAO;

    public ProductoLogica() {
        this.database = MongoDBConnector.getDatabase();
        this.productoDAO = new ProductoDAOConnector();
    }

    // Método para insertar producto
    public String insertarProducto(Producto producto) throws IllegalArgumentException {
        // Validación
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto está vacío.");
        }
        if (producto.getDescripcion() == null || producto.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del producto está vacía.");
        }
        if (producto.getPrecio() <= 0) { // Cambiado a obtener el valor de la propiedad
            throw new IllegalArgumentException("El precio del producto es inválido.");
        }
        if (producto.getStock() < 0) { // Cambiado a obtener el valor de la propiedad
            throw new IllegalArgumentException("El stock del producto es inválido.");
        }

        // Llamada al DAO para insertar el producto
        String idGenerado = productoDAO.insertar(producto);
        producto.setId(idGenerado); // Asignamos el ID al producto

        return idGenerado;
    }

    // Método para actualizar producto
    public void actualizarProducto(String id, Producto producto) throws IllegalArgumentException {
        // Validaciones
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID seleccionado está vacío.");
        }

        if (producto == null) {
            throw new IllegalArgumentException("Producto no válido.");
        }

        // Llamada al DAO para actualizar producto
        productoDAO.actualizar(id, producto);
    }

    // Método para eliminar producto
    public void eliminarProducto(String id) throws IllegalArgumentException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID seleccionado está vacío.");
        }

        // Llamada al DAO para eliminar el producto
        productoDAO.eliminar(id);
    }

    // Método para buscar un producto
    public Producto buscarProducto(String id) throws IllegalArgumentException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID seleccionado está vacío.");
        }

        return productoDAO.buscar(id);
    }

    // Método para obtener todos los productos
    public List<Producto> obtenerTodosLosProductos() {
        return productoDAO.obtenerTodos();
    }
}

package org.example.easymanage.Logica;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.easymanage.DAO.ConnectionsDB.MongoDBConnector;
import org.example.easymanage.DAO.ConnectionsDB.ProductoDAOConnector;
import org.example.easymanage.DAO.ProductoDAO;
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


    public void inserterProducto(Producto producto) throws Exception {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new Exception("Error: El nombre del producto está vacío.");
        }
        if (producto.getDescripcion() == null || producto.getDescripcion().trim().isEmpty()) {
            throw new Exception("Error: La descripción del producto está vacía.");
        }
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new Exception("Error: El precio del producto es inválido.");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new Exception("Error: El stock del producto es inválido.");
        }

        productoDAO.insertar(producto);
    }

    public void actualizarProducto(String id, Producto producto) throws Exception {
        if (id == null || id.equals("")) {
            throw new Exception("Error id seleccionado esta vacio.");
        }
        productoDAO.actualizar(id, producto);
    }
    public void eliminarProducto(String id) throws Exception {
        if (id == null || id.equals("")) {
            throw new Exception("Error id seleccionado esta vacio.");
        }
        Producto producto = productoDAO.buscar(id);
    }
    public Producto buscarProducto(String id) throws Exception {
        if (id == null || id.equals("")) {
            throw new Exception("Error id seleccionado esta vacio.");
        }
        return productoDAO.buscar(id);
    }

    public List<Producto> obtenerTodosLosProductos() {
        MongoCollection<Document> collection = database.getCollection("productos");
        List<Producto> productos = new ArrayList<>();

        for (Document doc : collection.find()) {
            Producto producto = new Producto(
                    doc.getString("nombre"),
                    doc.getString("descripcion"),
                    doc.getDouble("precio"),
                    doc.getInteger("stock")
            );
            productos.add(producto);
        }

        return productos;
    }
}

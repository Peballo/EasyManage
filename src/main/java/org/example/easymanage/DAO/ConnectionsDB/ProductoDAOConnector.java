package org.example.easymanage.DAO.ConnectionsDB;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.easymanage.DAO.ProductoDAO;
import org.example.easymanage.Modelo.Producto;

import java.util.ArrayList;
import java.util.List;

public class ProductoDAOConnector implements ProductoDAO {

    private final MongoDBConnector conn = new MongoDBConnector();
    private final MongoDatabase db = conn.getDatabase();
    private final MongoCollection<Document> collection = db.getCollection("productos");

    @Override
    public String insertar(Producto producto) {
        Document doc = new Document("nombre", producto.getNombre())
                .append("descripcion", producto.getDescripcion())
                .append("precio", producto.getPrecio())
                .append("stock", producto.getStock());

        collection.insertOne(doc);

        String idGenerado = doc.getObjectId("_id").toHexString();
        producto.setId(idGenerado); // ✅ Asignamos el ID al objeto producto

        return idGenerado; // ✅ Devolvemos el ID
    }


    @Override
    public void actualizar(String id, Producto producto) {
        collection.updateOne(
                new Document("_id", new org.bson.types.ObjectId(id)), // Buscamos el documento por ID
                new Document("$set", new Document()
                        .append("nombre", producto.getNombre())
                        .append("descripcion", producto.getDescripcion())
                        .append("precio", producto.getPrecio())
                        .append("stock", producto.getStock())
                )
        );
    }

    @Override
    public void eliminar(String id) {
        collection.deleteOne(new Document("_id", new org.bson.types.ObjectId(id)));
    }

    @Override
    public Producto buscar(String id) {
        ObjectId idProducto = new ObjectId(id);
        Document result = collection.find(Filters.eq("_id", idProducto)).first();

        if (result != null) {
            Producto producto = new Producto(
                    result.getString("nombre"),
                    result.getString("descripcion"),
                    result.getDouble("precio"),
                    result.getInteger("stock")
            );
            producto.setId(result.getObjectId("_id").toHexString()); // Asignar el ID recuperado
            return producto;
        }
        return null;
    }

    @Override
    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();

        for (Document doc : collection.find()) {
            Producto producto = new Producto(
                    doc.getString("nombre"),
                    doc.getString("descripcion"),
                    doc.getDouble("precio"),
                    doc.getInteger("stock")
            );
            producto.setId(doc.getObjectId("_id").toHexString()); // ✅ Asignar el ID
            productos.add(producto);
        }

        return productos;

    }


}

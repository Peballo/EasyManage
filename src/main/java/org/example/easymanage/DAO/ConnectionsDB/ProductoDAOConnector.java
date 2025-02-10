package org.example.easymanage.DAO.ConnectionsDB;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.easymanage.DAO.ProductoDAO;
import org.example.easymanage.Modelo.Producto;

public class ProductoDAOConnector implements ProductoDAO {

    MongoDBConnector conn = new MongoDBConnector();
    MongoDatabase db = conn.getDatabase();
    MongoCollection<Document> collection = db.getCollection("productos");

    @Override
    public void insertar(Producto producto) {
        Document doc = new Document();
        doc.append("nombre", producto.getNombre());
        doc.append("descripcion", producto.getDescripcion());
        doc.append("precio", producto.getPrecio());
        doc.append("stock", producto.getStock());

        // Insertar el documento en la colección (MongoDB generará el _id automáticamente)
        collection.insertOne(doc);

        // Obtener el ID generado por MongoDB
        ObjectId generatedId = doc.getObjectId("_id");

        // Asignar el ID al producto usando el método setId()
        producto.setId(generatedId.toHexString());
    }


    @Override
    public void actualizar(String id, Producto producto) {
        ObjectId idProducto = new ObjectId(id);

        Document doc = (Document) Filters.eq("_id", idProducto);

        Document update = new Document("$set", doc)
                .append("nombre", producto.getNombre())
                .append("descripcion", producto.getDescripcion())
                .append("precio", producto.getPrecio())
                .append("stock", producto.getStock());
        collection.updateOne(doc, update);
    }

    @Override
    public void eliminar(String id) {
        ObjectId idProducto = new ObjectId(id);
        Document doc = (Document) Filters.eq("_id", idProducto);
        collection.deleteOne(doc);
    }

    @Override
    public Producto buscar(String id) {
        ObjectId idProducto = new ObjectId(id);
        Document doc = (Document) Filters.eq("_id", idProducto);

        Document result = collection.find(doc).first();

        if (result != null) {
            return new Producto(
                    result.getString("nombre"),
                    result.getString("descripcion"),
                    result.getDouble("precio"),
                    result.getInteger(("stock"))
            );
        }
        return null;
    }
}


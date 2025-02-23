package org.example.easymanage.DAO.ConnectionsDB;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.easymanage.DAO.ClienteDAO;
import org.example.easymanage.Modelo.Cliente;

import java.util.ArrayList;
import java.util.List;



public class ClienteDAOConnector implements ClienteDAO {

    private final MongoDBConnector conn = new MongoDBConnector();
    private final MongoDatabase db = conn.getDatabase();
    private final MongoCollection<Document> collection = db.getCollection("clientes");

    @Override
    public String insertar(Cliente cliente) {
        Document doc = new Document("nombre", cliente.getNombre())
                .append("direccion", cliente.getDireccion())
                .append("telefono", cliente.getTelefono())
                .append("email", cliente.getEmail())
                .append("fechaDeRegistro", cliente.getFechaDeRegistro());

        collection.insertOne(doc);

        String idGenerado = doc.getObjectId("_id").toHexString();
        cliente.setId(idGenerado); // ✅ Asignamos el ID al objeto producto

        return idGenerado;
    }

    @Override
    public void actualizar(String id, Cliente cliente) {
        collection.updateOne(
                new Document("_id", new org.bson.types.ObjectId(id)), // Buscamos el documento por ID
                new Document("$set", new Document()
                        .append("nombre", cliente.getNombre())
                        .append("direccion", cliente.getDireccion())
                        .append("telefono", cliente.getTelefono())
                        .append("email", cliente.getEmail())
                        .append("fechaDeRegistro", cliente.getFechaDeRegistro())
                )
        );
    }

    @Override
    public void eliminar(String id) {
        collection.deleteOne(new Document("_id", new org.bson.types.ObjectId(id)));
    }

    @Override
    public Cliente buscar(String id) {
        ObjectId idProducto = new ObjectId(id);
        Document result = collection.find(Filters.eq("_id", idProducto)).first();

        if (result != null) {
            Cliente cliente = new Cliente(
                    result.getString("nombre"),
                    result.getString("direccion"),
                    result.getString("telefono"),
                    result.getString("email"),
                    result.getDate("fechaDeRegistro")
            );
            cliente.setId(result.getObjectId("_id").toHexString()); // Asignar el ID recuperado
            return cliente;
        }
        return null;
    }

    @Override
    public List<Cliente> obtenerTodos() {
        List<Cliente> clientes = new ArrayList<>();

        for (Document doc : collection.find()) {
            Cliente cliente = new Cliente(
                    doc.getString("nombre"),
                    doc.getString("direccion"),
                    doc.getString("telefono"),
                    doc.getString("email"),
                    doc.getDate("fechaDeRegistro")
            );
            cliente.setId(doc.getObjectId("_id").toHexString()); // ✅ Asignar el ID
            clientes.add(cliente);
        }
        return clientes;
    }
}

package org.example.easymanage.DAO;

import org.example.easymanage.Modelo.Cliente;
import org.example.easymanage.Modelo.Producto;

import java.util.List;

public interface ClienteDAO {
    String insertar(Cliente cliente);
    void actualizar(String id, Cliente cliente);
    void eliminar(String id);
    Cliente buscar(String id);
    List<Cliente> obtenerTodos();
}

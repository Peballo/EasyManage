package org.example.easymanage.DAO;

import org.example.easymanage.Modelo.Producto;
import java.util.List;

public interface ProductoDAO {
    String insertar(Producto producto);
    void actualizar(String id, Producto producto);
    void eliminar(String id);
    Producto buscar(String id);
    List<Producto> obtenerTodos();
}


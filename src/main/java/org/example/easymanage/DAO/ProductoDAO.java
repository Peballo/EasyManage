package org.example.easymanage.DAO;

import org.example.easymanage.Modelo.Producto;

public interface ProductoDAO {
    public void insertar(Producto producto);
    public void actualizar(String id, Producto producto);
    public void eliminar(String id);
    public Producto buscar(String id);
}

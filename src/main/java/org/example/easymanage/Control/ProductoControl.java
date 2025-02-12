package org.example.easymanage.Control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.beans.property.*;
import org.example.easymanage.Logica.ProductoLogica;
import org.example.easymanage.Modelo.Producto;

import java.util.Optional;

public class ProductoControl {
    @FXML private TableView<Producto> TablaProductos;
    @FXML private TableColumn<Producto, String> CodeColumn;
    @FXML private TableColumn<Producto, String> NameColumn;
    @FXML private TableColumn<Producto, String> DescriptionColumn;
    @FXML private TableColumn<Producto, Double> PriceColumn;
    @FXML private TableColumn<Producto, Integer> StockColumn;

    @FXML private TextField NameField;
    @FXML private TextField DescriptionField;
    @FXML private TextField PriceField;
    @FXML private TextField StockField;
    @FXML private TextField SearchField;

    @FXML private Button InsertButton;
    @FXML private Button DeleteButton;
    @FXML private Button UploadButton;
    @FXML private Button searchButton;

    private ProductoLogica productoLogica = new ProductoLogica();

    @FXML
    public void initialize() {
        // Configuración de columnas
        CodeColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        NameColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        DescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());
        PriceColumn.setCellValueFactory(cellData -> cellData.getValue().precioProperty().asObject());
        StockColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());

        // Cargar productos en la tabla
        cargarProductos();
    }

    private void cargarProductos() {
        try {
            TablaProductos.getItems().setAll(productoLogica.obtenerTodosLosProductos());
        } catch (Exception e) {
            mostrarAlerta("Error al cargar productos: " + e.getMessage());
        }
    }

    @FXML
    private void insertarProducto(ActionEvent event) {
        imprimirCampos();

        if (!validarCampos()) {
            return;
        }

        try {
            Producto producto = new Producto(
                    NameField.getText().trim(),
                    DescriptionField.getText().trim(),
                    Double.parseDouble(PriceField.getText().trim()),
                    Integer.parseInt(StockField.getText().trim())
            );

            // Insertar en MongoDB y obtener el ID generado
            String idGenerado = productoLogica.insertarProducto(producto);
            producto.setId(idGenerado);

            TablaProductos.getItems().add(producto);
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error al insertar producto: " + e.getMessage());
        }
    }

    @FXML
    public void eliminarProducto() {
        Producto productoSeleccionado = TablaProductos.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un producto para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de que desea eliminar este producto?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            productoLogica.eliminarProducto(productoSeleccionado.getId());
            TablaProductos.getItems().remove(productoSeleccionado);
            mostrarAlerta("Producto eliminado correctamente.");
        }
    }


    @FXML
    public void actualizarProducto() {
        Producto productoSeleccionado = TablaProductos.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un producto para actualizar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar actualización");
        confirmacion.setHeaderText("¿Está seguro de que desea actualizar este producto?");
        confirmacion.setContentText("Esta acción modificará la información del producto en la base de datos.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                String nombre = NameField.getText();
                String descripcion = DescriptionField.getText();
                Double precio = Double.parseDouble(PriceField.getText());
                Integer stock = Integer.parseInt(StockField.getText());

                productoSeleccionado.setNombre(nombre);
                productoSeleccionado.setDescripcion(descripcion);
                productoSeleccionado.setPrecio(precio);
                productoSeleccionado.setStock(stock);

                productoLogica.actualizarProducto(productoSeleccionado.getId(), productoSeleccionado);
                TablaProductos.refresh();
                mostrarAlerta("Producto actualizado correctamente.");
            } catch (NumberFormatException e) {
                mostrarAlerta("Error en los valores ingresados. Verifique los datos.");
            }
        }
    }



    @FXML
    private void buscarProducto(ActionEvent event) {
        String texto = SearchField.getText();  // Obtenemos el texto de búsqueda del campo de texto

        if (texto.isEmpty()) {
            // Si el texto está vacío, mostramos todos los productos en la tabla
            TablaProductos.setItems(FXCollections.observableArrayList(productoLogica.obtenerTodosLosProductos()));
        } else {
            // Si hay texto, filtramos los productos según el texto ingresado
            ObservableList<Producto> productosFiltrados = FXCollections.observableArrayList();
            for (Producto producto : productoLogica.obtenerTodosLosProductos()) {
                if (String.valueOf(producto.getId()).contains(texto) ||
                        producto.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                        producto.getDescripcion().toLowerCase().contains(texto.toLowerCase()) ||
                        String.valueOf(producto.getPrecio()).contains(texto) ||
                        String.valueOf(producto.getStock()).contains(texto)) {
                    productosFiltrados.add(producto);
                }
            }
            // Actualizamos la tabla con los productos filtrados
            TablaProductos.setItems(productosFiltrados);
        }
    }

    private boolean validarCampos() {
        imprimirCampos(); // Para depuración antes de validar

        if (NameField.getText() == null || NameField.getText().trim().isEmpty() ||
                DescriptionField.getText() == null || DescriptionField.getText().trim().isEmpty() ||
                PriceField.getText() == null || PriceField.getText().trim().isEmpty() ||
                StockField.getText() == null || StockField.getText().trim().isEmpty()) {
            mostrarAlerta("Todos los campos deben estar llenos.");
            return false;
        }

        try {
            Double.parseDouble(PriceField.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("El precio debe ser un número válido.");
            return false;
        }

        try {
            Integer.parseInt(StockField.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("El stock debe ser un número entero válido.");
            return false;
        }

        return true;
    }

    private void imprimirCampos() {
        System.out.println("---- Valores de los campos ----");
        System.out.println("Nombre: '" + NameField.getText() + "'");
        System.out.println("Descripción: '" + DescriptionField.getText() + "'");
        System.out.println("Precio: '" + PriceField.getText() + "'");
        System.out.println("Stock: '" + StockField.getText() + "'");
        System.out.println("------------------------------");
    }

    private void limpiarCampos() {
        NameField.clear();
        DescriptionField.clear();
        PriceField.clear();
        StockField.clear();
        SearchField.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

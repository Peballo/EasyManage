package org.example.easymanage.Control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.beans.property.*;
import org.example.easymanage.Logica.ProductoLogica;
import org.example.easymanage.Modelo.Producto;

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
        CodeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        NameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        DescriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
        PriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecio()).asObject());
        StockColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());

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
        imprimirCampos(); // <-- Esto nos dirá exactamente qué está recibiendo el sistema

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

            productoLogica.inserterProducto(producto);
            TablaProductos.getItems().add(producto);
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error al insertar producto: " + e.getMessage());
        }
    }


    @FXML
    private void eliminarProducto(ActionEvent event) {
        Producto seleccionado = TablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                productoLogica.eliminarProducto(seleccionado.getId());
                TablaProductos.getItems().remove(seleccionado);
            } catch (Exception e) {
                mostrarAlerta("Error al eliminar producto: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Seleccione un producto para eliminar.");
        }
    }

    @FXML
    private void actualizarProducto(ActionEvent event) {
        imprimirCampos(); // Para depuración
        if (!validarCampos()) return;

        Producto seleccionado = TablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                Producto producto = new Producto(
                        NameField.getText().trim(),
                        DescriptionField.getText().trim(),
                        Double.parseDouble(PriceField.getText().trim()),
                        Integer.parseInt(StockField.getText().trim())
                );
                productoLogica.actualizarProducto(seleccionado.getId(), producto);
                TablaProductos.refresh();
                limpiarCampos();
            } catch (Exception e) {
                mostrarAlerta("Error al actualizar producto: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Seleccione un producto para actualizar.");
        }
    }

    @FXML
    private void buscarProducto(ActionEvent event) {
        try {
            Producto producto = productoLogica.buscarProducto(SearchField.getText().trim());
            if (producto != null) {
                TablaProductos.getItems().setAll(producto);
            } else {
                mostrarAlerta("No se encontró el producto.");
            }
        } catch (Exception e) {
            mostrarAlerta("Error al buscar producto: " + e.getMessage());
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

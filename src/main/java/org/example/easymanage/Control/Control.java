package org.example.easymanage.Control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.example.easymanage.Logica.ClienteLogica;
import org.example.easymanage.Logica.ProductoLogica;
import org.example.easymanage.Modelo.Cliente;
import org.example.easymanage.Modelo.Producto;

import java.util.Date;
import java.util.Optional;

public class Control {


    @FXML private TextField NameClienteField;
    @FXML private TextField DireccionField;
    @FXML private TextField EmailField;
    @FXML private TextField TelefonoField;

    @FXML private TableView<Cliente> TablaCliente;
    @FXML private TableColumn<Cliente, String> CodeColumn1;
    @FXML private TableColumn<Cliente, String> NameColumn1;
    @FXML private TableColumn<Cliente, String> DescriptionColumn1;
    @FXML private TableColumn<Cliente, String> PriceColumn1;
    @FXML private TableColumn<Cliente, String> StockColumn1;
    @FXML private TableColumn<Cliente, Date> FechaDeIngresoColumn;

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
    @FXML private TextField SearchField1;

    @FXML private Button InsertButton;
    @FXML private Button DeleteButton;
    @FXML private Button UploadButton;
    @FXML private Button searchButton;

    private ProductoLogica productoLogica = new ProductoLogica();
    private ClienteLogica clienteLogica = new ClienteLogica();

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

        // Configuración de columnas para clientes
        CodeColumn1.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        NameColumn1.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        DescriptionColumn1.setCellValueFactory(cellData -> cellData.getValue().direccionProperty());
        PriceColumn1.setCellValueFactory(cellData -> cellData.getValue().telefonoProperty());
        StockColumn1.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        FechaDeIngresoColumn.setCellValueFactory(cellData -> cellData.getValue().fechaDeRegistroProperty() );
        // Cargar clientes en la tabla
        cargarClientes();
    }

    private void cargarProductos() {
        try {
            ObservableList<Producto> productos = FXCollections.observableArrayList(productoLogica.obtenerTodosLosProductos());
            TablaProductos.setItems(productos);
        } catch (Exception e) {
            mostrarAlerta("Error al cargar productos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cargarClientes() {
        try {
            ObservableList<Cliente> clientes = FXCollections.observableArrayList(clienteLogica.obtenerTodosLosClientes());
            TablaCliente.setItems(clientes);
        } catch (Exception e) {
            mostrarAlerta("Error al cargar clientes: " + e.getMessage(), Alert.AlertType.ERROR);
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
            limpiarCamposProducto();
        } catch (Exception e) {
            mostrarAlerta("Error al insertar producto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void eliminarProducto() {
        Producto productoSeleccionado = TablaProductos.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un producto para eliminar." , Alert.AlertType.ERROR);
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
            mostrarAlerta("Producto eliminado correctamente.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void actualizarProducto() {
        Producto productoSeleccionado = TablaProductos.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un producto para actualizar.", Alert.AlertType.ERROR);
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
                mostrarAlerta("Producto actualizado correctamente.", Alert.AlertType.ERROR);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error en los valores ingresados. Verifique los datos." + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }



    @FXML
    private void buscarProducto(ActionEvent event) {
        String texto = SearchField.getText().trim().toLowerCase();
        ObservableList<Producto> productos = FXCollections.observableArrayList(productoLogica.obtenerTodosLosProductos());

        if (!texto.isEmpty()) {
            productos = productos.filtered(producto ->
                    producto.getId().contains(texto) ||
                            producto.getNombre().toLowerCase().contains(texto) ||
                            producto.getDescripcion().toLowerCase().contains(texto) ||
                            String.valueOf(producto.getPrecio()).contains(texto) ||
                            String.valueOf(producto.getStock()).contains(texto)
            );
        }

        TablaProductos.setItems(productos);
    }


    private boolean validarCampos() {
        imprimirCampos(); // Para depuración antes de validar

        if (NameField.getText() == null || NameField.getText().trim().isEmpty() ||
                DescriptionField.getText() == null || DescriptionField.getText().trim().isEmpty() ||
                PriceField.getText() == null || PriceField.getText().trim().isEmpty() ||
                StockField.getText() == null || StockField.getText().trim().isEmpty()) {
            mostrarAlerta("Todos los campos deben estar llenos.", Alert.AlertType.ERROR);
            return false;
        }

        try {
            Double.parseDouble(PriceField.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("El precio debe ser un número válido.", Alert.AlertType.ERROR);
            return false;
        }

        try {
            Integer.parseInt(StockField.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("El stock debe ser un número entero válido." + e.getMessage(), Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    @FXML
    private void insertarCliente(ActionEvent event) {
        imprimirCamposCliente();

        if (!validarCamposCliente()) {
            return;
        }

        try {
            Cliente cliente = new Cliente(
                    NameClienteField.getText().trim(),
                    DireccionField.getText().trim(),
                    TelefonoField.getText().trim(),
                    EmailField.getText().trim(),
                    new Date() // Suponiendo que la fecha de registro es la fecha actual
            );

            // Insertar en MongoDB y obtener el ID generado
            String idGenerado = clienteLogica.insertarCliente(cliente);
            cliente.setId(idGenerado);

            TablaCliente.getItems().add(cliente);
            limpiarCamposCliente();
        } catch (Exception e) {
            mostrarAlerta("Error al insertar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void eliminarCliente(ActionEvent actionEvent) {
        Cliente clienteSeleccionado = TablaCliente.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un cliente para eliminar.", Alert.AlertType.ERROR);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de que desea eliminar este cliente?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            clienteLogica.eliminarCliente(clienteSeleccionado.getId());
            TablaCliente.getItems().remove(clienteSeleccionado);
            mostrarAlerta("Cliente eliminado correctamente.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void actualizarCliente(ActionEvent actionEvent) {
        Cliente clienteSeleccionado = TablaCliente.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un cliente para actualizar.", Alert.AlertType.ERROR);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar actualización");
        confirmacion.setHeaderText("¿Está seguro de que desea actualizar este cliente?");
        confirmacion.setContentText("Esta acción modificará la información del cliente en la base de datos.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                String nombre = NameClienteField.getText();
                String direccion = DireccionField.getText();
                String telefono = TelefonoField.getText();
                String email = EmailField.getText();

                clienteSeleccionado.setNombre(nombre);
                clienteSeleccionado.setDireccion(direccion);
                clienteSeleccionado.setTelefono(telefono);
                clienteSeleccionado.setEmail(email);

                clienteLogica.actualizarCliente(clienteSeleccionado.getId(), clienteSeleccionado);
                TablaCliente.refresh();
                mostrarAlerta("Cliente actualizado correctamente.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Error al actualizar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void buscarCliente(ActionEvent event) {
        String texto = SearchField.getText().trim().toLowerCase();
        ObservableList<Cliente> clientes = FXCollections.observableArrayList(clienteLogica.obtenerTodosLosProductos());
        if (!texto.isEmpty()) {
            clientes = clientes.filtered(cliente ->
                    cliente.getId().contains(texto) ||
                            cliente.getNombre().toLowerCase().contains(texto) ||
                            cliente.getDireccion().toLowerCase().contains(texto) ||
                            cliente.getTelefono().toLowerCase().contains(texto) ||
                            cliente.getEmail().toLowerCase().contains(texto) ||
                            String.valueOf(cliente.getFechaDeRegistro()).contains(texto)
            );
        }
        TablaCliente.setItems(clientes);
    }

    private boolean validarCamposCliente() {
        imprimirCamposCliente(); // Para depuración antes de validar

        if (NameClienteField.getText().trim().isEmpty() ||
                DireccionField.getText().trim().isEmpty() ||
                TelefonoField.getText().trim().isEmpty() ||
                EmailField.getText().trim().isEmpty()) {
            mostrarAlerta("Todos los campos deben estar llenos.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void imprimirCamposCliente() {
        System.out.println("---- Valores de los campos ----");
        System.out.println("Nombre: '" + NameClienteField.getText() + "'");
        System.out.println("Dirección: '" + DireccionField.getText() + "'");
        System.out.println("Teléfono: '" + TelefonoField.getText() + "'");
        System.out.println("Email: '" + EmailField.getText() + "'");
        System.out.println("------------------------------");
    }

    private void imprimirCampos() {
        System.out.println("---- Valores de los campos ----");
        System.out.println("Nombre: '" + NameField.getText() + "'");
        System.out.println("Descripción: '" + DescriptionField.getText() + "'");
        System.out.println("Precio: '" + PriceField.getText() + "'");
        System.out.println("Stock: '" + StockField.getText() + "'");
        System.out.println("------------------------------");
    }

    private void limpiarCamposCliente() {
        NameClienteField.clear();
        DireccionField.clear();
        TelefonoField.clear();
        EmailField.clear();
        SearchField1.clear();
    }

    private void limpiarCamposProducto() {
        NameField.clear();
        DescriptionField.clear();
        PriceField.clear();
        StockField.clear();
        SearchField.clear();
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo == Alert.AlertType.ERROR ? "Error" : "Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

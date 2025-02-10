module org.example.easymanage {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;

    // Abre el paquete que contiene el controlador para que JavaFX lo pueda acceder
    opens org.example.easymanage.Control to javafx.fxml;

    // Exporta los paquetes necesarios
    exports org.example.easymanage.Main;
    exports org.example.easymanage.Control;  // Exportar el paquete Control
}

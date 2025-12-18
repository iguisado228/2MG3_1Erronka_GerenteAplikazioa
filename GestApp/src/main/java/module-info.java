module com.example.gestapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    // Paquetes principales
    opens com.gestapp.main to javafx.fxml;
    exports com.gestapp.main;

    exports com.gestapp.controllers;
    opens com.gestapp.controllers to javafx.fxml;

    exports com.gestapp.hasheadorea;
    opens com.gestapp.hasheadorea to javafx.fxml;

    exports com.gestapp.konexioa;
    opens com.gestapp.konexioa to javafx.fxml;


    opens com.gestapp.modeloa to javafx.base;
    exports com.gestapp.modeloa;
}

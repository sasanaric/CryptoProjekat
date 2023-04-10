module com.example.cryptoprojekat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires spring.security.crypto;

    opens com.example.cryptoprojekat to javafx.fxml;
    exports com.example.cryptoprojekat;
}
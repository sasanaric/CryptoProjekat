package com.example.cryptoprojekat;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.control.Notifications;

public class ApplicationController {
    @FXML
    private Button loginButton;
    @FXML
    private Button registracijaButton;

    public void setLoginButton() throws Exception{
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();
        Application.openWindow("login.fxml");
    }
    public void setRegistracijaButton() throws Exception{
        Stage currentStage = (Stage) registracijaButton.getScene().getWindow();
        currentStage.close();
        Application.openWindow("register.fxml");
    }
}
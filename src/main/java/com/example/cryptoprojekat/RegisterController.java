package com.example.cryptoprojekat;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button potvrdiButton;
    @FXML
    private Label porukaLabel;

    public void setPotvrdiButton() throws Exception{
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if("".equals(username) || "".equals(password)){
            porukaLabel.setText("Potrebno je popuniti sva polja");
            return;
        }
        if(Crypto.usernameIsTaken(username)) {
            usernameTextField.setText("Username je zauzet");
        }else{
            Crypto.registerUser(username,password);
            Stage currentStage = (Stage) potvrdiButton.getScene().getWindow();
            currentStage.close();
            Application.openWindow("application.fxml");
        }
    }
}

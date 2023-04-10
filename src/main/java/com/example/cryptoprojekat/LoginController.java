package com.example.cryptoprojekat;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class LoginController{
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button potvrdiButton;
    @FXML
    private Label porukaLabel;
    @FXML
    private Button sertifikatButton;
    public static int brojac = 0;
    public static String currentUser="";
    public void setSertifikatButton() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("Priprema/certs"));
        fileChooser.setTitle("Select Certificate");
        File file = fileChooser.showOpenDialog(sertifikatButton.getScene().getWindow());
        if(file != null){
            System.out.println("SERTIFIKAT");
            String certificateName = file.getName();
            if(!Crypto.checkCertificate(file.getName())){
                porukaLabel.setText("Sertifikat nije validan");
            }else {
                porukaLabel.setText("Sertifikat je validan");
                usernameTextField.setDisable(false);
                passwordField.setDisable(false);
                potvrdiButton.setDisable(false);
                currentUser=certificateName.split("\\.")[0];
            }
        }
    }
    public void setPotvrdiButton() throws Exception{
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        if("".equals(username) || "".equals(password)){
            porukaLabel.setText("Potrebno je popuniti sva polja");
            return;
        }
        if(currentUser.equals(username)) {
            if(Crypto.CheckUserPassword(username,password)){
                if(brojac==3){
                    Crypto.removeCertificateHold(currentUser);
                }
                porukaLabel.setText("Korisnik je ulogovan");
                Stage currentStage = (Stage) potvrdiButton.getScene().getWindow();
                currentStage.close();
                Application.openWindow("secureFolder.fxml");
            }else {
                errorLogin();
            }
        }else{
            errorLogin();
        }
    }
    public void errorLogin() throws Exception{
        brojac++;
        if(brojac==4){
            Stage currentStage = (Stage) potvrdiButton.getScene().getWindow();
            currentStage.close();
            Application.openWindow("application.fxml");
        }
        System.out.println(brojac);
        porukaLabel.setText("Pogresni podaci za login");
        if(brojac==3){
            porukaLabel.setText("Sertifikat je suspendovan\nUnesite odgovarajuce podatke za reaktivaciju");
            Crypto.revokeCertificate(currentUser);
            Crypto.genCRL();
        }
    }
}
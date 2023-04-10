package com.example.cryptoprojekat;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import static com.example.cryptoprojekat.Crypto.ICON_CRYPTO;
import static com.example.cryptoprojekat.Crypto.SECURE_FOLDER_PATH;
import static com.example.cryptoprojekat.LoginController.currentUser;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SecureFolderController {
    @FXML
    private ListView<String> filesList;
    @FXML
    private Button uploadButton;
    @FXML
    private Button downloadButton;
    @FXML
    public void initialize() throws Exception
    {
        List<String> files = Crypto.getFiles(currentUser);
        filesList.getItems().addAll(files);
        filesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    public void setDownloadButton() throws Exception {
        String file = filesList.getSelectionModel().getSelectedItem();
        if(file!=null){
            System.out.println(file);
            if(!downloadFile(file)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("The file has been modified!");
                alert.showAndWait();}
        }
    }
    public void setUploadButton() throws Exception{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        File file = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        if(file != null){
            String fileName = file.getName();
            System.out.println(fileName);
            System.out.println(file.getAbsolutePath());
            Files.move(Path.of(file.getAbsolutePath()),Path.of(SECURE_FOLDER_PATH+fileName));
            filesList.getItems().add(fileName);
            uploadFile(fileName);
        }
    }
    public void uploadFile(String fileName) throws Exception{
        Crypto.signFile(currentUser,fileName);
        Crypto.encryptFile(fileName);
        List<String> segments = Crypto.divideFile(fileName);
        Crypto.putSegments(currentUser,fileName,segments);
    }
    public boolean downloadFile(String fileName) throws Exception{
        List<String> segments = Crypto.getSegments(currentUser,fileName);
        Crypto.assembleFile(fileName,segments);
        Crypto.decryptFile(fileName);
        Crypto.deleteFile(SECURE_FOLDER_PATH+fileName+".enc");
        return Crypto.verifyFile(currentUser,fileName);
    }
}

package com.example.cryptoprojekat;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.cryptoprojekat.Crypto.*;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("application.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 410, 320);
        Image iconCrypto = new Image(getClass().getResourceAsStream(ICON_CRYPTO));
        stage.getIcons().add(iconCrypto);
        stage.setTitle("Secure Folder");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
//        Crypto.signCertificateRequest("user1");
        launch();
//        genCRL();
//        getAlgKeySign();
//        for (int i =0;i<15;i++){
//            System.out.println(new Random().nextInt(7)+4);
//        }
//        System.out.println(Crypto.getFiles("user23"));
//        List<String> segmenti = new ArrayList<>();
//        segmenti = Crypto.divideFile("p3.txt",5);
//        System.out.println(segmenti);
//        Crypto.putSegment("user23","p3.txt",segmenti);
//        Crypto.assembleFile("sastavljen.txt",segmenti);
//        segmenti = Crypto.getSegment("user23","p3.txt");
//        System.out.println(segmenti);
//        System.out.println(Crypto.getFiles("batch"));
//        System.out.println(Crypto.getFilePaths("user23","p2.txt"));
//        System.out.println(Crypto.getFilePaths("batch","batchFile.txt"));
//        System.out.println(Crypto.getAlgAndKey());
//        System.out.println(Crypto.ALG);
//        System.out.println(Crypto.KEY);
//        System.out.println(Crypto.SIGN);
//        Crypto.signFile("batch","batchFile.txt");
//        Crypto.encryptFile("batchFile.txt");
//        List<String> segments = Crypto.divideFile("batchFile.txt");
//        Crypto.putSegments("batch","batchFile.txt",segments);
//        System.out.println(Crypto.getFiles("batch"));
//        List<String> segmentsGet = Crypto.getSegments("batch","batchFile.txt");
//        Crypto.assembleFile("batchFile.txt",segmentsGet);
//        Crypto.decryptFile("batchFile.txt");
//        System.out.println(Crypto.verifyFile("batch","batchFile.txt"));
//        Crypto.decryptFile(Crypto.MAPTXT);
//        Crypto.encryptFile(Crypto.MAPTXT);
//        System.exit(0);
    }
    public static void openWindow(String fxml) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), 410,320);
        Stage stage = new Stage();
        Image iconCrypto = new Image(Application.class.getResourceAsStream(ICON_CRYPTO));
        stage.getIcons().add(iconCrypto);
        stage.setTitle("Secure Folder");
        stage.setScene(scene);
        stage.show();
    }
}
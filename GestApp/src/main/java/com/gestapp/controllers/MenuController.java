package com.gestapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class MenuController {
@FXML
    public Button btnErreserbak;
    @FXML
    private BorderPane rootDashboard;

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnErabiltzaileak;

    @FXML
    private Button btnIrten;

    @FXML
    public void onbtnErabiltzaileak(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/gestapp/main/langilea-view.fxml")
            );
            Parent langileView = loader.load();


            contentArea.getChildren().setAll(langileView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void onbtnErreserbak(javafx.event.ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/gestapp/main/erreserba-view.fxml")
            );
            Parent erreserbaView = loader.load();
            contentArea.getChildren().setAll(erreserbaView);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onbtnProduktuak(javafx.event.ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/gestapp/main/produktua-view.fxml")
            );
            Parent produktuaView = loader.load();
            contentArea.getChildren().setAll(produktuaView);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onbtnMahaiak(javafx.event.ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/mahaia-view.fxml"));
            Parent mahaiaView = loader.load();
            contentArea.getChildren().setAll(mahaiaView);
        }catch (IOException e){e.printStackTrace();}
    }



    @FXML
    public void onbtnHornitzaileak(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/hornitzailea-view.fxml"));
            Parent hornitzaileaView = loader.load();
            contentArea.getChildren().setAll(hornitzaileaView);
        }catch (IOException e){e.printStackTrace();}
    }

    @FXML
    public void onbtnOsagaiak(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/osagaia-view.fxml"));
            Parent osagaiaView = loader.load();
            contentArea.getChildren().setAll(osagaiaView);
        }catch (IOException e){e.printStackTrace();}
    }

    @FXML
    public void onbtnMaterialak(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new  FXMLLoader(getClass().getResource("/com/gestapp/main/materiala-view.fxml"));
            Parent materialaView = loader.load();
            contentArea.getChildren().setAll(materialaView);
        }catch (IOException e){e.printStackTrace();}
    }



    @FXML
    public void onbtnErosketak(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/erosketa-view.fxml"));
            Parent erosketaView = loader.load();
            contentArea.getChildren().setAll(erosketaView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onbtnIrten(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) btnIrten.getScene().getWindow();
        stage.close();
    }
}
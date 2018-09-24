/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.kintlevoseg;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author szekus
 */
public class FxHtmlEditorExample extends Application {

    private StringBuilder htmlText;
    KintlevosegLevel kintlevosegLevel;

    public void run() {

        launch("");
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new Group());

        VBox root = new VBox();
        root.setPadding(new Insets(8, 8, 8, 8));
        root.setSpacing(5);
        root.setAlignment(Pos.BOTTOM_LEFT);

        // Create the HTMLEditor
        HTMLEditor htmlEditor = new HTMLEditor();
        // Set the Height of the HTMLEditor
        htmlEditor.setPrefHeight(300);
        // Set the Width of the HTMLEditor
        htmlEditor.setPrefWidth(600);

        kintlevosegLevel = KintlevosegLevel.create(KintlevosegLevel.Type.PDF);

        htmlEditor.setHtmlText(kintlevosegLevel.getHtmlStringFromDb().toString());

        Button saveButton = new Button("Mentés");
        root.setAlignment(Pos.BASELINE_LEFT);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                System.out.println(htmlEditor.getHtmlText());
            }
        });

        

        root.getChildren().addAll(htmlEditor, saveButton);
        scene.setRoot(root);

        stage.setScene(scene);
        stage.show();
    }

}

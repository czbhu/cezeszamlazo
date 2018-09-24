/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.kintlevoseg;

import cezeszamlazo.kintlevoseg.KintlevosegLevel.Type;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSException;

public class KintlevosegLevelHtmlEditor {

    private static HtmlTableCreateDialog htmlTableCreateDialog;
    private static JFrame frame;
    private StringBuilder htmlText;
    private static KintlevosegLevel kintlevosegLevel;
    private static KintlevosegLevelAttributum kintlevosegLevelAttributum;
    public static Type type;

    private KintlevosegLevelHtmlEditor(Type type) {
        this.type = type;
    }

    public static KintlevosegLevelHtmlEditor create(Type type) {
        return new KintlevosegLevelHtmlEditor(type);
    }

    private static void initAndShowGUI() {
        frame = new JFrame("Felszólító levél");
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setSize(800, 600);
        frame.setVisible(true);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
    }

    private static void initFX(JFXPanel fxPanel) {
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }

    private static Scene createScene() {
        Scene scene = new Scene(new Group());

        VBox root = new VBox();
        root.setPadding(new Insets(8, 8, 8, 8));
        root.setSpacing(5);
        root.setAlignment(Pos.CENTER);

        HTMLEditor htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(600);
        htmlEditor.setPrefWidth(800);

        Node webNode = htmlEditor.lookup(".web-view");
        WebView webView = (WebView) webNode;
        WebEngine engine = webView.getEngine();

        kintlevosegLevel = KintlevosegLevel.create(KintlevosegLevelHtmlEditor.type);
        kintlevosegLevelAttributum = new KintlevosegLevelAttributum();
        TextField subjectTextField = new TextField(kintlevosegLevel.getSubjectFromDb());

        htmlEditor.setHtmlText(kintlevosegLevel.getHtmlStringFromDb().toString());

        Button saveAndCloseButton = new Button("Mentés és bezárás");
//        root.setAlignment(Pos.BASELINE_LEFT);
        saveAndCloseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {

                kintlevosegLevel.setHtmlString(new StringBuilder(htmlEditor.getHtmlText()));
                kintlevosegLevel.setSubject(subjectTextField.getText());
                kintlevosegLevel.save();
                frame.setVisible(false);
            }
        });

        Button saveButton = new Button("Mentés");
//        root.setAlignment(Pos.BASELINE_LEFT);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                kintlevosegLevel.setHtmlString(new StringBuilder(htmlEditor.getHtmlText()));
                kintlevosegLevel.setSubject(subjectTextField.getText());
                kintlevosegLevel.save();
            }
        });

        ToolBar toolbar = new ToolBar();
        final ComboBox combobox = new ComboBox();
        Label label = new Label("Tárgy: ");
        Button button = new Button("table");

        combobox.getItems().addAll(
                kintlevosegLevelAttributum.getAllName()
        );
        toolbar.getItems().add(combobox);
        toolbar.getItems().add(saveAndCloseButton);
        toolbar.getItems().add(saveButton);
        toolbar.getItems().add(label);
        toolbar.getItems().add(subjectTextField);
        toolbar.getItems().add(button);

        combobox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {

            }
        });

        String jsCodeInsertHtml = "function insertHtmlAtCursor(html) {"
                + "    var range, node;"
                + "    if (window.getSelection && window.getSelection().getRangeAt) {"
                + "        range = window.getSelection().getRangeAt(0);"
                + "        node = range.createContextualFragment(html);"
                + "        range.insertNode(node);"
                + "    } else if (document.selection && document.selection.createRange) {"
                + "        document.selection.createRange().pasteHTML(html);"
                + "    }"
                + "}insertHtmlAtCursor('####html####')";

        combobox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                try {

//                    System.out.println(t1);
                    engine.executeScript(jsCodeInsertHtml.
                            replace("####html####",
                                    escapeJavaStyleString(t1, true, true)));
                } catch (JSException e) {
                    // A JavaScript Exception Occured
                }
            }
        });

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ;
                htmlTableCreateDialog = new HtmlTableCreateDialog();
                htmlTableCreateDialog.setVisible(true);
                System.out.println("OPEN");
                if (htmlTableCreateDialog.getReturnStatus() == HtmlTableCreateDialog.RET_OK) {

                    String t1 = htmlTableCreateDialog.getHtmlTableString();
                    try {

//                    System.out.println(t1);
                        engine.executeScript(jsCodeInsertHtml.
                                replace("####html####",
                                        escapeJavaStyleString(t1, true, true)));
                    } catch (JSException e) {
                        // A JavaScript Exception Occured
                    }
                }

                System.out.println("Closed");
            }
        });
        root.getChildren().addAll(htmlEditor, saveButton, toolbar);
        scene.setRoot(root);

        return (scene);
    }

    public static void run() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initAndShowGUI();
            }
        });
    }

    private static String escapeJavaStyleString(String str,
            boolean escapeSingleQuote, boolean escapeForwardSlash) {
        StringBuilder out = new StringBuilder("");
        if (str == null) {
            return null;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff) {
                out.append("\\u").append(hex(ch));
            } else if (ch > 0xff) {
                out.append("\\u0").append(hex(ch));
            } else if (ch > 0x7f) {
                out.append("\\u00").append(hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                    case '\b':
                        out.append('\\');
                        out.append('b');
                        break;
                    case '\n':
                        out.append('\\');
                        out.append('n');
                        break;
                    case '\t':
                        out.append('\\');
                        out.append('t');
                        break;
                    case '\f':
                        out.append('\\');
                        out.append('f');
                        break;
                    case '\r':
                        out.append('\\');
                        out.append('r');
                        break;
                    default:
                        if (ch > 0xf) {
                            out.append("\\u00").append(hex(ch));
                        } else {
                            out.append("\\u000").append(hex(ch));
                        }
                        break;
                }
            } else {
                switch (ch) {
                    case '\'':
                        if (escapeSingleQuote) {
                            out.append('\\');
                        }
                        out.append('\'');
                        break;
                    case '"':
                        out.append('\\');
                        out.append('"');
                        break;
                    case '\\':
                        out.append('\\');
                        out.append('\\');
                        break;
                    case '/':
                        if (escapeForwardSlash) {
                            out.append('\\');
                        }
                        out.append('/');
                        break;
                    default:
                        out.append(ch);
                        break;
                }
            }
        }
        return out.toString();
    }

    private static String hex(int i) {
        return Integer.toHexString(i);
    }
}

package dev.abykov.pets.relmgr.ui.tabs.letter;

import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class LetterTab {

    private final LetterViewModel vm;

    public LetterTab(LetterViewModel vm) {
        this.vm = vm;
    }

    public Parent getContent() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField versionField = createTextField("...", vm.versionProperty());
        TextField patchUrlField = createTextField("ftp://...", vm.patchUrlProperty());
        TextField sqlUrlField = createTextField("ftp://...", vm.sqlUrlProperty());

        Button generateBtn = new Button("Сформировать");
        generateBtn.disableProperty().bind(vm.loadingProperty());

        ProgressIndicator loader = new ProgressIndicator();
        loader.setPrefSize(24, 24);
        loader.visibleProperty().bind(vm.loadingProperty());

        HBox actions = new HBox(10, generateBtn, loader);

        WebView webView = new WebView();
        webView.setPrefHeight(450);
        VBox.setVgrow(webView, Priority.ALWAYS);

        vm.letterHtmlProperty().addListener((obs, o, html) ->
                webView.getEngine().loadContent(html, "text/html")
        );

        generateBtn.setOnAction(e -> vm.generateLetter());

        Button sendBtn = new Button("Отправить письмо");
        sendBtn.disableProperty().bind(
                vm.loadingProperty()
                        .or(vm.sendingProperty())
                        .or(vm.letterHtmlProperty().isEmpty())
        );

        ProgressIndicator sendLoader = new ProgressIndicator();
        sendLoader.setPrefSize(20, 20);
        sendLoader.visibleProperty().bind(vm.sendingProperty());

        HBox sendActions = new HBox(10, sendBtn, sendLoader);

        sendBtn.setOnAction(e -> vm.sendLetter());

        Label sendStatus = new Label();
        sendStatus.textProperty().bind(vm.sendStatusProperty());

        root.getChildren().addAll(
                label("Версия:"), versionField,
                label("URL патча:"), patchUrlField,
                label("URL SQL:"), sqlUrlField,
                actions,
                label("Письмо:"),
                webView,
                sendActions,
                sendStatus
        );

        return root;
    }

    private TextField createTextField(String prompt, StringProperty prop) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.textProperty().bindBidirectional(prop);
        return tf;
    }

    private Label label(String text) {
        return new Label(text);
    }
}

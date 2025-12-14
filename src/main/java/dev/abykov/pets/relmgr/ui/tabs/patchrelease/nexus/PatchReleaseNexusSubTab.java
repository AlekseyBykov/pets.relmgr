package dev.abykov.pets.relmgr.ui.tabs.patchrelease.nexus;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;

public class PatchReleaseNexusSubTab {

    private final PatchReleaseNexusViewModel vm;

    public PatchReleaseNexusSubTab(PatchReleaseNexusViewModel vm) {
        this.vm = vm;
    }

    public Parent getContent() {
        VBox root = createRoot();
        GridPane form = createForm();
        Label resultLabel = new Label("Лог загрузки:");
        TextArea output = createOutputArea();

        root.getChildren().addAll(
                form,
                resultLabel,
                output
        );

        return root;
    }

    private VBox createRoot() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        return root;
    }

    private GridPane createForm() {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        TextField versionField = createVersionField();

        Button chooseZipBtn = createChooseZipButton();
        Label zipLabel = new Label();
        zipLabel.textProperty().bind(vm.zipFileProperty().asString());

        Button uploadBtn = createUploadButton();

        form.add(new Label("Версия патча:"), 0, 0);
        form.add(versionField, 1, 0);

        form.add(new Label("Архив:"), 0, 1);
        form.add(new VBox(5, chooseZipBtn, zipLabel), 1, 1);

        form.add(uploadBtn, 1, 2);

        return form;
    }

    private TextField createVersionField() {
        TextField tf = new TextField();
        tf.setPromptText("..._fix7");
        tf.textProperty().bindBidirectional(vm.patchVersionProperty());
        return tf;
    }

    private Button createChooseZipButton() {
        Button btn = new Button("Выбрать архив");
        btn.setOnAction(e -> {
            FileChooser ch = new FileChooser();
            ch.setTitle("Выбрать архив патча");
            ch.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("ZIP", "*.zip")
            );

            File file = ch.showOpenDialog(null);
            if (file != null) {
                vm.zipFileProperty().set(file);
            }
        });
        return btn;
    }

    private Button createUploadButton() {
        Button btn = new Button("Загрузить в Nexus");
        btn.setOnAction(e -> vm.upload());
        return btn;
    }

    private TextArea createOutputArea() {
        TextArea area = new TextArea();
        area.setEditable(false);
        area.setWrapText(true);
        area.textProperty().bind(vm.outputLogProperty());
        VBox.setVgrow(area, Priority.ALWAYS);
        return area;
    }
}

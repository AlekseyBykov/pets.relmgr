package dev.abykov.pets.relmgr.ui.tabs.patchrelease.message;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class PatchReleaseMessageSubTab {

    private final PatchReleaseMessageViewModel vm;

    public PatchReleaseMessageSubTab(PatchReleaseMessageViewModel vm) {
        this.vm = vm;
    }

    public Parent getContent() {
        VBox root = createRoot();
        GridPane form = createForm(vm);
        Label resultLabel = new Label("Итоговое сообщение:");
        TextArea outputArea = createOutputArea(vm);

        root.getChildren().addAll(
                form,
                resultLabel,
                outputArea
        );
        return root;
    }

    private VBox createRoot() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        return root;
    }

    private GridPane createForm(PatchReleaseMessageViewModel vm) {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        TextField versionField = createVersionField(vm);
        form.add(new Label("Версия патча:"), 0, 0);
        form.add(versionField, 1, 0);

        TextField patchUrlField = createPatchUrlField(vm);
        form.add(new Label("URL патча:"), 0, 1);
        form.add(patchUrlField, 1, 1);

        ComboBox<String> installTargetsCombo = createTargetsCombo(vm);
        form.add(new Label("Установить на:"), 0, 2);
        form.add(installTargetsCombo, 1, 2);

        TextArea issuesArea = createIssuesArea(vm);
        form.add(new Label("Задачи:"), 0, 3);
        form.add(issuesArea, 1, 3);

        HBox actions = createActions(vm, form);
        form.add(actions, 1, 4);

        return form;
    }

    private TextField createVersionField(PatchReleaseMessageViewModel vm) {
        TextField tf = new TextField();
        tf.setPromptText("..._fix7");
        tf.textProperty().bindBidirectional(vm.fullVersionProperty());
        return tf;
    }

    private TextField createPatchUrlField(PatchReleaseMessageViewModel vm) {
        TextField tf = new TextField();
        tf.setPromptText("https://jira-.../patches/");
        tf.textProperty().bindBidirectional(vm.patchUrlProperty());
        return tf;
    }

    private ComboBox<String> createTargetsCombo(PatchReleaseMessageViewModel vm) {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll(
                "Систему A и B",
                "Систему A",
                "Систему B"
        );
        combo.valueProperty().bindBidirectional(vm.installTargetsProperty());
        combo.setValue("Систему A и B");
        return combo;
    }

    private TextArea createIssuesArea(PatchReleaseMessageViewModel vm) {
        TextArea ta = new TextArea();
        ta.setPromptText("PROJ-1234");
        ta.setPrefRowCount(4);
        ta.textProperty().bindBidirectional(vm.issuesTextProperty());
        return ta;
    }

    private HBox createActions(PatchReleaseMessageViewModel vm, Parent root) {
        Button generateBtn = new Button("Сформировать");
        generateBtn.setOnAction(e -> vm.generate());

        Button saveBtn = new Button("Сохранить в TXT");
        saveBtn.setOnAction(e -> saveToFile(vm, root));

        return new HBox(10, generateBtn, saveBtn);
    }

    private TextArea createOutputArea(PatchReleaseMessageViewModel vm) {
        TextArea area = new TextArea();
        area.setEditable(false);
        area.setWrapText(true);
        area.textProperty().bind(vm.resultMessageProperty());
        VBox.setVgrow(area, Priority.ALWAYS);
        return area;
    }

    private void saveToFile(PatchReleaseMessageViewModel vm, Parent root) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Сохранить сообщение как TXT");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text files", "*.txt")
        );

        chooser.setInitialFileName("patch_" + vm.fullVersionProperty().get() + ".txt");

        File file = chooser.showSaveDialog(root.getScene().getWindow());
        if (file == null) {
            return;
        }

        try {
            Files.writeString(
                    file.toPath(),
                    vm.resultMessageProperty().get(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException ex) {
            new Alert(
                    Alert.AlertType.ERROR,
                    "Ошибка сохранения файла: " + ex.getMessage()
            ).showAndWait();
        }
    }
}

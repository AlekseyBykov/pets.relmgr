package dev.abykov.pets.relmgr.ui.tabs.patchbytask.zip;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class PatchByTaskZipSubTab {

    private final PatchByTaskZipViewModel vm;

    public PatchByTaskZipSubTab(PatchByTaskZipViewModel vm) {
        this.vm = vm;
    }

    public Parent getContent() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        TextField appVerField = new TextField();
        appVerField.setPromptText("123...");
        appVerField.textProperty().bindBidirectional(vm.appVersionProperty());

        TextArea issuesArea = new TextArea();
        issuesArea.setPromptText("PROJ-123");
        issuesArea.textProperty().bindBidirectional(vm.issuesTextProperty());

        TextField dateField = new TextField();
        dateField.setPromptText("14.12.2025 16:32:44");
        dateField.textProperty().bindBidirectional(vm.buildDateTimeProperty());

        TextField coreField = new TextField();
        coreField.textProperty().bindBidirectional(vm.coreVersionProperty());

        Button chooseZip = new Button("Выбрать прототип ZIP");
        chooseZip.setOnAction(e -> {
            FileChooser ch = new FileChooser();
            ch.setTitle("Выбрать архив");
            ch.getExtensionFilters().add(new FileChooser.ExtensionFilter("ZIP", "*.zip"));

            File file = ch.showOpenDialog(root.getScene().getWindow());
            if (file != null) {
                vm.prototypeZipProperty().set(file);
            }
        });

        Label zipLabel = new Label();
        zipLabel.textProperty().bind(vm.prototypeZipProperty().asString());

        Button generateBtn = new Button("Сформировать ZIP");
        generateBtn.setOnAction(e -> vm.generate());

        form.add(new Label("Версия:"), 0, 0);
        form.add(appVerField, 1, 0);

        form.add(new Label("Задачи:"), 0, 1);
        form.add(issuesArea, 1, 1);

        form.add(new Label("Дата/время:"), 0, 2);
        form.add(dateField, 1, 2);

        form.add(new Label("Версия основная:"), 0, 3);
        form.add(coreField, 1, 3);

        form.add(new Label("Прототип ZIP:"), 0, 4);
        form.add(new VBox(5, chooseZip, zipLabel), 1, 4);

        form.add(generateBtn, 1, 5);

        TextArea reportArea = new TextArea();
        reportArea.setEditable(false);
        VBox.setVgrow(reportArea, Priority.ALWAYS);
        reportArea.textProperty().bind(vm.outputMessageProperty());

        root.getChildren().addAll(
                form,
                new Label("Отчет:"),
                reportArea
        );
        return root;
    }
}

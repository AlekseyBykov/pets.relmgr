package dev.abykov.pets.relmgr.ui.tabs.patchrelease.zip;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;

public class PatchReleaseZipSubTab {

    private final PatchReleaseZipViewModel vm;

    public PatchReleaseZipSubTab(PatchReleaseZipViewModel vm) {
        this.vm = vm;
    }

    public Parent getContent() {
        VBox root = createRoot();
        GridPane form = createForm(vm);
        Label resultLabel = new Label("Результат:");
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

    private GridPane createForm(PatchReleaseZipViewModel vm) {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        TextField appVersionField = new TextField();
        appVersionField.setPromptText("...");
        appVersionField.textProperty().bindBidirectional(vm.appVersionProperty());

        TextField fullVersionField = new TextField();
        fullVersionField.setPromptText("..._fix7");
        fullVersionField.textProperty().bindBidirectional(vm.fullVersionProperty());

        TextField dateField = new TextField();
        dateField.setPromptText("14.12.2025 13:25:00");
        dateField.textProperty().bindBidirectional(vm.buildDateTimeProperty());

        TextField coreField = new TextField();
        coreField.textProperty().bindBidirectional(vm.coreVersionProperty());

        Button chooseZipBtn = createChooseZipButton(vm);
        Label zipLabel = new Label();
        zipLabel.textProperty().bind(vm.sourceZipProperty().asString());

        Button buildBtn = createBuildButton(vm);

        form.add(new Label("Версия:"), 0, 0);
        form.add(appVersionField, 1, 0);

        form.add(new Label("Итоговая версия:"), 0, 1);
        form.add(fullVersionField, 1, 1);

        form.add(new Label("Дата/время:"), 0, 2);
        form.add(dateField, 1, 2);

        form.add(new Label("Версия основная:"), 0, 3);
        form.add(coreField, 1, 3);

        form.add(new Label("Прототип ZIP:"), 0, 4);
        form.add(new VBox(5, chooseZipBtn, zipLabel), 1, 4);

        form.add(buildBtn, 1, 5);

        return form;
    }

    private Button createChooseZipButton(PatchReleaseZipViewModel vm) {
        Button btn = new Button("Выбрать прототип ZIP");
        btn.setOnAction(e -> {
            FileChooser ch = new FileChooser();
            ch.setTitle("Выбрать архив");
            ch.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("ZIP", "*.zip")
            );

            File file = ch.showOpenDialog(null);
            if (file != null) {
                vm.sourceZipProperty().set(file);
            }
        });
        return btn;
    }

    private Button createBuildButton(PatchReleaseZipViewModel vm) {
        Button btn = new Button("Сформировать ZIP");
        btn.setOnAction(e -> vm.patchZip());
        return btn;
    }

    private TextArea createOutputArea(PatchReleaseZipViewModel vm) {
        TextArea area = new TextArea();
        area.setEditable(false);
        area.setWrapText(true);
        area.textProperty().bind(vm.outputLogProperty());
        VBox.setVgrow(area, Priority.ALWAYS);
        return area;
    }
}

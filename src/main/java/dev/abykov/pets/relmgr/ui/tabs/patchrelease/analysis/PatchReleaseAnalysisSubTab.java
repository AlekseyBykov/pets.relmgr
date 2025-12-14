package dev.abykov.pets.relmgr.ui.tabs.patchrelease.analysis;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PatchReleaseAnalysisSubTab {

    private final PatchReleaseAnalysisViewModel vm;

    public PatchReleaseAnalysisSubTab(PatchReleaseAnalysisViewModel vm) {
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

    private GridPane createForm(PatchReleaseAnalysisViewModel vm) {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        TextField baseVersionField = createBaseVersionField(vm);
        HBox actions = createActions(vm);

        form.add(new Label("Базовая версия:"), 0, 0);
        form.add(baseVersionField, 1, 0);

        form.add(actions, 1, 1);

        return form;
    }

    private TextField createBaseVersionField(PatchReleaseAnalysisViewModel vm) {
        TextField tf = new TextField();
        tf.setPromptText("...");
        tf.textProperty().bindBidirectional(vm.baseVersionProperty());
        return tf;
    }

    private HBox createActions(PatchReleaseAnalysisViewModel vm) {
        Button detectBtn = new Button("Определить версию");
        detectBtn.setOnAction(e -> vm.analyze());

        return new HBox(10, detectBtn);
    }

    private TextArea createOutputArea(PatchReleaseAnalysisViewModel vm) {
        TextArea area = new TextArea();
        area.setEditable(false);
        area.setWrapText(true);
        area.textProperty().bind(vm.resultTextProperty());
        VBox.setVgrow(area, Priority.ALWAYS);
        return area;
    }
}

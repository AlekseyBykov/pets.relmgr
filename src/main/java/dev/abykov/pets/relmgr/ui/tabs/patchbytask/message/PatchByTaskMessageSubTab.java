package dev.abykov.pets.relmgr.ui.tabs.patchbytask.message;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PatchByTaskMessageSubTab {

    private final PatchByTaskMessageViewModel vm;

    public PatchByTaskMessageSubTab(PatchByTaskMessageViewModel vm) {
        this.vm = vm;
    }

    public Parent getContent() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(10));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        TextField versionField = new TextField();
        versionField.setPromptText("123_fix7");
        versionField.textProperty().bindBidirectional(vm.patchVersionProperty());

        TextArea issuesArea = new TextArea();
        issuesArea.setPromptText("PROJ-123456");
        issuesArea.setPrefRowCount(6);
        issuesArea.textProperty().bindBidirectional(vm.issuesTextProperty());

        Button buildBtn = new Button("Сформировать сообщение");
        buildBtn.setOnAction(e -> vm.generateMessage());

        TextArea messageOut = new TextArea();
        messageOut.setEditable(false);
        messageOut.setWrapText(true);
        messageOut.setPrefRowCount(10);
        VBox.setVgrow(messageOut, Priority.ALWAYS);
        messageOut.textProperty().bind(vm.messageProperty());

        form.add(new Label("Версия патча:"), 0, 0);
        form.add(versionField, 1, 0);

        form.add(new Label("Задачи:"), 0, 1);
        form.add(issuesArea, 1, 1);

        form.add(buildBtn, 1, 2);

        root.getChildren().addAll(
                form,
                new Label("Сообщение:"),
                messageOut
        );

        return root;
    }
}

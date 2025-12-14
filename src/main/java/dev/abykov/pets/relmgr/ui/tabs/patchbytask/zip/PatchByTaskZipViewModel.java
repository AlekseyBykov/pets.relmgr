package dev.abykov.pets.relmgr.ui.tabs.patchbytask.zip;

import dev.abykov.pets.relmgr.core.service.PatchByTaskService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PatchByTaskZipViewModel {

    private final StringProperty appVersion = new SimpleStringProperty("");
    private final StringProperty issuesText = new SimpleStringProperty("");
    private final StringProperty buildDateTime = new SimpleStringProperty("");
    private final StringProperty coreVersion = new SimpleStringProperty("");

    private final ObjectProperty<File> prototypeZip = new SimpleObjectProperty<>();

    private final StringProperty outputMessage = new SimpleStringProperty("");
    private final StringProperty testerMessage = new SimpleStringProperty("");

    private final PatchByTaskService service;

    public PatchByTaskZipViewModel(PatchByTaskService service) {
        this.service = service;
    }

    public void generate() {
        List<String> issues = Arrays.stream(issuesText.get().split("\\s+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        PatchByTaskService.Result res = service.patch(
                appVersion.get(),
                issues,
                buildDateTime.get(),
                coreVersion.get(),
                prototypeZip.get()
        );

        outputMessage.set(res.report());
        testerMessage.set(res.testerMessage());
    }

    public StringProperty appVersionProperty() {
        return appVersion;
    }

    public StringProperty issuesTextProperty() {
        return issuesText;
    }

    public StringProperty buildDateTimeProperty() {
        return buildDateTime;
    }

    public StringProperty coreVersionProperty() {
        return coreVersion;
    }

    public ObjectProperty<File> prototypeZipProperty() {
        return prototypeZip;
    }

    public StringProperty outputMessageProperty() {
        return outputMessage;
    }

    public StringProperty testerMessageProperty() {
        return testerMessage;
    }
}

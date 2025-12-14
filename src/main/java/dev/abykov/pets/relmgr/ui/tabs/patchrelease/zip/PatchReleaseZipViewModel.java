package dev.abykov.pets.relmgr.ui.tabs.patchrelease.zip;

import dev.abykov.pets.relmgr.core.service.PatchReleaseZipService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

public class PatchReleaseZipViewModel {

    private final StringProperty appVersion = new SimpleStringProperty("");
    private final StringProperty fullVersion = new SimpleStringProperty("");
    private final StringProperty buildDateTime = new SimpleStringProperty("");
    private final StringProperty coreVersion = new SimpleStringProperty("...");

    private final ObjectProperty<File> sourceZip = new SimpleObjectProperty<>();
    private final ObjectProperty<File> resultZip = new SimpleObjectProperty<>();
    private final StringProperty outputLog = new SimpleStringProperty("");

    private final PatchReleaseZipService patchReleaseZipService;

    public PatchReleaseZipViewModel(PatchReleaseZipService patchReleaseZipService) {
        this.patchReleaseZipService = patchReleaseZipService;
    }

    public void patchZip() {
        if (sourceZip.get() == null) {
            outputLog.set("Не выбран архив-прототип.");
            return;
        }

        if (fullVersion.get().isBlank()) {
            outputLog.set("Не указана итоговая версия.");
            return;
        }

        PatchReleaseZipService.Result res = patchReleaseZipService.patchZip(
                appVersion.get(),
                fullVersion.get(),
                buildDateTime.get(),
                coreVersion.get(),
                sourceZip.get()
        );

        outputLog.set(res.report());
    }

    public StringProperty appVersionProperty() {
        return appVersion;
    }

    public StringProperty fullVersionProperty() {
        return fullVersion;
    }

    public StringProperty buildDateTimeProperty() {
        return buildDateTime;
    }

    public StringProperty coreVersionProperty() {
        return coreVersion;
    }

    public ObjectProperty<File> sourceZipProperty() {
        return sourceZip;
    }

    public ObjectProperty<File> resultZipProperty() {
        return resultZip;
    }

    public StringProperty outputLogProperty() {
        return outputLog;
    }
}

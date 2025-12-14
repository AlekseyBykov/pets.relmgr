package dev.abykov.pets.relmgr.ui.tabs.patchrelease.nexus;

import dev.abykov.pets.relmgr.core.service.NexusUploadService;
import javafx.beans.property.*;

import java.io.File;

public class PatchReleaseNexusViewModel {

    private final StringProperty patchVersion = new SimpleStringProperty("");
    private final ObjectProperty<File> zipFile = new SimpleObjectProperty<>();
    private final StringProperty outputLog = new SimpleStringProperty("");

    private final NexusUploadService service;

    public PatchReleaseNexusViewModel(NexusUploadService service) {
        this.service = service;
    }

    public void upload() {
        if (zipFile.get() == null) {
            outputLog.set("Архив не выбран.");
            return;
        }

        if (patchVersion.get().isBlank()) {
            outputLog.set("Не указана версия патча.");
            return;
        }

        String result = service.upload(zipFile.get(), patchVersion.get());
        outputLog.set(result);
    }

    public StringProperty patchVersionProperty() {
        return patchVersion;
    }

    public ObjectProperty<File> zipFileProperty() {
        return zipFile;
    }

    public StringProperty outputLogProperty() {
        return outputLog;
    }
}

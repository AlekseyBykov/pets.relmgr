package dev.abykov.pets.relmgr.ui.tabs.patchrelease.message;

import dev.abykov.pets.relmgr.core.service.PatchMessageService;
import javafx.beans.property.*;

import java.util.Arrays;
import java.util.List;

public class PatchReleaseMessageViewModel {

    private final StringProperty fullVersion = new SimpleStringProperty("");
    private final StringProperty patchUrl = new SimpleStringProperty("");
    private final StringProperty installTargets = new SimpleStringProperty("Систему A и B");
    private final StringProperty issuesText = new SimpleStringProperty("");
    private final StringProperty resultMessage = new SimpleStringProperty("");

    private final PatchMessageService service;

    public PatchReleaseMessageViewModel(PatchMessageService service) {
        this.service = service;
    }

    public void generate() {
        List<String> issues = Arrays.stream(issuesText.get().split("\\R"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        String msg = service.generateMessage(
                fullVersion.get(),
                patchUrl.get(),
                installTargets.get(),
                issues
        );

        resultMessage.set(msg);
    }

    public StringProperty fullVersionProperty() {
        return fullVersion;
    }

    public StringProperty patchUrlProperty() {
        return patchUrl;
    }

    public StringProperty installTargetsProperty() {
        return installTargets;
    }

    public StringProperty issuesTextProperty() {
        return issuesText;
    }

    public StringProperty resultMessageProperty() {
        return resultMessage;
    }
}

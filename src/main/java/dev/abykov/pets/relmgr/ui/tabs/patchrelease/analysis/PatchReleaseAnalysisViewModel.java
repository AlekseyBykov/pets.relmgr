package dev.abykov.pets.relmgr.ui.tabs.patchrelease.analysis;

import dev.abykov.pets.relmgr.core.domain.PatchReleaseInfo;
import dev.abykov.pets.relmgr.core.service.PatchReleaseAnalysisService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PatchReleaseAnalysisViewModel {

    private final StringProperty baseVersion = new SimpleStringProperty();
    private final StringProperty resultText = new SimpleStringProperty();

    private final PatchReleaseAnalysisService patchReleaseAnalysisService;

    public PatchReleaseAnalysisViewModel(PatchReleaseAnalysisService patchReleaseAnalysisService) {
        this.patchReleaseAnalysisService = patchReleaseAnalysisService;
    }

    public void analyze() {
        PatchReleaseInfo result = patchReleaseAnalysisService.analyze(baseVersion.get());
        resultText.set(result.report());
    }

    public StringProperty baseVersionProperty() {
        return baseVersion;
    }

    public StringProperty resultTextProperty() {
        return resultText;
    }
}

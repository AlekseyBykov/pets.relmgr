package dev.abykov.pets.relmgr.ui;

import dev.abykov.pets.relmgr.core.service.*;
import dev.abykov.pets.relmgr.ui.tabs.letter.LetterService;
import dev.abykov.pets.relmgr.ui.tabs.letter.LetterTab;
import dev.abykov.pets.relmgr.ui.tabs.letter.LetterViewModel;
import dev.abykov.pets.relmgr.ui.tabs.patchbytask.PatchByTaskTab;
import dev.abykov.pets.relmgr.ui.tabs.patchbytask.message.PatchByTaskMessageSubTab;
import dev.abykov.pets.relmgr.ui.tabs.patchbytask.message.PatchByTaskMessageViewModel;
import dev.abykov.pets.relmgr.ui.tabs.patchbytask.zip.PatchByTaskZipSubTab;
import dev.abykov.pets.relmgr.ui.tabs.patchbytask.zip.PatchByTaskZipViewModel;
import dev.abykov.pets.relmgr.ui.tabs.patchrelease.PatchReleaseTab;
import dev.abykov.pets.relmgr.ui.tabs.patchrelease.analysis.PatchReleaseAnalysisSubTab;
import dev.abykov.pets.relmgr.ui.tabs.patchrelease.analysis.PatchReleaseAnalysisViewModel;
import dev.abykov.pets.relmgr.ui.tabs.patchrelease.message.PatchReleaseMessageSubTab;
import dev.abykov.pets.relmgr.ui.tabs.patchrelease.message.PatchReleaseMessageViewModel;
import dev.abykov.pets.relmgr.ui.tabs.patchrelease.nexus.PatchReleaseNexusSubTab;
import dev.abykov.pets.relmgr.ui.tabs.patchrelease.nexus.PatchReleaseNexusViewModel;
import dev.abykov.pets.relmgr.ui.tabs.patchrelease.zip.PatchReleaseZipSubTab;
import dev.abykov.pets.relmgr.ui.tabs.patchrelease.zip.PatchReleaseZipViewModel;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MainWindow {

    private final PatchMessageService patchMessageService;
    private final PatchReleaseAnalysisService patchReleaseAnalysisService;
    private final PatchReleaseZipService patchReleaseZipService;

    private final PatchByTaskService patchByTaskService;
    private final NexusUploadService nexusUploadService;
    private final LetterService letterService;
    private final MailService mailService;

    public MainWindow(
            PatchLetterService patchLetterService,
            PatchMessageService patchMessageService,
            PatchReleaseAnalysisService patchReleaseAnalysisService,
            PatchReleaseZipService patchReleaseZipService,
            PatchByTaskService patchByTaskService,
            NexusUploadService nexusUploadService,
            JiraService jiraService,
            MailService mailService
    ) {
        this.patchMessageService = patchMessageService;
        this.patchReleaseAnalysisService = patchReleaseAnalysisService;
        this.patchReleaseZipService = patchReleaseZipService;
        this.patchByTaskService = patchByTaskService;
        this.nexusUploadService = nexusUploadService;
        this.letterService = new LetterService(jiraService, patchLetterService);
        this.mailService = mailService;
    }

    public Parent createMainUI() {
        TabPane pane = new TabPane();

        pane.getTabs().add(createFinalPatchTab());
        pane.getTabs().add(createPatchReleaseTab());
        pane.getTabs().add(createPatchByTaskTab());

        Label footer = new Label("JavaFX Desktop Application");
        footer.setStyle("-fx-font-size: 10px; -fx-text-fill: #666; -fx-padding: 4 0 4 6;");

        VBox root = new VBox();
        VBox.setVgrow(pane, Priority.ALWAYS);

        root.getChildren().addAll(
                pane,
                footer
        );

        return root;
    }

    private Tab createFinalPatchTab() {
        LetterViewModel vm = new LetterViewModel(letterService, mailService);
        LetterTab letterTab = new LetterTab(vm);

        Tab tab = new Tab("Финальные патчи");
        tab.setContent(letterTab.getContent());
        tab.setClosable(false);
        return tab;
    }

    private Tab createPatchReleaseTab() {
        PatchReleaseAnalysisViewModel analysisVM =
                new PatchReleaseAnalysisViewModel(patchReleaseAnalysisService);
        PatchReleaseAnalysisSubTab analysisTab =
                new PatchReleaseAnalysisSubTab(analysisVM);

        PatchReleaseZipViewModel zipVM =
                new PatchReleaseZipViewModel(patchReleaseZipService);
        PatchReleaseZipSubTab zipTab =
                new PatchReleaseZipSubTab(zipVM);

        PatchReleaseMessageViewModel messageVM =
                new PatchReleaseMessageViewModel(patchMessageService);
        PatchReleaseMessageSubTab messageTab =
                new PatchReleaseMessageSubTab(messageVM);

        PatchReleaseNexusViewModel nexusVM =
                new PatchReleaseNexusViewModel(nexusUploadService);
        PatchReleaseNexusSubTab nexusTab =
                new PatchReleaseNexusSubTab(nexusVM);

        PatchReleaseTab patchReleaseTab = new PatchReleaseTab(analysisTab, zipTab, messageTab, nexusTab);

        Tab tab = new Tab("Релиз патчи");
        tab.setContent(patchReleaseTab.getContent());
        tab.setClosable(false);
        return tab;
    }

    private Tab createPatchByTaskTab() {
        PatchByTaskZipViewModel zipVm =
                new PatchByTaskZipViewModel(patchByTaskService);
        PatchByTaskZipSubTab zipTab =
                new PatchByTaskZipSubTab(zipVm);

        PatchByTaskMessageViewModel msgVm =
                new PatchByTaskMessageViewModel();
        PatchByTaskMessageSubTab msgTab =
                new PatchByTaskMessageSubTab(msgVm);

        PatchByTaskTab patchByTaskTab = new PatchByTaskTab(zipTab, msgTab);

        Tab tab = new Tab("Патч по задаче");
        tab.setClosable(false);
        tab.setContent(patchByTaskTab.getContent());

        return tab;
    }
}

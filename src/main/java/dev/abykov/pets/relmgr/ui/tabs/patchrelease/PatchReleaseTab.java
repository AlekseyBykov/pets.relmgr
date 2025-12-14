package dev.abykov.pets.relmgr.ui.tabs.patchrelease;

import dev.abykov.pets.relmgr.ui.tabs.patchrelease.analysis.PatchReleaseAnalysisSubTab;
import dev.abykov.pets.relmgr.ui.tabs.patchrelease.message.PatchReleaseMessageSubTab;
import dev.abykov.pets.relmgr.ui.tabs.patchrelease.nexus.PatchReleaseNexusSubTab;
import dev.abykov.pets.relmgr.ui.tabs.patchrelease.zip.PatchReleaseZipSubTab;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class PatchReleaseTab {

    private final PatchReleaseAnalysisSubTab analysisTab;
    private final PatchReleaseZipSubTab zipTab;
    private final PatchReleaseMessageSubTab messageTab;
    private final PatchReleaseNexusSubTab nexusTab;

    public PatchReleaseTab(
            PatchReleaseAnalysisSubTab analysisTab,
            PatchReleaseZipSubTab zipTab,
            PatchReleaseMessageSubTab messageTab,
            PatchReleaseNexusSubTab nexusTab
    ) {
        this.analysisTab = analysisTab;
        this.zipTab = zipTab;
        this.messageTab = messageTab;
        this.nexusTab = nexusTab;
    }

    public Parent getContent() {
        TabPane pane = new TabPane();

        pane.getTabs().add(buildTab("Анализ", analysisTab.getContent()));
        pane.getTabs().add(buildTab("ZIP", zipTab.getContent()));
        pane.getTabs().add(buildTab("Сообщение", messageTab.getContent()));
        pane.getTabs().add(buildTab("Nexus", nexusTab.getContent()));

        return pane;
    }

    private Tab buildTab(String title, Parent content) {
        Tab tab = new Tab(title);
        tab.setClosable(false);
        tab.setContent(content);
        return tab;
    }
}

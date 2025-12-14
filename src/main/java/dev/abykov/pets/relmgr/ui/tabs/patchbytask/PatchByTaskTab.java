package dev.abykov.pets.relmgr.ui.tabs.patchbytask;

import dev.abykov.pets.relmgr.ui.tabs.patchbytask.message.PatchByTaskMessageSubTab;
import dev.abykov.pets.relmgr.ui.tabs.patchbytask.zip.PatchByTaskZipSubTab;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class PatchByTaskTab {

    private final PatchByTaskZipSubTab zipTab;
    private final PatchByTaskMessageSubTab messageTab;

    public PatchByTaskTab(
            PatchByTaskZipSubTab zipTab,
            PatchByTaskMessageSubTab messageTab
    ) {
        this.zipTab = zipTab;
        this.messageTab = messageTab;
    }

    public Parent getContent() {
        TabPane pane = new TabPane();

        pane.getTabs().add(buildTab("ZIP", zipTab.getContent()));
        pane.getTabs().add(buildTab("Сообщение", messageTab.getContent()));

        return pane;
    }

    private Tab buildTab(String title, Parent content) {
        Tab tab = new Tab(title);
        tab.setClosable(false);
        tab.setContent(content);
        return tab;
    }
}

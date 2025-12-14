package dev.abykov.pets.relmgr.ui.tabs.patchbytask.message;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Arrays;
import java.util.List;

public class PatchByTaskMessageViewModel {

    private final StringProperty patchVersion = new SimpleStringProperty("");
    private final StringProperty issuesText = new SimpleStringProperty("");
    private final StringProperty message = new SimpleStringProperty("");

    public void generateMessage() {
        List<String> issues = Arrays.stream(issuesText.get().split("\\s+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        if (issues.isEmpty()) {
            message.set("Не указаны задачи.");
            return;
        }

        String firstIssue = issues.getFirst();

        String msg = """
                Сформирован патч: %s
                
                Включает задачи:
                %s
                
                Патч приложен к:
                https://.../browse/%s
                """
                .formatted(
                        patchVersion.get(),
                        String.join("\n", issues),
                        firstIssue
                );

        message.set(msg);
    }

    public StringProperty patchVersionProperty() {
        return patchVersion;
    }

    public StringProperty issuesTextProperty() {
        return issuesText;
    }

    public StringProperty messageProperty() {
        return message;
    }
}

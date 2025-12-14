package dev.abykov.pets.relmgr.ui.tabs.letter;

import dev.abykov.pets.relmgr.core.domain.JiraIssue;
import dev.abykov.pets.relmgr.core.service.MailService;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LetterViewModel {

    private final StringProperty version = new SimpleStringProperty("");
    private final StringProperty patchUrl = new SimpleStringProperty("");
    private final StringProperty sqlUrl = new SimpleStringProperty("");

    private final StringProperty sendStatus = new SimpleStringProperty("");
    private final BooleanProperty sending = new SimpleBooleanProperty(false);

    private final StringProperty letterHtml = new SimpleStringProperty();
    private final BooleanProperty loading = new SimpleBooleanProperty(false);

    private final LetterService letterService;
    private final MailService mailService;

    public LetterViewModel(LetterService letterService, MailService mailService) {
        this.letterService = letterService;
        this.mailService = mailService;
    }

    public void generateLetter() {
        loading.set(true);

        CompletableFuture
                .supplyAsync(() -> {
                    List<JiraIssue> issues = letterService.loadIssues(version.get());
                    return letterService.generateLetter(
                            version.get(),
                            patchUrl.get(),
                            sqlUrl.get(),
                            issues
                    );
                })
                .thenAccept(html -> Platform.runLater(() -> {
                    letterHtml.set(html);
                    loading.set(false);
                }))
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        letterHtml.set("<p style='color:red'>Ошибка: " + ex.getMessage() + "</p>");
                        loading.set(false);
                    });
                    return null;
                });
    }

    public void sendLetter() {
        if (letterHtml.get() == null || letterHtml.get().isBlank()) {
            sendStatus.set("Сначала сформируйте письмо.");
            return;
        }

        sending.set(true);
        sendStatus.set("");

        try {
            mailService.send(version.get(), letterHtml.get());
            sendStatus.set("Письмо успешно отправлено.");
        } catch (Exception e) {
            sendStatus.set("Ошибка отправки: " + e.getMessage());
        } finally {
            sending.set(false);
        }
    }

    public StringProperty versionProperty() {
        return version;
    }

    public StringProperty patchUrlProperty() {
        return patchUrl;
    }

    public StringProperty sqlUrlProperty() {
        return sqlUrl;
    }

    public StringProperty letterHtmlProperty() {
        return letterHtml;
    }

    public BooleanProperty loadingProperty() {
        return loading;
    }

    public StringProperty sendStatusProperty() {
        return sendStatus;
    }

    public BooleanProperty sendingProperty() {
        return sending;
    }
}

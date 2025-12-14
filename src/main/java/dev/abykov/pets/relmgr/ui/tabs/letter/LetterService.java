package dev.abykov.pets.relmgr.ui.tabs.letter;

import dev.abykov.pets.relmgr.core.domain.JiraIssue;
import dev.abykov.pets.relmgr.core.service.JiraService;
import dev.abykov.pets.relmgr.core.service.PatchLetterService;

import java.util.List;

public class LetterService {

    private final JiraService jiraService;
    private final PatchLetterService letterService;

    public LetterService(JiraService jiraService, PatchLetterService letterService) {
        this.jiraService = jiraService;
        this.letterService = letterService;
    }

    public List<JiraIssue> loadIssues(String version) {
        return jiraService.loadIssues(version);
    }

    public String generateLetter(
            String version,
            String patchUrl,
            String sqlUrl,
            List<JiraIssue> issues
    ) {
        return letterService.generateHtml(version, patchUrl, sqlUrl, issues);
    }
}

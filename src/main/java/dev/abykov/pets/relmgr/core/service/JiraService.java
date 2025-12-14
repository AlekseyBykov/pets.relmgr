package dev.abykov.pets.relmgr.core.service;

import dev.abykov.pets.relmgr.core.domain.JiraIssue;
import dev.abykov.pets.relmgr.core.infra.JiraClient;

import java.util.List;

public class JiraService {

    public List<JiraIssue> loadIssues(String version) {
        return JiraClient.getIssuesForVersion(version);
    }
}

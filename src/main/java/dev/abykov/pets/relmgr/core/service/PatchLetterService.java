package dev.abykov.pets.relmgr.core.service;

import dev.abykov.pets.relmgr.core.domain.JiraIssue;
import dev.abykov.pets.relmgr.core.infra.TemplateRenderer;

import java.util.List;
import java.util.Map;

public class PatchLetterService {

    private final TemplateRenderer renderer;

    public PatchLetterService(TemplateRenderer renderer) {
        this.renderer = renderer;
    }

    public String generateHtml(
            String version,
            String patchUrl,
            String sqlUrl,
            List<JiraIssue> issues
    ) {
        Map<String, Object> model = Map.of(
                "ver", version,
                "patchUrl", patchUrl,
                "sqlUrl", sqlUrl,
                "issues", issues
        );

        return renderer.render("patch_letter.ftl", model);
    }
}

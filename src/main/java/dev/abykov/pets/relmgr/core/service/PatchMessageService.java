package dev.abykov.pets.relmgr.core.service;

import dev.abykov.pets.relmgr.core.infra.TemplateRenderer;

import java.util.List;
import java.util.Map;

public class PatchMessageService {

    private final TemplateRenderer renderer;

    public PatchMessageService(TemplateRenderer renderer) {
        this.renderer = renderer;
    }

    public String generateMessage(
            String message,
            String url,
            String installTargets,
            List<String> issues
    ) {
        Map<String, Object> model = Map.of(
                "version", message,
                "url", url,
                "installTargets", installTargets,
                "issues", issues
        );

        return renderer.render("patch_message.ftl", model);
    }
}

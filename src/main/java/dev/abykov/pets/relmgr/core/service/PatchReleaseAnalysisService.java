package dev.abykov.pets.relmgr.core.service;

import dev.abykov.pets.relmgr.core.domain.PatchReleaseInfo;
import dev.abykov.pets.relmgr.core.infra.GitTagFinder;
import dev.abykov.pets.relmgr.core.util.PatchVersionParser;

import java.util.Optional;

public class PatchReleaseAnalysisService {

    private final GitTagFinder tagFinder;

    public PatchReleaseAnalysisService(GitTagFinder tagFinder) {
        this.tagFinder = tagFinder;
    }

    public PatchReleaseInfo analyze(String baseVersion) {
        Optional<String> latestCurrentFix = tagFinder.findLatestFix(baseVersion);

        String usedBaseVersion = baseVersion;

        String previousTag;
        int newFixNumber;

        if (latestCurrentFix.isPresent()) {
            previousTag = latestCurrentFix.get();
            int prevFixNumber = PatchVersionParser.extractFixNumber(previousTag);
            newFixNumber = prevFixNumber + 1;
        } else {
            String previousBase = PatchVersionParser.decrementBaseVersion(baseVersion);
            usedBaseVersion = previousBase;

            Optional<String> firstPrevFix = tagFinder.findFirstFix(previousBase);

            if (firstPrevFix.isEmpty()) {
                return buildNotFoundResult(baseVersion, previousBase);
            }

            previousTag = firstPrevFix.get();
            newFixNumber = 1;
        }

        String newTag = baseVersion + "_fix" + newFixNumber;

        String previousUrl = buildPatchUrl(previousTag);
        String newUrl = buildPatchUrl(newTag);

        String report = buildReport(
                baseVersion,
                usedBaseVersion,
                previousTag,
                newTag,
                previousUrl,
                newUrl
        );

        return new PatchReleaseInfo(newTag, newUrl, report);
    }

    private PatchReleaseInfo buildNotFoundResult(String requestedBase, String prevBase) {
        String report = "Не найдено ни одного патч-тега ни для %s ни для %s"
                .formatted(requestedBase, prevBase);
        return new PatchReleaseInfo(null, null, report);
    }

    private String buildPatchUrl(String tag) {
        return "https://jira.../patches/content/repositories/patch/VERSION_XX/m/"
                + tag + "/m-" + tag + ".zip";
    }

    private String buildReport(
            String requestedBase,
            String usedBase,
            String previousTag,
            String newTag,
            String previousUrl,
            String newUrl
    ) {
        return """
                Анализ патча:
                
                Запрошенная базовая версия: %s
                Используемая база: %s
                
                Найден предыдущий фикс: %s
                Новый фикс: %s
                
                URL предыдущего фикса:
                %s
                
                URL нового фикса:
                %s
                """
                .formatted(
                        requestedBase,
                        usedBase,
                        previousTag,
                        newTag,
                        previousUrl,
                        newUrl
                );
    }
}

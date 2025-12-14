package dev.abykov.pets.relmgr.core.infra;

import dev.abykov.pets.relmgr.core.util.PatchVersionParser;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GitTagFinder {

    private final File repoDir;

    public GitTagFinder(File repoDir) {
        this.repoDir = repoDir;
    }

    public List<String> findFixTagsForBase(String baseVersion) {
        String prefix = baseVersion + "_fix";

        return getAllNormalizedTags().stream()
                .filter(tag -> tag.startsWith(prefix))
                // сортируем по числовому значению fixN
                .sorted(Comparator.comparingInt(PatchVersionParser::extractFixNumber))
                .collect(Collectors.toList());
    }

    public Optional<String> findFirstFix(String baseVersion) {
        List<String> tags = findFixTagsForBase(baseVersion);
        if (tags.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(tags.getFirst());
    }

    public Optional<String> findLatestFix(String baseVersion) {
        List<String> tags = findFixTagsForBase(baseVersion);
        if (tags.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(tags.getLast());
    }

    private List<String> getAllNormalizedTags() {
        try (Git git = Git.open(repoDir)) {

            List<Ref> refs = git.tagList().call();

            return refs.stream()
                    .map(ref -> ref.getName().replace("refs/tags/", ""))
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Не удалось получить список тегов Git", e);
        }
    }
}

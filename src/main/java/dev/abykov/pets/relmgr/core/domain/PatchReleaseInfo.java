package dev.abykov.pets.relmgr.core.domain;

public record PatchReleaseInfo(
        String newVersion,
        String nexusUrl,
        String report
) {
}

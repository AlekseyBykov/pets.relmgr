package dev.abykov.pets.relmgr.core.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.zip.*;

public class PatchByTaskService {

    public record Result(String report, String testerMessage) {
    }

    public Result patch(
            String appVersion,
            List<String> issues,
            String buildDateTime,
            String coreVersion,
            File templateZip
    ) {
        if (!isTemplateValid(templateZip)) {
            return new Result("Не выбран архив-прототип", "");
        }

        String suffix = String.join("_", issues);
        String fullVersion = appVersion + "_" + suffix;
        String outputName = "patch_" + fullVersion + ".zip";

        Path output = templateZip.toPath().getParent().resolve(outputName);

        try {
            patch(templateZip.toPath(), output, fullVersion, buildDateTime);
        } catch (Exception e) {
            return new Result("Ошибка при обработке архива: " + e.getMessage(), "");
        }

        String report = buildReport(fullVersion, outputName, buildDateTime, coreVersion);
        return new Result(
                report,
                buildTesterMessage(issues)
        );
    }

    private boolean isTemplateValid(File file) {
        return file != null && file.exists() && file.isFile();
    }

    private void patch(Path inputZip, Path outputZip, String fullVersion, String buildDateTime) throws Exception {
        try (ZipInputStream zin = new ZipInputStream(Files.newInputStream(inputZip));
             ZipOutputStream zout = new ZipOutputStream(Files.newOutputStream(outputZip))) {

            ZipEntry entry;

            while ((entry = zin.getNextEntry()) != null) {
                String name = entry.getName();
                byte[] data = zin.readAllBytes();

                if (name.endsWith("version.txt")) {
                    data = processVersionTxt(data, fullVersion, buildDateTime);
                }

                if (name.endsWith("app.version")) {
                    data = processAppVersion(data, fullVersion, buildDateTime);
                }

                writeZipEntry(zout, name, data);
            }
        }
    }

    private byte[] processVersionTxt(byte[] data, String fullVersion, String dateTime) {
        String text = new String(data, StandardCharsets.UTF_8);

        text = text.replaceAll(
                "Build version\\s*:.*",
                "Build version      : " + fullVersion
        );
        text = text.replaceAll(
                "Compile date/time\\s*:.*",
                "Compile date/time  : " + dateTime
        );

        return text.getBytes(StandardCharsets.UTF_8);
    }

    private byte[] processAppVersion(byte[] data, String fullVersion, String dateTime) {
        String text = new String(data, StandardCharsets.UTF_8);

        text = text.replaceAll("build.version=.*", "build.version=" + fullVersion);
        text = text.replaceAll("build.date=.*", "build.date=" + dateTime);

        return text.getBytes(StandardCharsets.UTF_8);
    }

    private void writeZipEntry(ZipOutputStream zout, String name, byte[] data) throws IOException {
        ZipEntry outEntry = new ZipEntry(name);
        zout.putNextEntry(outEntry);
        zout.write(data);
        zout.closeEntry();
    }

    private String buildReport(
            String version,
            String zipName,
            String buildDateTime,
            String coreVersion
    ) {
        return """
                Патч по задаче сформирован.
                
                Итоговая версия: %s
                Итоговый ZIP: %s
                
                Дата/время сборки: %s
                Версия основная: %s
                """
                .formatted(version, zipName, buildDateTime, coreVersion);
    }

    private String buildTesterMessage(List<String> issues) {
        if (issues.isEmpty()) {
            return "";
        }
        return "Патч по https://jira.../browse/" + issues.getFirst() + " приложен к задаче.";
    }
}

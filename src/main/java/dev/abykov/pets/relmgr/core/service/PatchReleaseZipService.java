package dev.abykov.pets.relmgr.core.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.zip.*;

public class PatchReleaseZipService {

    public record Result(String report) {
    }

    public Result patchZip(
            String appVersion,
            String fullVersion,
            String buildDateTime,
            String coreVersion,
            File templateZip
    ) {
        if (templateZip == null || !templateZip.exists()) {
            return new Result("Не выбран архив-прототип.");
        }

        String outputName = "m-" + fullVersion + ".zip";
        Path output = templateZip.toPath().getParent().resolve(outputName);

        try {
            patchZip(templateZip.toPath(), output, appVersion, buildDateTime);
        } catch (Exception e) {
            return new Result("Ошибка при обработке архива: " + e.getMessage());
        }

        String report = """
                Патч сформирован.
                
                Версия: %s
                Итоговая версия: %s
                Дата/время сборки: %s
                Версия основная: %s
                
                ZIP: %s
                """.formatted(
                appVersion,
                fullVersion,
                buildDateTime,
                coreVersion,
                outputName
        );

        return new Result(report);
    }

    private void patchZip(
            Path inputZip,
            Path outputZip,
            String appVersion,
            String buildDateTime
    ) throws Exception {
        try (ZipInputStream zin = new ZipInputStream(Files.newInputStream(inputZip));
             ZipOutputStream zout = new ZipOutputStream(Files.newOutputStream(outputZip))) {

            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                String name = entry.getName();
                byte[] data = zin.readAllBytes();

                if (name.endsWith("version.txt")) {
                    data = processVersionTxt(data, appVersion, buildDateTime);
                }

                if (name.endsWith("app.version")) {
                    data = processAppVersion(data, appVersion, buildDateTime);
                }

                writeZipEntry(zout, name, data);
            }
        }
    }

    private byte[] processVersionTxt(byte[] data, String appVersion, String dateTime) {
        String text = new String(data, StandardCharsets.UTF_8);

        text = text.replaceAll(
                "Build version\\s*:.*",
                "Build version      : " + appVersion
        );
        text = text.replaceAll(
                "Compile date/time\\s*:.*",
                "Compile date/time  : " + dateTime
        );

        return text.getBytes(StandardCharsets.UTF_8);
    }

    private byte[] processAppVersion(byte[] data, String appVersion, String dateTime) {
        String text = new String(data, StandardCharsets.UTF_8);

        text = text.replaceAll("build.version=.*", "build.version=" + appVersion);
        text = text.replaceAll("build.date=.*", "build.date=" + dateTime);

        return text.getBytes(StandardCharsets.UTF_8);
    }

    private void writeZipEntry(ZipOutputStream zout, String name, byte[] data) throws IOException {
        ZipEntry outEntry = new ZipEntry(name);
        zout.putNextEntry(outEntry);
        zout.write(data);
        zout.closeEntry();
    }
}

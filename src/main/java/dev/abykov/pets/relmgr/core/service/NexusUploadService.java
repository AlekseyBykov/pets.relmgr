package dev.abykov.pets.relmgr.core.service;

import dev.abykov.pets.relmgr.core.infra.SslTrustDisabler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;

public class NexusUploadService {

    private static final String NEXUS_UPLOAD_URL =
            "https://nexus.../service/rest/v1/components?repository=patches";

    private static final String USERNAME = "...";
    private static final String PASSWORD = "...";

    public String upload(File zipFile, String patchVersion) {
        try {
            SslTrustDisabler.disableSslVerification();

            String boundary = "----RelMgrBoundary";
            URL url = new URL(NEXUS_UPLOAD_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            String basicAuth = Base64.getEncoder()
                    .encodeToString((USERNAME + ":" + PASSWORD).getBytes());
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            try (OutputStream out = conn.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(out))) {

                writer.append("--").append(boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"raw.directory\"\r\n\r\n");
                writer.append("m/").append(patchVersion).append("\r\n");

                writer.append("--").append(boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"raw.asset1\"; filename=\"m-")
                        .append(patchVersion).append(".zip\"\r\n");
                writer.append("Content-Type: application/zip\r\n\r\n");
                writer.flush();

                Files.copy(zipFile.toPath(), out);
                out.flush();
                writer.append("\r\n");

                writer.append("--").append(boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"raw.asset1.filename\"\r\n\r\n");
                writer.append("m-").append(patchVersion).append(".zip\r\n");

                writer.append("--").append(boundary).append("--\r\n");
                writer.flush();
            }

            int code = conn.getResponseCode();

            String responseBody = "";
            try (InputStream err = conn.getErrorStream()) {
                if (err != null) {
                    responseBody = new String(err.readAllBytes());
                }
            }
            try (InputStream in = conn.getInputStream()) {
                if (in != null && responseBody.isEmpty()) {
                    responseBody = new String(in.readAllBytes());
                }
            }

            if (code == 204) {
                return "Патч " + patchVersion + " успешно загружен в Nexus.";
            }

            return "Ошибка Nexus. Код=" + code + "\nОтвет сервера:\n" + responseBody;

        } catch (Exception e) {
            return "Ошибка загрузки: " + e.getMessage();
        }
    }
}

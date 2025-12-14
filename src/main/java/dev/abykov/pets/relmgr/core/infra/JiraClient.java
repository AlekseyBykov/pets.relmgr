package dev.abykov.pets.relmgr.core.infra;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.abykov.pets.relmgr.core.domain.JiraIssue;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JiraClient {

    private static final String BASE = "https://jira...";

    private static final String AUTH = JiraAuthConfig.getAuthHeader();

    public static List<JiraIssue> getIssuesForVersion(String version) {
        try {
            String jql = URLEncoder.encode(
                    "project = PROJ AND fixVersion = \"" + version + "\"",
                    StandardCharsets.UTF_8
            );

            String fields = URLEncoder.encode("key,summary", StandardCharsets.UTF_8);

            String url = BASE + "/rest/api/2/search?jql=" + jql + "&fields=" + fields;

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", AUTH)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            String json = resp.body();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            List<JiraIssue> issues = new ArrayList<>();

            if (root.has("issues")) {
                for (JsonNode issue : root.get("issues")) {
                    issues.add(new JiraIssue(
                            issue.get("key").asText(),
                            issue.get("fields").get("summary").asText()
                    ));
                }
            }

            return issues;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}

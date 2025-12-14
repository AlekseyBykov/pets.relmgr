package dev.abykov.pets.relmgr;

import dev.abykov.pets.relmgr.core.infra.GitTagFinder;
import dev.abykov.pets.relmgr.core.infra.TemplateRenderer;
import dev.abykov.pets.relmgr.core.service.*;
import dev.abykov.pets.relmgr.ui.MainWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class App extends Application {

    private static final String DEFAULT_REPO_PATH =
            System.getProperty("user.home") + "/.....";

    private PatchLetterService patchLetterService;
    private PatchMessageService patchMessageService;
    private PatchReleaseAnalysisService patchReleaseAnalysisService;
    private PatchReleaseZipService patchReleaseZipService;
    private PatchByTaskService patchByTaskService;
    private NexusUploadService nexusUploadService;
    private JiraService jiraService;
    private MailService mailService;

    @Override
    public void init() {
        initServices();
    }

    private void initServices() {
        jiraService = new JiraService();

        TemplateRenderer templateRenderer = new TemplateRenderer();
        patchLetterService = new PatchLetterService(templateRenderer);
        patchMessageService = new PatchMessageService(templateRenderer);

        GitTagFinder tagFinder = new GitTagFinder(new File(DEFAULT_REPO_PATH));
        patchReleaseAnalysisService = new PatchReleaseAnalysisService(tagFinder);

        patchByTaskService = new PatchByTaskService();
        nexusUploadService = new NexusUploadService();
        patchReleaseZipService = new PatchReleaseZipService();

        mailService = new MailService();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("RelMgr — Release management tool v" + AppInfo.version());

        MainWindow mainWindow = createMainWindow();
        Scene scene = new Scene(mainWindow.createMainUI(), 900, 600);

        stage.setScene(scene);
        stage.show();
    }

    private MainWindow createMainWindow() {
        return new MainWindow(
                patchLetterService,
                patchMessageService,
                patchReleaseAnalysisService,
                patchReleaseZipService,
                patchByTaskService,
                nexusUploadService,
                jiraService,
                mailService
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}

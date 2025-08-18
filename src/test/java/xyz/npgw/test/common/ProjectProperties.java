package xyz.npgw.test.common;

import com.microsoft.playwright.options.ColorScheme;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

import static xyz.npgw.test.common.util.BrowserUtils.initObject;

public final class ProjectProperties {

    private static final String PROJECT_PROPERTIES = "project.properties";

    @Getter
    @Setter
    private static String baseURL = "https://test.npgw.xyz";
    @Getter
    @Setter
    private static String email;
    @Getter
    @Setter
    private static String password;
    @Getter
    @Setter
    private static boolean tracingMode = false;
    @Getter
    @Setter
    private static boolean videoMode = false;
    @Getter
    @Setter
    private static boolean closeBrowserIfError = true;
    @Getter
    @Setter
    private static ColorScheme colorScheme = ColorScheme.DARK;
    @Getter
    @Setter
    private static Path artefactDir = Path.of("target/artefact");
    @Getter
    @Setter
    private static double defaultTimeout = 5000.0;
    @Getter
    @Setter
    private static int additionalRetries = 0;

    static {
        initObject(ProjectProperties.class, PROJECT_PROPERTIES);
    }
}

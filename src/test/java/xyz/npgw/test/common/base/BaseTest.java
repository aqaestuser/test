package xyz.npgw.test.common.base;

import org.testng.annotations.AfterSuite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseTest {
    protected static final Map<String, Long> classDurations = new ConcurrentHashMap<>();
    protected long startTime;

    @AfterSuite
    protected void afterSuite() throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("Class,Duration(s)");
        lines.addAll(
                classDurations.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .map(e -> e.getKey() + "," + e.getValue())
                        .toList()
        );

        Files.write(Paths.get("durations.csv"), lines);
    }
}

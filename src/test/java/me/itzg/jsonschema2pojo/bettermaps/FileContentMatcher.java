package me.itzg.jsonschema2pojo.bettermaps;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author geof0549
 * @since Mar 2017
 */
public class FileContentMatcher extends BaseMatcher<Path> {

    private String expected;

    public FileContentMatcher(String expected) {
        this.expected = expected;
    }

    public static FileContentMatcher fileContains(String expected) {
        return new FileContentMatcher(expected);
    }
    @Override
    public boolean matches(Object o) {

        final Path path = (Path) o;

        try {
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(expected)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }

        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a file that contains:").appendValue(expected);
    }
}

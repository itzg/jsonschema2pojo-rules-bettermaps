package me.itzg.jsonschema2pojo.bettermaps;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.writer.FileCodeWriter;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static me.itzg.jsonschema2pojo.bettermaps.FileContentMatcher.fileContains;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * @author geof0549
 * @since Mar 2017
 */
public class BetterMapsRuleFactoryTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testPrimaryScenario() throws Exception {
        final DefaultGenerationConfig config = new DefaultGenerationConfig();
        final SchemaMapper mapper = new SchemaMapper(new BetterMapsRuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());


        final URL source = BetterMapsRuleFactoryTest.class.getResource("/test-schema.json");
        final JCodeModel codeModel = new JCodeModel();
        mapper.generate(codeModel, "MyClass", "com.example", source);

        final File outFile = tempFolder.newFolder();

        codeModel.build(new FileCodeWriter(outFile));

        final Path outPath = outFile.toPath();
        final List<String> contents = Files.walk(outPath)
                .filter(path -> Files.isRegularFile(path))
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList());

        assertThat(contents, hasItems("Baz.java", "BazProperty.java", "Boo.java", "MyClass.java"));
        assertThat(contents, not(hasItems("Bars.java", "Times.java")));

        final Path myClassJava = outPath.resolve("com").resolve("example").resolve("MyClass.java");
        assertThat(myClassJava, fileContains("private Map<String, String> bars;"));
        assertThat(myClassJava, fileContains("private Map<String, Integer> times;"));
    }
}
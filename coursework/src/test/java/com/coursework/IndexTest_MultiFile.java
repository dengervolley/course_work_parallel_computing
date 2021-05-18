package com.coursework;

import com.coursework.indices.InverseIndex;
import com.coursework.loggers.ConsoleLogger;
import com.coursework.persistence.StubPersistenceProvider;
import com.coursework.suppliers.StubTextSupplier;
import com.coursework.tokenizers.TextTokenizer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IndexTest_MultiFile {
    private static InverseIndex index;
    private static StubTextSupplier textSupplier;
    private static Map<String, Integer> simpleIndex;
    private static Map<String, Integer> specialCharsIndex;

    private static final String[] testFiles = {"src/test.txt", "src/test2.txt"};
    private static final String oneLine = "a a a aa b;b bb test testtest";
    private static final String multiLine = "a a a aa;\n b b bb\n test testtest\n";
    private static final String specialCharsOneLine = "a'b a'b a'b;b'a b'a t'est";
    private static final String specialCharsMultiLine = "a'b\na'b a'b;b'a\nb'a\n t'est";
    private static final String fileSeparator = ";";

    @BeforeAll
    public static void setupDependencies() {
        textSupplier = new StubTextSupplier();
        var tokenizer = new TextTokenizer(textSupplier);
        index = new InverseIndex(
                tokenizer,
                new StubPersistenceProvider(),
                false, 1,
                new ConsoleLogger(),
                testFiles
        );

        simpleIndex = Stream.of(new Object[][]{
                {"a", 3},
                {"aa", 1},
                {"b", 2},
                {"bb", 1},
                {"test", 1},
                {"testtest", 1}
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

        specialCharsIndex = Stream.of(new Object[][]{
                {"a'b", 3},
                {"b'a", 2},
                {"t'est", 1}

        }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

    }

    private static void checkAsserts(InverseIndex index, Map<String, Integer> testIndex) {
        var idx = index.getIndex();
        for (var pair : testIndex.entrySet()) {
            assertTrue(idx.getOrDefault(pair.getKey(), null) != null);
            assertTrue(idx.get(pair.getKey()).getTotalCount().equals(pair.getValue()));
        }
    }

    private static void setupFiles(String text, String[] files, String fileSeparator) throws IOException {
        textSupplier.clearFiles();
        var splitText = text.split(fileSeparator);
        for (int i = 0; i < files.length; i++) {
            textSupplier.addFileContent(files[i], splitText[i]);
        }
    }

    @Test
    public void BuildIndex_OneLine_AssertsTrue() throws IOException {
        setupFiles(oneLine, testFiles, fileSeparator);
        index.buildIndex();
        checkAsserts(index, simpleIndex);
    }

    @Test
    public void BuildIndex_MultiLine_AssertsTrue() throws IOException {
        setupFiles(multiLine, testFiles, fileSeparator);
        index.buildIndex();
        checkAsserts(index, simpleIndex);
    }

    @Test
    public void BuildIndex_SpecialChars_OneLine_AssertsTrue() throws IOException {
        setupFiles(specialCharsOneLine, testFiles, fileSeparator);
        index.buildIndex();
        checkAsserts(index, specialCharsIndex);
    }

    @Test
    public void BuildIndex_SpecialChars_MultiLine_AssertsTrue() throws IOException {
        setupFiles(specialCharsMultiLine, testFiles, fileSeparator);
        index.buildIndex();
        checkAsserts(index, specialCharsIndex);
    }

}

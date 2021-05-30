package com.coursework;

import com.coursework.indices.InverseIndex;
import com.coursework.loggers.ConsoleLogger;
import com.coursework.persistence.StubPersistenceProvider;
import com.coursework.suppliers.StubTextSupplier;
import com.coursework.tokenizers.TextTokenizer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IndexTest_SingleFile {
    private static InverseIndex index;
    private static StubTextSupplier textSupplier;
    private static final String testFilePath = "src/test.txt";

    private static final String simpleTestOneLine = "a a a aa b b bb test testtest";
    private static final String simpleTestMultiLine = "a a a aa \n b b bb\n test testtest\n";
    private static final String specialCharsOneLine = "a'b a'b a'b b'a b'a t'est";
    private static final String specialCharsMultiLine = "a'b\na'b a'b \n b'a\nb'a\n t'est";
    private static Map<String, Integer> simpleIndex;
    private static Map<String, Integer> specialCharsIndex;

    @BeforeAll
    public static void setupDependencies() {
        textSupplier = new StubTextSupplier();
        var tokenizer = new TextTokenizer(textSupplier);
        index = new InverseIndex(
                tokenizer,
                new StubPersistenceProvider(),
                false, 1,
                new ConsoleLogger(),
                null,
                testFilePath
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

    private static void setupSingleFile(String text) throws IOException {
        var file = new File(testFilePath);
        textSupplier.addFileContent(testFilePath, text);
    }

    private static void checkAsserts(InverseIndex index, Map<String, Integer> testIndex) {
        var idx = index.getIndex();
        for (var pair : testIndex.entrySet()) {
            assertTrue(idx.getOrDefault(pair.getKey(), null) != null);
            assertTrue(idx.get(pair.getKey()).getTotalCount().equals(pair.getValue()));
        }
    }

    @Test
    public void BuildIndex_OneLine_AssertsTrue() throws IOException {
        setupSingleFile(simpleTestOneLine);
        index.buildIndex();
        checkAsserts(index, simpleIndex);
    }

    @Test
    public void BuildIndex_MultiLine_AssertsTrue() throws IOException {
        setupSingleFile(simpleTestMultiLine);
        index.buildIndex();
        checkAsserts(index, simpleIndex);
    }

    @Test
    public void BuildIndex_SpecialChars_OneLine_ShouldHaveCorrectItems() throws IOException {
        setupSingleFile(specialCharsOneLine);
        index.buildIndex();
        checkAsserts(index, specialCharsIndex);
    }

    @Test
    public void BuildIndex_SpecialChars_MultiLine_ShoudHaveCorrectItems() throws IOException {
        setupSingleFile(specialCharsMultiLine);
        index.buildIndex();
        checkAsserts(index, specialCharsIndex);
    }
}
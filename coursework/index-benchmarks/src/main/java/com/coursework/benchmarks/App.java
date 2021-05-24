package com.coursework.benchmarks;

import com.coursework.indices.InverseIndex;
import com.coursework.loggers.ConsoleLogger;
import com.coursework.persistence.FilePersistenceProvider;
import com.coursework.suppliers.FileTextSupplier;
import com.coursework.tokenizers.TextTokenizer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        var index = new InverseIndex(new TextTokenizer(new FileTextSupplier(new ConsoleLogger())),
                new FilePersistenceProvider(new ConsoleLogger()),
                1,
                new ConsoleLogger(), "index/files");
        var fileWriter = new FileWriter("benchmarks/run.csv");
        var csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader("NThreads", "NFiles", "Time"));
        for (int i = 5000; i <= 25000; i += 5000) {
            for (int j = 1; j < 30; j++) {
                index.setNumThreads(j);
                index.setNumFiles(i);
                var startTime = System.nanoTime();
                index.buildIndex();
                var endTime = System.nanoTime();
                var runTimeMillis = (endTime - startTime) / 1000000;
                System.out.println(" NThreads: " + j +
                        " NFiles: " + i +
                        " Time(ms): " + runTimeMillis);
                csvPrinter.printRecord(j, i, runTimeMillis);
                csvPrinter.flush();
            }
        }

    }
}

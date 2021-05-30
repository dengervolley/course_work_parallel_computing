package com.coursework.indices;

import com.coursework.loggers.ILogger;
import com.coursework.models.IndexItem;
import com.coursework.models.Token;
import com.coursework.persistence.IPersistenceProvider;
import com.coursework.tokenizers.ITokenizer;
import com.coursework.utils.ArrayUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.groupingBy;

public class InverseIndex {

    private HashMap<String, IndexItem> inverseIndex;
    private final ITokenizer tokenizer;
    private List<String> files;
    private final IPersistenceProvider persistenceProvider;
    private int numThreads;
    private int numFiles;
    private final ILogger logger;

    public HashMap<String, IndexItem> getIndex() {
        return this.inverseIndex;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public void setNumFiles(int numFiles) {
        this.numFiles = numFiles;
    }

    public InverseIndex(ITokenizer tokenizer, IPersistenceProvider persistenceProvider, int numThreads, ILogger logger, String directory) {
        this(tokenizer, persistenceProvider, false, numThreads, logger, directory);

    }

    public InverseIndex(ITokenizer tokenizer, IPersistenceProvider persistenceProvider,
                        boolean buildIndex, int threads, ILogger logger, String directory, String... files) {
        this.tokenizer = tokenizer;
        this.numThreads = threads;
        this.logger = logger;
        if (buildIndex)
            this.buildIndex();
        this.persistenceProvider = persistenceProvider;
        this.inverseIndex = new HashMap<>();
        if(directory != null && directory != "") {
            try{
                this.files = Files.walk(Paths.get(directory)).filter(Files::isRegularFile).map(x -> x.toString()).toList();

            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            this.files = new ArrayList<>(Arrays.asList(files));
        }
    }

    public void addFilesToIndex(String... fileNames) {
        this.files.addAll(Arrays.asList(fileNames));
        /*
        var partialIndex = buildIndexImpl(Arrays.asList(fileNames));
        for(var item: partialIndex){
            var existingItem = this.inverseIndex
                    .stream()
                    .filter(x -> x.getValue().equals(item.getValue()))
                    .findAny()
                    .orElse(null);
            if(existingItem != null){
                existingItem.getEntries().addAll(item.getEntries());
            }
            else{
                this.inverseIndex.add(item);
            }
        }
        */
    }

    public IndexItem findByValue(String value) {
        return this.inverseIndex.get(value);
    }

    public void buildIndex() {
        this.inverseIndex = buildIndexImpl(this.files);
    }

    public void getFromFile(String filePath) {
        this.inverseIndex = this.persistenceProvider.readIndex(filePath);
    }

    public void saveToFile(String filePath) {
        this.persistenceProvider.setPath(filePath);
        this.persistenceProvider.persistIndex(this);
    }

    private HashMap<String, IndexItem> buildIndexImpl(List<String> files) {
        var tokens = Collections.synchronizedList(new ArrayList<List<Token>>(files.size()));
        var chunks = ArrayUtils.nChunks(files, numThreads);
        var executor = Executors.newFixedThreadPool(numThreads);
        var latch = new CountDownLatch(chunks.size());
        AtomicInteger filesCount = new AtomicInteger();

        for (var chunk : chunks) {
            executor.submit(() -> {
                for (var file : chunk) {
                    var tokenizedFilesCount = filesCount.get();
                    if (tokenizedFilesCount > this.numFiles && this.numFiles != 0)
                        break;
                    var tokenizedFile = tokenizer.tokenize(file);
                    tokens.add(tokenizedFile);
                    int oldValue, newValue;
                    do {
                        oldValue = filesCount.get();
                        newValue = oldValue + 1;
                    } while (!filesCount.compareAndSet(oldValue, newValue));
                }
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.logError(e);
        }

        var indexItems = new HashMap<String, IndexItem>();
        var flatTokens = tokens.stream().flatMap(List::stream).toList();
        var groupedTokens = flatTokens.stream().collect(groupingBy(Token::value));

        for (var group : groupedTokens.entrySet()) {
            var indexItem = new IndexItem(group.getKey());
            for (var occurrences : group.getValue()) {
                indexItem.getEntries().add(occurrences.matches());
            }
            indexItems.put(indexItem.getValue(), indexItem);
        }
        return indexItems;
    }

    @Override
    public String toString() {
        return this.inverseIndex.toString();
    }

}

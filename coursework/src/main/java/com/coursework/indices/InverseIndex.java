package com.coursework.indices;

import com.coursework.loggers.ILogger;
import com.coursework.models.IndexItem;
import com.coursework.models.Token;
import com.coursework.persistence.IPersistenceProvider;
import com.coursework.tokenizers.ITokenizer;
import com.coursework.utils.ArrayUtils;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.groupingBy;

public class InverseIndex {

    private HashMap<String, IndexItem> inverseIndex;
    private final ITokenizer tokenizer;
    private final List<String> files;
    private final IPersistenceProvider persistenceProvider;
    private final int numThreads;
    private final ILogger logger;
    public HashMap<String, IndexItem> getIndex() {
        return this.inverseIndex;
    }

    public InverseIndex(ITokenizer tokenizer, IPersistenceProvider persistenceProvider, int numThreads, ILogger logger) {
        this(tokenizer, persistenceProvider, false, numThreads, logger);
    }

    public InverseIndex(ITokenizer tokenizer, IPersistenceProvider persistenceProvider,
                        boolean buildIndex, int threads, ILogger logger, String... files) {
        this.tokenizer = tokenizer;
        this.files = new ArrayList<>(Arrays.asList(files));
        this.numThreads = threads;
        this.logger = logger;
        if (buildIndex)
            this.buildIndex();
        this.persistenceProvider = persistenceProvider;
        this.inverseIndex = new HashMap<>();
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

    public IndexItem findByValue(String value){
        return this.inverseIndex.get(value);
    }

    public void buildIndex() {
        this.inverseIndex = buildIndexImpl(this.files);
    }

    public void getFromFile(String filePath){
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

        for (var chunk : chunks) {
            executor.submit(() -> {
                for (var file : chunk) {
                    var tokenizedFile = tokenizer.tokenize(file);
                    tokens.add(tokenizedFile);
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
    public String toString(){
        return this.inverseIndex.toString();
    }

}

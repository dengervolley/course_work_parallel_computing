package com.coursework.server;

import com.coursework.indices.InverseIndex;
import com.coursework.loggers.ConsoleLogger;
import com.coursework.models.IndexItem;
import com.coursework.persistence.FilePersistenceProvider;
import com.coursework.server.listeners.IndexListener;
import com.coursework.server.models.BuildIndexRequest;
import com.coursework.server.models.FindTermRequest;
import com.coursework.server.responses.BuiltIndexResponse;
import com.coursework.server.responses.ExceptionResponse;
import com.coursework.server.responses.FoundTermResponse;
import com.coursework.suppliers.FileTextSupplier;
import com.coursework.tokenizers.TextTokenizer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.util.concurrent.TimeUnit;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        var server = new Server(100000000, 100000000);
        Kryo kryo = server.getKryo();
        kryo.register(BuildIndexRequest.class);
        kryo.register(BuiltIndexResponse.class);
        kryo.register(FindTermRequest.class);
        kryo.register(FoundTermResponse.class);
        kryo.register(ExceptionResponse.class);
        server.bind(1337, 1338);
        server.start();

        var index = new InverseIndex(new TextTokenizer(new FileTextSupplier(new ConsoleLogger())),
                new FilePersistenceProvider(new ConsoleLogger()),
                5,
                new ConsoleLogger(), "index/files");
        server.addListener(new Listener() {
            public void received(Connection c, Object o) {
                if (o instanceof BuildIndexRequest request) {
                    try {
                        index.setNumFiles(request.getNumFiles());
                        index.setNumThreads(request.getNumThreads());
                        index.buildIndex();
                        server.sendToTCP(c.getID(), new BuiltIndexResponse());
                    }
                    catch(Exception e){
                        server.sendToTCP(c.getID(), new ExceptionResponse("An error occured: " + e.getMessage()));
                    }
                }
                if (o instanceof FindTermRequest request) {
                    var result = index.findByValue(request.getSearchTerm());
                    try{
                        server.sendToTCP(c.getID(), new FoundTermResponse(result));
                    }
                    catch(Exception e){
                        server.sendToTCP(c.getID(), new ExceptionResponse("An error occured: " + e.getMessage()));
                    }
                }
            }
        });
    }
}

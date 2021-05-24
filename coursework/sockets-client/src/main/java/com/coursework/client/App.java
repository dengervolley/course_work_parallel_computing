package com.coursework.client;

import com.coursework.models.IndexItem;
import com.coursework.server.models.BuildIndexRequest;
import com.coursework.server.models.FindTermRequest;
import com.coursework.server.responses.BuiltIndexResponse;
import com.coursework.server.responses.ExceptionResponse;
import com.coursework.server.responses.FoundTermResponse;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException {
        var client = new Client(100000, 100000);
        var kryo = client.getKryo();

        kryo.register(BuildIndexRequest.class);
        kryo.register(BuiltIndexResponse.class);
        kryo.register(FindTermRequest.class);
        kryo.register(FoundTermResponse.class);
        kryo.register(ExceptionResponse.class);

        client.start();
        client.connect(10000, "localhost", 1337, 1338);
        var indexReady = new Semaphore(1, false);
        var responseNotReady = new Semaphore(1, false);
        client.addListener(new Listener() {
            public void received(Connection connection, Object o) {
                if (o instanceof BuiltIndexResponse) {
                    indexReady.release();
                }
                if (o instanceof FoundTermResponse response) {
                    System.out.println("Found index item: " + response.getIndexItem());
                    responseNotReady.release();
                }
                if (o instanceof ExceptionResponse response) {
                    System.out.println("An exception occured: " + response.getMessage());
                }
            }
        });
        client.sendTCP(new BuildIndexRequest(1500, 5));
        indexReady.acquire();
        while (true) {
            responseNotReady.acquire();
            var searchTerm = askForSearchTerm();
            client.sendTCP(new FindTermRequest(searchTerm));
        }
    }

    private static synchronized String askForSearchTerm() {
        System.out.print("Input search term: ");
        var scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}

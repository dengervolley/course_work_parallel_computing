package com.coursework.server.listeners;

import com.coursework.indices.InverseIndex;
import com.coursework.server.models.BuildIndexRequest;
import com.coursework.server.responses.BuiltIndexResponse;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class IndexListener extends Listener {
    private final InverseIndex index;
    private final Server server;
    public IndexListener(InverseIndex index, Server server) {
        this.index = index;
        this.server = server;
    }

    @Override
    public void received(Connection c, Object request) {
        if (request instanceof BuildIndexRequest) {
            var req = (BuildIndexRequest) request;
            this.index.setNumThreads(req.getNumThreads());
            this.index.setNumFiles(req.getNumFiles());
            this.index.buildIndex();
            server.sendToTCP(c.getID(), new BuiltIndexResponse());
        }
    }
}

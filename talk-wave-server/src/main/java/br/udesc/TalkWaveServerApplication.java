package br.udesc;

import br.udesc.server.Server;

import java.io.IOException;

public class TalkWaveServerApplication {

    public static void main(String[] args) throws IOException {
        Server connectionPool = new Server();
        connectionPool.startServer(8080);
    }

}

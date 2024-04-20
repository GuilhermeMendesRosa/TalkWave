package br.udesc;

import br.udesc.model.ConnectionPool;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ConnectionPool connectionPool = new ConnectionPool();
        connectionPool.startServer(8080);
    }
}

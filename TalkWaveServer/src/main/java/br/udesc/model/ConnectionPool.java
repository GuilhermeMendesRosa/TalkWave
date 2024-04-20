package br.udesc.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.TreeSet;

public class ConnectionPool {

    private ServerSocket server;
    private TreeSet<User> users;

    public void startServer(int port) throws IOException {
        this.server = new ServerSocket(port);
        this.users = new TreeSet<User>();
        while (true) {
            Socket clientSocket = server.accept();
            System.out.println("Cliente conectado: " + clientSocket);
            var saida = new Scanner(clientSocket.getInputStream());

            while (saida.hasNextLine()) {
                System.out.println(saida.nextLine());
            }

            saida.close();
            server.close();
            clientSocket.close();
        }
    }

    public void connect(String userName) throws IOException {
    }

}

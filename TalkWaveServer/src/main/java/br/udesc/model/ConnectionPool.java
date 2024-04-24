package br.udesc.model;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionPool {

    private volatile ServerSocket server;
    private Vector<User> users;

    public void startServer(int port) throws IOException {
        this.server = new ServerSocket(port);
        this.users = new Vector<User>();
        this.acceptSockets();
    }

    private void acceptSockets() throws IOException {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        while (true) {
            Socket clientSocket = server.accept();
            threadPool.execute(() -> {
                try {
                    boolean first = true;
                    String userName = null;

                    Scanner input = new Scanner(clientSocket.getInputStream());

                    while (input.hasNextLine()) {
                        if (first) {
                            first = false;
                            userName = input.nextLine();
                            this.users.add(new User(userName, clientSocket));
                            System.out.println("Usuário conectado: " + userName + " - Socket: " + clientSocket);

                            continue;
                        }

                        String jsonMessage = input.nextLine();

                        Message message = new Gson().fromJson(jsonMessage, Message.class);
                        User recipient = this.users.stream().filter(u -> u.getName().equals(message.getRecipient())).findFirst().get();
                        PrintStream output = new PrintStream(recipient.getSocket().getOutputStream());
                        output.println(jsonMessage);
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public static void main(String[] args) throws IOException {
        ConnectionPool connectionPool = new ConnectionPool();
        connectionPool.startServer(8080);
    }

}

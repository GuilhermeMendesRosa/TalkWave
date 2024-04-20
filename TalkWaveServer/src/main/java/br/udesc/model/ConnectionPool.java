package br.udesc.model;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintStream;
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
            new Thread(() -> {

                System.out.println("Cliente conectado: " + clientSocket);
                Scanner entrada = null;
                try {
                    entrada = new Scanner(clientSocket.getInputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                String userName = entrada.nextLine();

                this.users.add(new User(userName, clientSocket));
                System.out.println(userName);
                System.out.println(this.users.size());
                while (true) {
                    if (entrada.hasNextLine()) {
                        Message message = new Gson().fromJson(entrada.nextLine(), Message.class);
                        User recipient = this.users.stream().filter(u -> u.getName().equals(message.getRecipient())).findFirst().get();
                        Socket recipientSocket = recipient.getSocket();
                        try {
                            var saida = new PrintStream(recipientSocket.getOutputStream());
                            saida.printf("Mensagem de %s: %s%n", userName, entrada.nextLine());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            }).start();
        }
    }

    public static void main(String[] args) throws IOException {
        ConnectionPool connectionPool = new ConnectionPool();
        connectionPool.startServer(8080);
    }

}

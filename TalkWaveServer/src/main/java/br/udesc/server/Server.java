package br.udesc.server;

import br.udesc.model.Message;
import br.udesc.model.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private volatile ServerSocket server;
    private Vector<User> users;

    public void startServer(int port) throws IOException {
        System.out.println("SERVIDOR INICIANDO");
        this.server = new ServerSocket(port);
        this.users = new Vector<>();
        this.acceptSockets();
    }

    private void acceptSockets() throws IOException {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        while (true) {
            Socket clientSocket = server.accept();
            threadPool.execute(() -> {
                try {
                    Scanner input = new Scanner(clientSocket.getInputStream());
                    this.registerUser(input, clientSocket);

                    while (input.hasNextLine()) {
                        this.processMessage(input);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void registerUser(Scanner input, Socket clientSocket) {
        String userName = input.nextLine();
        this.users.add(new User(userName, clientSocket));
        System.out.println("Usuário conectado: " + userName + " - Socket: " + clientSocket);
    }

    private void processMessage(Scanner input) throws IOException {
        String jsonMessage = input.nextLine();
        Message message = new Gson().fromJson(jsonMessage, Message.class);
        User recipient = this.users.stream().filter(u -> u.getName().equals(message.getRecipient())).findFirst().get();
        PrintStream output = new PrintStream(recipient.getSocket().getOutputStream());
        output.println(jsonMessage);
    }

}

package br.udesc.server;

import br.udesc.enums.Command;
import br.udesc.model.Message;
import br.udesc.model.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private volatile ServerSocket server;
    private Set<User> users;
    private ExecutorService threadPool;

    public void startServer(int port) throws IOException {
        System.out.println("SERVIDOR INICIANDO");
        this.server = new ServerSocket(port);
        this.users = Collections.synchronizedSet(new TreeSet<>());
        this.acceptConnections();
    }

    private void acceptConnections() throws IOException {
        this.threadPool = Executors.newCachedThreadPool();
        while (true) {
            Socket clientSocket = server.accept();
            threadPool.execute(() -> {
                try {
                    Scanner input = new Scanner(clientSocket.getInputStream());
                    this.registerUser(input.nextLine(), clientSocket);

                    while (input.hasNextLine()) {
                        this.processMessage(input.nextLine());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void registerUser(String userName, Socket clientSocket) {
        this.users.add(new User(userName, clientSocket));
        System.out.println("Usuário conectado: " + userName + " - Socket: " + clientSocket);
    }

    private void processMessage(String jsonMessage) throws IOException {
        Message message = new Gson().fromJson(jsonMessage, Message.class);
        switch (message.getCommand()) {
            case SEND_MESSAGE -> {
                this.sendMessage(message);
            }
            case USERS -> {
                this.listUsers(message);
            }
            case EXIT -> {
                this.closeConnection(message);
            }
        }
    }

    private void sendMessage(Message message) throws IOException {
        System.out.println(MessageFormat.format("MENSAGEM DE {0} PARA {1}", message.getSender(), message.getRecipient()));
        User recipient = this.users.stream().filter(u -> u.getName().equals(message.getRecipient())).findFirst().get();
        String jsonMessage = new Gson().toJson(message);
        PrintStream output = new PrintStream(recipient.getSocket().getOutputStream());
        output.println(jsonMessage);
    }

    private void listUsers(Message messageToProcess) throws IOException {
        User recipient = this.users.stream().filter(u -> u.getName().equals(messageToProcess.getRecipient())).findFirst().get();

        System.out.println("LISTANDO USUÁRIO PARA -> " + recipient.getName());

        List<String> list = this.users
                .stream()
                .map(User::getName)
                .toList();
        String jsonList = new Gson().toJson(list);

        Message messageToSend = new Message("", recipient.getName(), jsonList, Command.USERS);
        String jsonMessage = new Gson().toJson(messageToSend);
        PrintStream output = new PrintStream(recipient.getSocket().getOutputStream());
        output.println(jsonMessage);
    }

    private void closeConnection(Message message) throws IOException {
        User sender = this.users.stream().filter(u -> u.getName().equals(message.getSender())).findFirst().get();
        sender.getSocket().close();
        System.out.println("REMOVENDO USUÁRIO: " + sender.getName());
        this.users.remove(sender);

        this.users.forEach(user -> {
            threadPool.execute(() -> {
                try {
                    PrintStream output = new PrintStream(user.getSocket().getOutputStream());
                    String jsonMessage = new Gson().toJson(message);
                    output.println(jsonMessage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

}

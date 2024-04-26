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
        this.startAuditMode();
        this.acceptConnections();
    }

    public List<User> listUsers() {
        return this.users.stream().toList();
    }

    public void banUser(String userToBan) throws IOException {
        User user = this.findUser(userToBan);
        this.sendBanned(user);
        this.removeUser(user);
    }



    private void removeUser(User userToRemove) throws IOException {
        userToRemove.getSocket().close();
        this.users.remove(userToRemove);
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
                this.sendUsers(message);
            }
            case EXIT -> {
                this.closeConnection(message);
            }
        }
    }

    private void sendMessage(Message message) {
        List<User> recipients;

        List<String> recipientNames = message.getRecipients();
        if (recipientNames == null) {
            recipients = this.users.stream()
                    .filter(user -> !user.equals(this.findUser(message.getSender())))
                    .toList();
        } else {
            recipients = recipientNames.stream().map(this::findUser).toList();
        }

        for (User user : recipients) {
            if (user == null) {
                continue;
            }

            System.out.println(MessageFormat.format("MENSAGEM DE {0} PARA {1}", message.getSender(), user.getName()));
            this.threadPool.execute(() -> {
                try {
                    PrintStream output = new PrintStream(user.getSocket().getOutputStream());
                    String jsonMessage = new Gson().toJson(message);
                    output.println(jsonMessage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void sendUsers(Message messageToProcess) throws IOException {
        User recipient = this.findUser(messageToProcess.getRecipients().get(0));
        System.out.println("LISTANDO USUÁRIO PARA -> " + recipient.getName());

        List<String> list = this.users
                .stream()
                .map(User::getName)
                .toList();
        String jsonList = new Gson().toJson(list);

        Message messageToSend = new Message("", Collections.singletonList(recipient.getName()), jsonList, Command.USERS);
        String jsonMessage = new Gson().toJson(messageToSend);
        PrintStream output = new PrintStream(recipient.getSocket().getOutputStream());
        output.println(jsonMessage);
    }

    private void sendBanned(User userToBan) throws IOException {
        System.out.println("BANINDO USUÁRIO " + userToBan.getName());

        Message messageToSend = new Message("", Collections.singletonList(userToBan.getName()), "", Command.BANNED);
        String jsonMessage = new Gson().toJson(messageToSend);
        PrintStream output = new PrintStream(userToBan.getSocket().getOutputStream());
        output.println(jsonMessage);
    }

    private User findUser(String userName) {
        return this.users.stream()
                .filter(u -> u.getName().equals(userName))
                .findFirst()
                .orElse(null);
    }

    private void closeConnection(Message message) throws IOException {
        System.out.println("REMOVENDO USUÁRIO: " + message.getSender());

        User userToRemove = this.findUser(message.getSender());
        this.removeUser(userToRemove);

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

    private void startAuditMode() {
        new Thread(new AuditMode(this)).start();
    }
}

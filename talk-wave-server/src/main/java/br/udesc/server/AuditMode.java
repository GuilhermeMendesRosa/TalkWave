package br.udesc.server;

import br.udesc.model.User;

import java.util.List;
import java.util.Scanner;

public class AuditMode implements Runnable {

    private final Server server;
    private Scanner scanner;

    public AuditMode(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        this.scanner = new Scanner(System.in);

        while (this.scanner.hasNextLine()) {
            String textTyped = this.scanner.nextLine();
            if (!"#audit".equals(textTyped)) {
                continue;
            }

            this.showAuditOptions();
        }
    }

    private void showAuditOptions() {
        while (true) {
            System.out.println("""
                    ---------Audit Mode---------
                    1 - Auditar Mensagens
                    2 - Listar Usuários
                    3 - Banir Usuário
                    4 - Sair
                    """);
            ;

            String option = this.scanner.nextLine();

            switch (option.trim()) {
                case "1" -> {
                    this.auditMessages();
                }
                case "2" -> {
                    this.showUsers();
                }
                case "3" -> {
                    this.showBanUser();
                }
                case "4" -> {
                    return;
                }
            }
        }
    }

    private void showUsers() {
        List<User> users = this.server.listUsers();
        for (User user : users) {
            System.out.println("----------Usuários----------");
            System.out.println(user.getName());
            System.out.println("----------------------------");
        }
    }

    private void showBanUser() {
        try {
            System.out.print("Qual usuário deseja banir? ");
            String userToBan = this.scanner.nextLine();
            this.server.banUser(userToBan);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void auditMessages() {
        this.server.listChats();
        String chat = this.scanner.nextLine();

        String[] users = chat.split("-", 2);
        String user1 = users[0].trim();
        String user2 = users[1].trim();

        this.server.auditChat(user1, user2);
    }

}

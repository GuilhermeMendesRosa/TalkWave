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
                    """);
            ;

            String option = this.scanner.nextLine();

            switch (option.trim()) {
                case "1" -> {

                }
                case "2" -> {
                    this.showUsers();
                }
                case "3" -> {
                    this.showBanUser();
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

}

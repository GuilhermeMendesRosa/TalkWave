package br.udesc.server;

import br.udesc.model.User;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ClientActiviyCheck {

    private final Server server;
    private final ArrayList<User> warnedUserList = new ArrayList<>();

    public ClientActiviyCheck(Server server) {
        this.server = server;
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    long minutesCheckDelay = 10;
                    Thread.sleep(TimeUnit.MINUTES.toMillis(minutesCheckDelay));
                    checkActivity();
                } catch (InterruptedException e) {
                    System.out.println("Erro ao verificar atividade dos usuários");
                }
            }
       }).start();
    }

    public void checkActivity() {
        server.listUsers().stream().filter(this::shouldBeWarned).forEach(user -> {
            server.sendAdminMessage(user, "Olá, você ainda está aí? Envie uma mensagem para manter-se conectado.");
            warnedUserList.add(user);
        });

        warnedUserList.stream().filter(this::shouldBeBanned).forEach(user -> {
            try {
                server.banUser(user.getName());
            } catch (Exception e) {
                System.out.println("Erro ao banir usuário: " + e.getMessage());
            }
        });
    }

    private Boolean shouldBeWarned(User user) {
        if (warnedUserList.contains(user)) return false;

        return inactiveHours(user) > 1;
    }

    private Boolean shouldBeBanned(User user) {
        return inactiveHours(user) > 5;
    }

    private long inactiveHours(User user) {
        long now = System.currentTimeMillis();
        long inactiveTime = now - user.getActiveAt();
        return TimeUnit.MILLISECONDS.toHours(inactiveTime);
    }
}

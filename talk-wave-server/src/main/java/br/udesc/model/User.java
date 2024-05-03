package br.udesc.model;

import java.net.Socket;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class User implements Comparable<User>{

    private String name;
    private Socket socket;
    private Long activeAt;

    public User(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Long getActiveAt() {
        return activeAt;
    }

    public void setActiveAt(Long activeAt) {
        this.activeAt = activeAt;
    }

    public String getStatusDescription() {
        long now = System.currentTimeMillis();
        long inactiveTime = now - this.activeAt;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(inactiveTime);

        if (minutes < 2) return "Online";
        if (minutes < 60) return "Última atividade há " + minutes + " minutos";

        Date activeDateText = new Date(this.activeAt);
        return "Última atividade em " + activeDateText;
    }
    @Override
    public int compareTo(User user) {
        return this.name.compareTo(user.getName());
    }
}

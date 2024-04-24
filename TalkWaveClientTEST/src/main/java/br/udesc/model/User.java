package br.udesc.model;

import java.net.Socket;

public class User implements Comparable<User>{

    private String name;
    private Socket socket;

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

    @Override
    public int compareTo(User user) {
        return this.name.compareTo(user.getName());
    }
}

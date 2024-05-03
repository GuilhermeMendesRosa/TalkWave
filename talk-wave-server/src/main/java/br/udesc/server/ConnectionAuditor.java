package br.udesc.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class ConnectionAuditor {

    private static final String FILE_NAME = "connections.csv";
    private static final String SEPARATOR = ";";

    public ConnectionAuditor() {
        this.createFile();
    }

    private void createFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                boolean success = file.createNewFile();
                if (!success) {
                    System.out.println("Erro ao criar arquivo de logs. Verifique as permissões.");
                    return;
                }
                write("ip", "user", "timestamp");
            } catch (IOException e) {
                System.out.println("Erro ao criar arquivo de logs: " + e.getMessage());
            }
        }
    }

    public void audit(String ip, String userId) {
        Long timestamp = new Date().getTime();
        write(ip, userId, timestamp.toString());
    }

    private void write(String ip, String userId, String timestamp) {
        try {
            FileWriter fw = new FileWriter(FILE_NAME, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(ip)
                    .append(SEPARATOR)
                    .append(timestamp.toString())
                    .append(SEPARATOR)
                    .append(userId)
                    .append('\n');
            bw.close();
        } catch (Exception exception) {
            System.out.println("Erro ao escrever no arquivo de logs: " + exception.getMessage());
        }
    }
}

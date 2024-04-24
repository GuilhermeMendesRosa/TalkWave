package br.udesc;

import br.udesc.model.Message;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Receive {
    public static void main(String[] args) throws IOException {
        Socket cliente = new Socket("127.0.0.1", 8080);
        PrintStream saida = new PrintStream(cliente.getOutputStream());
        Scanner teclado = new Scanner(System.in);

        String username = teclado.nextLine();
        saida.println(username);

        new Thread(() -> {
            while (true) {
                String recipient = teclado.nextLine();
                String content = teclado.nextLine();
                Message message = new Message(username, recipient, content);
                String json = new Gson().toJson(message);
                saida.println(json);
            }
        }).start();

        new Thread(() -> {
            try {
                Scanner input = new Scanner(cliente.getInputStream());
                while (true) {
                    if (!input.hasNextLine()) {
                        continue;
                    }

                    String str = input.nextLine();
                    System.out.println(str);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();


//            saida.close();
//            teclado.close();
//            cliente.close();
    }
}

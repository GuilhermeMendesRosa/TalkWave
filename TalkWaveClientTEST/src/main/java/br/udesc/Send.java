package br.udesc;


import br.udesc.model.Message;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Send {
    public static void main(String[] args) throws IOException {
        Scanner teclado = new Scanner(System.in);
        System.out.print("Digite o seu nome de usuário: ");
        String userName = teclado.nextLine();

        var cliente = new Socket("127.0.0.1", 8080);
        var saida = new PrintStream(cliente.getOutputStream());
        saida.println(userName);

        while (true) {
            System.out.print("Destinatário: ");
            String recipient = teclado.nextLine();

            System.out.print("Conteúdo: ");
            String content = teclado.nextLine();
            Message message = new Message(userName, recipient, content);
            saida.println(new Gson().toJson(message));
        }

//            saida.close();
//            teclado.close();
//            cliente.close();
    }
}

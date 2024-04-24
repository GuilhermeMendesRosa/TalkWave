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

        while (true) {
            Scanner teclado = new Scanner(System.in);
            String str = teclado.nextLine();
            saida.println(str);
        }

//            saida.close();
//            teclado.close();
//            cliente.close();
    }
}

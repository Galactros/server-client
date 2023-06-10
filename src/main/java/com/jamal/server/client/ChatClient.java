package com.jamal.server.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;

            // Thread para receber mensagens do servidor
            Thread serverThread = new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println("Servidor: " + serverResponse);
                    }
                } catch (IOException e) {
                    System.out.println("Conexão com o servidor perdida.");
                    System.exit(0);
                }
            });
            serverThread.start();

            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);

                if (userInput.equalsIgnoreCase("SAIR")) {
                    break;
                }
            }

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Erro ao conectar-se ao servidor.");
            e.printStackTrace();
        }
    }
}

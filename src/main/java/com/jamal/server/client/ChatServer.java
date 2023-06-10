package com.jamal.server.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Servidor iniciado. Aguardando conexão do cliente...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado.");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Cliente: " + inputLine);
                out.println("Servidor: " + inputLine);

                if (inputLine.equalsIgnoreCase("SAIR")) {
                    break;
                }
            }

            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
            System.out.println("Conexão encerrada.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

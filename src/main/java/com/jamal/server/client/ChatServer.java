package com.jamal.server.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static List<PrintWriter> clientWriters = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Servidor iniciado. Aguardando conexões de clientes...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado.");

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.add(out);

                ClientHandler clientHandler = new ClientHandler(clientSocket, out);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter clientWriter;

        public ClientHandler(Socket clientSocket, PrintWriter clientWriter) {
            this.clientSocket = clientSocket;
            this.clientWriter = clientWriter;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Cliente: " + inputLine);

                    if (inputLine.equalsIgnoreCase("SAIR")) {
                        break;
                    }

                    broadcast("Cliente: " + inputLine);
                }

                in.close();
                clientSocket.close();
                clientWriter.close();
                clientWriters.remove(clientWriter);
                System.out.println("Cliente desconectado.");
            } catch (IOException e) {
                // IOException ocorre quando há uma desconexão inesperada do cliente
                System.out.println("Erro na conexão com o cliente. Cliente desconectado.");
                clientWriters.remove(clientWriter);
            }
        }
    }

    private static void broadcast(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println("Servidor: " + message);
        }
    }
}

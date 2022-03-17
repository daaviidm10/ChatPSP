package Servidor;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MainServidor {
    static ServerSocket serverSocket;
    static InputStream entrada;
    static OutputStream salida;

    static int i =0;
    public static void main(String[] args) {

        try {

            serverSocket = new ServerSocket();
            String porto = JOptionPane.showInputDialog("Introduce o Porto ao que che vas a querer conectar");
            InetSocketAddress direccións = new InetSocketAddress("localhost", Integer.parseInt(porto));
            System.out.println("O porto que estás a usar é o "+porto);
            serverSocket.bind(direccións);
            System.out.println("Neste intre non hai ningún cliente conectado");
            while (true) {
                Socket newSocket = serverSocket.accept();
                entrada = newSocket.getInputStream();
                salida = newSocket.getOutputStream();
                salida.write("Conectado á sala do chat".getBytes());
                byte[] nick = new byte[100];
                entrada.read(nick);
                int usuarios=Servidor.usuarios.size()+1;
                if (usuarios>2){ //si el numero permitido de clientes es superado envía un mensaje al cliente sair para que se cierre.
                    System.out.println("Non poden conectarse máis usuarios");
                    salida.write("sair".getBytes());
                }else {
                    System.out.println("Novo cliente conectado : "+new String(nick).trim()
                            +"\nAgora mesmo hai "+usuarios+" usuarios conectados"
                    );
                    Servidor servidor = new Servidor(new String(nick).trim(), newSocket);
                    servidor.start();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
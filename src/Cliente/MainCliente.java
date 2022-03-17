package Cliente;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MainCliente {
    public static String nome;
    public static String IP;
    public static String Porto;

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in); //Se crea el lector
        // Creando socket cliente
        IP = JOptionPane.showInputDialog("Introduce a IP");
        Porto = JOptionPane.showInputDialog("Introduce o Porto");
        nome = JOptionPane.showInputDialog("Introduce o teu nome");

        Socket cliente = new Socket();
        InetSocketAddress addr = new InetSocketAddress(IP, Integer.parseInt(Porto));
        try{
            cliente.connect(addr);
            InputStream lerDatos = cliente.getInputStream();
            OutputStream escribirDatos = cliente.getOutputStream();
            escribirDatos.write(nome.getBytes());
            byte [] conectado = new byte[100];
            lerDatos.read(conectado);
            System.out.println(new String(conectado).trim());

            Interfaz chat = new Interfaz(escribirDatos);
            chat.setVisible(true);
            chat.setLocationRelativeTo(null);
            chat.ip.setText(IP);
            chat.porto.setText(Porto);
            chat.nick.setText(nome);
            chat.CampotextoChat.setText(new String(conectado).trim()+"\n");

            HiloLectura lectura = new HiloLectura(lerDatos, chat, cliente);
            lectura.start();

        }catch (UnknownHostException e){
                System.out.println("Dirección IP non correcta");

            }catch (ConnectException f){
            System.out.println("Porto incorrecto ou servidor desconectado");

        }
    }
}
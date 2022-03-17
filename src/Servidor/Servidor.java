package Servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor extends Thread {

    Socket Socket;
    InputStream lectura;
    OutputStream escritura;
    static ArrayList<Servidor> usuarios = new ArrayList();
    static String [] comando= new String[2];

    public Servidor(String name, Socket socket) throws IOException {

        super(name);
        this.Socket = socket;
        lectura = socket.getInputStream();
        escritura = socket.getOutputStream();

    }

    void enviarMensaxe(String mensaje) throws IOException {
        escritura.write(mensaje.getBytes());

    }

    @Override
    public void run() {

        String[] comando;
        try {

            for (int i = 0; i < usuarios.size(); i++) {
                usuarios.get(i).enviarMensaxe(getName() + " acaba de conectarse a este chat");
            }
            usuarios.add(this);
            String mensaxe = "";
            while (usuarios.size() != 0) {
                byte[] mensaxeRecibida = new byte[140];
                lectura.read(mensaxeRecibida);
                mensaxe = new String(mensaxeRecibida).trim();
                System.out.println(mensaxe);
                comando = mensaxe.split(": ");

                if (comando[1].equals("sair")) {
                    System.out.println(usuarios.size());
                    for (int i = 0; i < usuarios.size(); i++) {
                        String desconexion = comando[0] + " deixou este chat";
                        usuarios.get(i).enviarMensaxe(desconexion);
                    }
                    this.Socket.close();
                    usuarios.remove(this);
                } else {
                    for (int i = 0; i < usuarios.size(); i++) {
                        usuarios.get(i).enviarMensaxe(mensaxe);
                    }
                }
            }

        } catch (IOException e) {
            try {
                this.Socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            usuarios.remove(this);
            if (usuarios.size()==0){
                System.out.println("Ningún cliente conectado");
            }else {

                for (int i = 0; i < usuarios.size(); i++) {
                    String desconexion = this.getName() + " deixou este chat";
                    try {
                        usuarios.get(i).enviarMensaxe(desconexion);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                }
            }
            System.out.println("socket cerrado\n");
        }
    }
}
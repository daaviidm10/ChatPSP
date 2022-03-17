package Cliente;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class HiloLectura extends Thread{
    final InputStream entrada;
    private final Interfaz chat;
    private final Socket cliente;

    public HiloLectura(InputStream entrada, Interfaz chat, Socket cliente) {
        this.entrada= entrada;
        this.chat = chat;
        this.cliente =cliente;
    }

    public void run(){
        try {
            while (true){
                byte [] mensaje = new byte[140];
                entrada.read(mensaje);
                chat.CampotextoChat.append(new String(mensaje).trim() + "\n");
                if(new String(mensaje).trim().equals("sair")){  // si el menaje que lee el cliente procedente del servidor es "sair" se cierra
                    cliente.close();
                    System.exit(0);
                    System.out.println("O servidor pechouse");
                }
            }

        } catch (IOException ioException) {chat.CampotextoChat.append("O servidor desconectouse");
            try {
                sleep(3000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            System.exit(0);
        }
    }
}
package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Servidor {
	private static int puerto;
	private static ServerSocket servidor;
	private static int maximoConexiones;
	private static MensajesChat sala1;
	private static MensajesChat sala2;
	private static MensajesChat sala3;
	private static Socket socket;
	private static Logger log;

	public static void main(String[] args) throws FileNotFoundException {
		Scanner scan = new Scanner(new File("configServidor.config"));
		puerto = scan.nextInt();
		maximoConexiones = scan.nextInt();
		PropertyConfigurator.configure("log4j.properties");
		log = Logger.getLogger(Servidor.class);

		try {
			servidor = new ServerSocket(puerto, maximoConexiones);
			sala1 = new MensajesChat();
			sala2 = new MensajesChat();
			sala3 = new MensajesChat();

			log.info("Se crea el servidor Puerto: " + puerto + ", Maximo de conexiones: " + maximoConexiones);

			while (true) {
				log.info("Servidor a la espera de conexiones...");

				socket = servidor.accept();
				log.info("Cliente con la IP " + socket.getInetAddress().getHostName() + " conectado.");

				ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
				log.info("Esperando la eleccion del numero de sala..");
				String nroSala = entrada.readUTF();
				AtencionCliente atCliente = null;

				if ("1".equals(nroSala))
					atCliente = new AtencionCliente(socket, sala1);

				if ("2".equals(nroSala))
					atCliente = new AtencionCliente(socket, sala2);

				if ("3".equals(nroSala))
					atCliente = new AtencionCliente(socket, sala3);

				atCliente.start();
			}
		} catch (IOException e) {
			log.info("Error: " + e.getMessage());
		}

		finally {
			try {
				socket.close();
				servidor.close();
				scan.close();
			} catch (IOException e) {
				log.info("Error: " + e.getMessage());
			}
		}
	}
}

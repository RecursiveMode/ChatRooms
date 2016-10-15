package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Servidor {
	private static int puerto;
	private static ServerSocket servidor;
	private static int maximoConexiones;
	private static MensajesChat mensajes;
	private static Socket socket;
	private static Logger log;

	public static void main(String[] args) throws FileNotFoundException {
		puerto = 50000;
		maximoConexiones = 10;
		PropertyConfigurator.configure("log4j.properties");
		log = Logger.getLogger(Servidor.class);

		try {
			servidor = new ServerSocket(puerto, maximoConexiones);
			mensajes = new MensajesChat();

			log.info("Se crea el servidor Puerto: " + puerto + ", Maximo de conexiones: " + maximoConexiones);

			while (true) {
				log.info("Servidor a la espera de conexiones...");

				socket = servidor.accept();
				log.info("Cliente con la IP " + socket.getInetAddress().getHostName() + " conectado.");

				AtencionCliente atCliente = new AtencionCliente(socket, mensajes);
				atCliente.start();
			}
		}

		catch (IOException e) {
			log.info("Error: " + e.getMessage());
		}

		finally {
			try {
				socket.close();
				servidor.close();
			}

			catch (IOException e) {
				log.info("Error: " + e.getMessage());
			}
		}
	}
}

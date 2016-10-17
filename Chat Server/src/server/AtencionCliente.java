package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

public class AtencionCliente extends Thread implements Observer {
	private Logger log;
	private Socket socket;
	private ObjectInputStream entrada;
	private ObjectOutputStream salida;
	private MensajesChat mensajes;

	public AtencionCliente(Socket socket, MensajesChat mensajes) {
		this.socket = socket;
		this.mensajes = mensajes;
		log = Logger.getLogger(AtencionCliente.class);

		try {
			entrada = new ObjectInputStream(socket.getInputStream());
			salida = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			log.info("Error: " + e.getMessage());
		}
	}

	@Override
	public void run() {
		String mensajeRecibido;
		boolean conectado = true;
		mensajes.addObserver(this);

		while (conectado) {
			try {
				mensajeRecibido = entrada.readUTF();
				mensajes.setMensaje(mensajeRecibido);
			} catch (IOException e) {
				log.info("Cliente con la IP " + socket.getInetAddress().getHostName() + " desconectado.");
				conectado = false;

				try {
					entrada.close();
					salida.close();
				} catch (IOException ex) {
					log.error("Error: " + ex.getMessage());
				}
			}
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		try {
			salida.writeUTF(arg0.toString());
		} catch (IOException e) {
			log.error("Error al enviar mensaje al cliente" + e.getMessage());
		}
	}
}

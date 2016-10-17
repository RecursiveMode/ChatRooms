package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Cliente {
	private Logger log;
	private Socket socket;
	private int puerto;
	private String host;
	private int nroSala;
	private ObjectInputStream entrada;
	private String usuario;
	
	public Cliente(String usuario) throws FileNotFoundException {
		PropertyConfigurator.configure("log4j.properties");
		log = Logger.getLogger(Cliente.class);
		Scanner scn = new Scanner(new File("configConfig.config"));
		this.host = scn.nextLine();
		this.puerto = scn.nextInt();
		this.usuario = usuario;
		
		try {
			log.info("Intentando conectar a " + host + " en el puerto " + puerto + " con el nombre de ususario: " + usuario + ".");
			socket = new Socket(host, puerto);
			entrada = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			log.info("Error: " + e.getMessage());
		}
		
		scn.close();
	}
	
	public static void main(String[] args) {
		
	}
}

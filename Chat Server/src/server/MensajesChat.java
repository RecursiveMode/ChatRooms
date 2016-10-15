package server;

import java.util.Observable;

public class MensajesChat extends Observable {
	private String mensaje;

	public String getMensaje() {
		return this.mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;

		this.setChanged();
		this.notifyObservers(mensaje);
	}
}

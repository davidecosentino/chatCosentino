package pClientServer;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Messaggio implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2286641686222075360L;
	private String testo;
	private String mittente;
	private Date data;
	
	public Messaggio(String testo, String mittente) {
		this.testo = testo;
		this.mittente = mittente;
		data=new Date();
	}
	
	String getMittente(){
		return mittente;
	}
	
	void setMittente(String mittente){
		this.mittente=mittente;
	}
	
	@Override
	public String toString() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return "<"+dateFormat.format(data)+"> "+mittente+": \""+testo+"\"";
	}
	
}

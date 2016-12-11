package pClientServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
	
	private String nomeUtente;
	private ArrayList<Messaggio>messaggi;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public Client(String ipServer,String nomeUtente) {
		this.nomeUtente=nomeUtente;
		messaggi=new ArrayList<>();
		try {
			socket=new Socket(ipServer,65535);
			out=new ObjectOutputStream(socket.getOutputStream());
			in=new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void invioMessaggio(String testo){
		try {
			out.writeObject(new Messaggio(testo,nomeUtente));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	ArrayList<Messaggio> getMessaggi(){
		Object o;
		ArrayList<Messaggio>nuoviMessaggi=new ArrayList<>();
		try {
			out.writeObject(messaggi.size());
			do{
				o=in.readObject();
				if(!(o instanceof EOS)){
					Messaggio messaggio=(Messaggio)o;
					if(messaggio.getMittente().equals(nomeUtente))
						messaggio.setMittente("io");
					messaggi.add(messaggio);
					nuoviMessaggi.add(messaggio);
				}
			}while(!(o instanceof EOS));
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return nuoviMessaggi;
	}
	
	boolean isConnected(){
		return socket.isConnected();
	}

}

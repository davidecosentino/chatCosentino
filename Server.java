package pClientServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server implements Runnable{
	
	private ArrayList<Socket>data_sockets;
	private ArrayList<Messaggio>messaggi;
	private ServerSocket socket;
	
	public Server() {
		data_sockets=new ArrayList<>();
		messaggi=new ArrayList<>();
		try {
			socket=new ServerSocket(65535);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void attivaServer(){
		while(true){
			try {
				data_sockets.add(socket.accept());
			} catch (IOException e) {
				e.printStackTrace();
			}
			new Thread(this).start();
		}
	}
	
	private void aggiungiMessaggio(ObjectInputStream in,Messaggio messaggio) throws ClassNotFoundException, IOException{
		messaggi.add(messaggio);
	}
	
	private void inviaMessaggi(ObjectInputStream in,ObjectOutputStream out,int nMessaggiClient) throws ClassNotFoundException, IOException{
		for(int i=nMessaggiClient;i<messaggi.size();i++)
			out.writeObject(messaggi.get(i));
		out.writeObject(new EOS());
	}
	
	private void scambioDati(Socket data_socket){
		ObjectInputStream in;
		ObjectOutputStream out;
		Object o;
		try {
			in=new ObjectInputStream(data_socket.getInputStream());
			out=new ObjectOutputStream(data_socket.getOutputStream());
			do{
				o=in.readObject();
				if(o instanceof Messaggio)
					aggiungiMessaggio(in,(Messaggio)o);
				else
					inviaMessaggi(in, out,(Integer)o);
			}while(true);
		} catch (SocketException e) {
			data_sockets.remove(data_socket);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		scambioDati(data_sockets.get(data_sockets.size()-1));
	}

}

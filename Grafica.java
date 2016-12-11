package pClientServer;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Grafica {

	private JFrame frmChatcosentino;
	private JTextField txtInserisciMessaggio;
	private JTextArea textArea;
	private Client client;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		String ip=JOptionPane.showInputDialog("Inserire ip del server");
		String nomeUtente=JOptionPane.showInputDialog("Inserire nome utente");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Grafica window = new Grafica(ip,nomeUtente);
					window.frmChatcosentino.setVisible(true);
					window.stampaMessaggi();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Grafica(String ip,String nomeUtente) {
		initialize(ip,nomeUtente);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String ip,String nomeUtente) {
		client=new Client(ip, nomeUtente);
		
		frmChatcosentino = new JFrame();
		frmChatcosentino.setTitle("chatCosentino");
		frmChatcosentino.setBounds(100, 100, 591, 638);
		frmChatcosentino.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatcosentino.getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		JScrollPane scroll=new JScrollPane(textArea);
		scroll.setBounds(10, 22, 555, 535);
		frmChatcosentino.getContentPane().add(scroll);
		
		txtInserisciMessaggio = new JTextField();
		txtInserisciMessaggio.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER)
					client.invioMessaggio(txtInserisciMessaggio.getText());
			}
		});
		txtInserisciMessaggio.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtInserisciMessaggio.setText("");
			}
		});
		txtInserisciMessaggio.setText("Inserisci Messaggio");
		txtInserisciMessaggio.setBounds(10, 568, 456, 20);
		frmChatcosentino.getContentPane().add(txtInserisciMessaggio);
		txtInserisciMessaggio.setColumns(10);
		
		JButton btnInvia = new JButton("Invia");
		btnInvia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String messaggio=txtInserisciMessaggio.getText();
				if(!messaggio.equals(""))
					client.invioMessaggio(txtInserisciMessaggio.getText());
			}
		});
		btnInvia.setBounds(476, 567, 89, 23);
		frmChatcosentino.getContentPane().add(btnInvia);	
	}
	
	void stampaMessaggi(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					ArrayList<Messaggio>messaggi=client.getMessaggi();
					for(Messaggio messaggio : messaggi)
						textArea.append(messaggio.toString()+"\n");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}

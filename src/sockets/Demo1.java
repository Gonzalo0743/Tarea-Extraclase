package sockets;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;


public class Demo1 {
	
	
/**
 * La clase principal llama la funcion para crear el marco
 * @param args
 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoChat mimarco=new MarcoChat();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}


class MarcoChat extends JFrame{
	
	/**
	 * En esta clase se crea el marco y llama a la Lamina
	 */
	
	public MarcoChat(){
		
		setBounds(600,300,280,350);
				
		LaminaMarcoChat milamina=new LaminaMarcoChat();
		
		add(milamina);
		
		setVisible(true);
		}	
	
}

class LaminaMarcoChat extends JPanel implements Runnable{
	
	/**
	 * Aqui se crea la todas las entradas de la ventana
	 */
	
	public LaminaMarcoChat(){
		
		nick=new JTextField(5);
		
		add(nick);
	
		JLabel texto=new JLabel("-CHAT-");
		
		add(texto);
		
		ip = new JTextField(8);
		
		add(ip);
		
		campochat = new JTextArea(12,20);
	
		add(campochat);		
				
		campo1=new JTextField(20);
	
		add(campo1);		
	
		miboton=new JButton("Enviar");
		
		EnviaTexto eventoenviar=new EnviaTexto();
		
		miboton.addActionListener(eventoenviar);
		
		add(miboton);
		
		Thread hilo=new Thread(this);
		hilo.start();
		
	}
	
	
	private class EnviaTexto implements ActionListener{
		
		/**
		 * En este método se extraen los datos de la entrada y se crea el socket con su ip y el puerto
		 */

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
			try {
				Socket chatsocket = new Socket("127.0.0.1",9999);
			
				
				PackEnvio datos = new PackEnvio();
				
				datos.setNick(nick.getText());
				
				datos.setIp(ip.getText());
				
				datos.setMensaje(campo1.getText());
				
				ObjectOutputStream pack_datos = new ObjectOutputStream(chatsocket.getOutputStream());
				
				pack_datos.writeObject(datos);
				
				chatsocket.close();
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			
		}

		
		
		
		
	}
	
		
		
	/**
	 * Aqui se definden las entradas, el espacio del chat y el boton	
	 */
	private JTextField campo1, nick, ip;
	
	private JTextArea campochat;
	
	private JButton miboton;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		/**
		 * Esta parte recibe los datos del server y los introduce en el campo de texto
		 */
		
		try{
			
			ServerSocket servidor_cliente = new ServerSocket(9090);
			
			Socket cliente;
			
			PackEnvio packRecibido;
			
			while(true){
				
				cliente = servidor_cliente.accept();
				
				ObjectInputStream flujoentrada = new ObjectInputStream(cliente.getInputStream());
				
				packRecibido = (PackEnvio) flujoentrada.readObject();
				
				campochat.append("\n" + packRecibido.getNick() + ": " + packRecibido.getMensaje());
			}
			
			
	}
		
		catch(Exception e){
			
			System.out.println(e.getMessage());
			
		}
			
			
			
		}
	
}

class PackEnvio implements Serializable{
	
	/**
	 * Aqui se serializan los datos para lograr ser traspasados al server
	 */
	
	private String nick, ip, mensaje;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	
	
	
	
	
	
}


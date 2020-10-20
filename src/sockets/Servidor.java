package sockets;

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.net.*;

public class Servidor  {
	
	/**
	 * En este método, se accede a la clase MarcoServidor
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoServidor mimarco=new MarcoServidor();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

class MarcoServidor extends JFrame implements Runnable{
	
	/**
	 * Creación de la ventana del servidor
	 */
	
	public MarcoServidor(){
		
		setBounds(1200,300,280,350);				
			
		JPanel milamina= new JPanel();
		
		milamina.setLayout(new BorderLayout());
		
		areatexto=new JTextArea();
		
		milamina.add(areatexto,BorderLayout.CENTER);
		
		add(milamina);
		
		setVisible(true);
		
		Thread hilo = new Thread(this);
		
		hilo.start();
		
		}
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		/**
		 * Aqui se reciben los datos, se extraen y se copian en el servidor
		 */
		
		//Trata de recibir los datos para copiarlos en el servidor y enviarlos, sino, apunta el error en un archivo .txt
		try {
			ServerSocket servidor = new ServerSocket(9999);
			
			String nick, ip, mensaje;
			
			PackEnvio pack_recibido;
			
			while(true){
			
				Socket socket = servidor.accept();
			
				ObjectInputStream pack_datos = new ObjectInputStream(socket.getInputStream());
				
				pack_recibido = (PackEnvio) pack_datos.readObject();
				
				nick = pack_recibido.getNick();
				
				ip = pack_recibido.getIp();
				
				mensaje = pack_recibido.getMensaje();
				
				areatexto.append("\n"+ nick + ": " + mensaje);
				
				Socket enviaDestinatario = new Socket(ip,9090);
				
				ObjectOutputStream pack_reenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
				
				pack_reenvio.writeObject(pack_recibido);
			
				socket.close();
				
			}
			
		//Caso para los errores "IOException, ClassNotFoundException"
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			
			try {
				
				FileWriter fw = new FileWriter("Errores.txt",true);
				fw.append(e.getMessage());
				fw.close();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
	}
	
	private	JTextArea areatexto;
}
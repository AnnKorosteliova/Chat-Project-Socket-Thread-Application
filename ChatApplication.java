package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import static lib.MessageIO.*;

public class ChatApplication {

	static ArrayList<Socket> clients = new ArrayList<>();
	
	public static void main(String[] args) throws IOException, InterruptedException {

		print("SERVER STARTING");
		
		//THREAD SOCKET HANDLING
		new Thread(new Runnable(){

			@Override
			public void run() {
				ServerSocket ss;
				try {
					ss = new ServerSocket(8080);
					
					boolean loop = true;
					do {
						Socket cls;
						try {
							cls = ss.accept(); //blocking
							clients.add(cls);
							print(">> client entered");
							print(">> TOTAL CLIENTS: " + clients.size());	
	//						clients.forEach(client -> System.out.println("[ " + client + " ]"));
						} catch (IOException e) {
							e.printStackTrace();
						} 
					} while(loop); 
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		}).start(); 
		
		//MESSAGES HANDLING - thread of MAIN 
		boolean loop = true;
		do {
//			clients.forEach(client -> {
//			for(Socket client: clients) {
			for(int i = 0; i < clients.size(); i++) {
				Socket client = clients.get(i);
				//open io stream for the CURRENT CLIENT!!!
				try {
					
					DataInputStream dis = new DataInputStream(client.getInputStream());
					
					String message = dis.readUTF();
					print(">>>>>>>>> a client sends: " + message);
					print(">>>>>>>>> broadcasting: " + message);
					
//					clients.forEach(bClient -> {
//					for(Socket bClient: clients) {
					for(int ii = 0; ii < clients.size(); ii++) {
						Socket bClient = clients.get(ii);
						if(!bClient.equals(client)) {
							try {
								DataOutputStream dous = new DataOutputStream(bClient.getOutputStream());
								dous.writeUTF(message);
							} catch (IOException e) {
								e.printStackTrace();
							}
							
						}
					}//);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}//); 
			Thread.sleep(200);
		} while(loop);
		
		print("SERVER ENDING");
		
	}

}

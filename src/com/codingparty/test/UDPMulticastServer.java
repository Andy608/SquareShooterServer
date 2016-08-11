package com.codingparty.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPMulticastServer extends UDPServer {

	public UDPMulticastServer(String ip, int port) throws IOException {
		super("UPDMulticastServerThread", ip, port);
	}
	
	@Override
	public void run() {
		boolean isRunning = true;
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		while (isRunning) {
			try {
				//The length of the buffer will be in a wrapper class designed for the game.
				byte[] buffer = new byte[256];
				
				System.out.println("ERM");
				
				//Receive the request (Not necessary to get a request before sending a packet)
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				
//				System.out.println("Received packet: " + new String(packet.getData(), 0, packet.getLength()));
				
				for (int i = 1; i <= 20; i++) {
					//Figure out the response
					String string = "Packet #" + i;
					buffer = string.getBytes();
					
					//Send the response to the client
					InetAddress group = InetAddress.getByName("239.255.1.255");
					packet = new DatagramPacket(buffer, buffer.length, group, 609);
					socket.send(packet);
				}
				
				System.out.println("Continue?");
				if (!in.nextLine().isEmpty()) {
					isRunning = false;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		socket.close();
		System.out.println("Goodbye!");
	}
	
	public int getPortNumber() {
		return portNumber;
	}
	
	public String getServerIP() {
		return hostIP;
	}
}

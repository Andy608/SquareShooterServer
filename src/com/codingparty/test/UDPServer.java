package com.codingparty.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPServer extends Thread {

	protected String hostIP;
	protected int portNumber;
	protected DatagramSocket socket;
	
	public UDPServer(String threadName, String ip, int port) throws IOException {
		super(threadName);
		hostIP = ip;
		portNumber = port;
		socket = new DatagramSocket(608);
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
				
				//Receive the request
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				
				//Figure out the response
				String string = "Hello from the Server! IP: " + hostIP;
				buffer = string.getBytes();
				
				//Send the response to the client
				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				packet = new DatagramPacket(buffer, buffer.length, address, port);
				socket.send(packet);
				
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

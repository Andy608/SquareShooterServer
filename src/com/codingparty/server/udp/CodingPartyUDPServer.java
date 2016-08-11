package com.codingparty.server.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.codingparty.logger.LoggerUtil;
import com.codingparty.packet.udp.EnumUDPPacketType;
import com.codingparty.packet.udp.UDPIncomingPacketManager;
import com.codingparty.server.AbstractServer;

public class CodingPartyUDPServer extends AbstractServer {

	protected DatagramSocket socket;
	protected volatile boolean isRunning;
	
	public CodingPartyUDPServer(String threadName, String ip, int port) throws SocketException {
		super(threadName, ip, port);
		socket = new DatagramSocket(port);
		socket.setReuseAddress(true);
		isRunning = false;
	}
	
	@Override
	public void run() {
		
		while (isRunning) {
			try {
				byte[] buffer = new byte[EnumUDPPacketType.MAX_BYTE_ALLOCATION];
				DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
				socket.receive(incomingPacket);
				UDPIncomingPacketManager.retreivePacket(incomingPacket.getData());
				
			} catch (IOException e) {
				LoggerUtil.logError(getClass(), e);
			}
		}
	}
	
	public synchronized void startRunning() {
		isRunning = true;
		this.start();
	}
	
	public synchronized boolean isRunning() {
		return isRunning;
	}
	
	public synchronized void stopRunning() {
		isRunning = false;
	}
}

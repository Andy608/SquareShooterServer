package com.codingparty.server.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import com.codingparty.logger.LoggerUtil;
import com.codingparty.packet.udp.AbstractUDPServerPacket;

public class CodingPartyUDPMulticastServer extends CodingPartyUDPServer {

	private volatile boolean broadcast;
	private ArrayList<AbstractUDPServerPacket> outgoingPackets;
	private int multicastPort;
	//TODO: Get from database.
	
	public CodingPartyUDPMulticastServer(String threadName, String ip, int port, int mPort) throws SocketException {
		super(threadName, ip, port);
		multicastPort = mPort;
		outgoingPackets = new ArrayList<AbstractUDPServerPacket>();
	}
	
	@Override
	public void run() {
		
		while (isRunning) {
			
			try {
				if (broadcast) {
					sendBroadcast(false);
					
					synchronized(this) {
//						AbstractUDPServerPacket[] packets = UDPOutgoingPacketManager.getOutgoingPackets();
						
						for (int i = 0; i < outgoingPackets.size(); i++) {
							AbstractUDPServerPacket udpPacket = outgoingPackets.get(i);
							
							if (outgoingPackets.get(i) == null) {
								System.out.println("WHY!!!!!!");
								continue;
							}
							
							byte[] buffer = new byte[udpPacket.getByteLength()];
							InetAddress group = InetAddress.getByName(IP_ADDRESS);
							DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, multicastPort);
							packet.setData(udpPacket.getPacketData());
							socket.send(packet);
							outgoingPackets.remove(udpPacket);
							i--;
							udpPacket.setPacketSent();
						}
					}
				}
			} catch (IOException e) {
				LoggerUtil.logError(getClass(), e);
			}
		}
	}
	
	public synchronized void sendPackets(ArrayList<AbstractUDPServerPacket> packets) {
		for (int i = 0; i < packets.size(); i++) {
			outgoingPackets.add(packets.get(i));
			packets.remove(packets.get(i));
			i--;
		}
		broadcast = true;
	}
	
	public synchronized void sendBroadcast(boolean b) {
		broadcast = b;
	}
}

package com.codingparty.server.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.codingparty.connection.ConnectedUser;
import com.codingparty.connection.TCPConnectionThread;
import com.codingparty.core.CodingPartyServerMain;
import com.codingparty.entity.util.Color;
import com.codingparty.logger.LoggerUtil;
import com.codingparty.packet.EnumPacketSendType;
import com.codingparty.packet.tcp.TCPOutgoingBundleManager;
import com.codingparty.packet.tcp.TCPServerCapacityPacket;
import com.codingparty.packet.tcp.TCPServerIDPacket;
import com.codingparty.packet.tcp.TCPServerLevelPacket;
import com.codingparty.packet.tcp.TCPServerLoginPacket;
import com.codingparty.packet.tcp.TCPServerPacketBundle;
import com.codingparty.packet.tcp.TCPServerPlayerColorPacket;

public class CodingPartyTCPServer extends AbstractTCPServer {

	private TCPConnectionThread[] connectionThreads;
	private ArrayList<Integer> activeConnectionIDs;
	private boolean isTestConnection;
	
	public CodingPartyTCPServer(String threadName, String ip, int port, int waitTimeMilliseconds, int maxConnections) {
		super(threadName, ip, port, waitTimeMilliseconds, maxConnections);
		connectionThreads = new TCPConnectionThread[maxConnections];
		activeConnectionIDs = new ArrayList<>();
		isTestConnection = true;
	}

	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
			
			while (isListening) {
				System.out.println("Waiting for connection...");
				Socket clientServerConnection = serverSocket.accept();
				
				//TODO: Ideally I'd wanna use this, so no one on the same computer as the server can play. And it would allow for multiple test connections to query the server.
//				String clientIPAddress = clientServerConnection.getRemoteSocketAddress().toString();
//				String trimmedIPAddress = clientIPAddress.substring(1, clientIPAddress.indexOf(':'));
//				if (trimmedIPAddress.equals(PUBLIC_IP)) {
				
				//Skips the test connection.
				if (isTestConnection) {
					isTestConnection = false;
					continue;
				}
				
				int connectionID = 0;
				for (int i = 0; i < connectionThreads.length; i++) {
					if (connectionThreads[i] == null) {
						connectionID = i;
						break;
					}
				}
				
				System.out.println("Starting new connection thread using ID: " + connectionID);
				connectionThreads[connectionID] = new TCPConnectionThread(this, connectionID, clientServerConnection);
				connectionThreads[connectionID].start();
				activeConnectionIDs.add(connectionID);
				newUserBundleUpdate(connectionID);
			}
		} catch (IOException e) {
			LoggerUtil.logError(getClass(), e);
		}
	}
	
	public synchronized void sendBundle(TCPServerPacketBundle bundle) {
		byte[] bundleData = bundle.getPacketData();
		int[] receivingConnectionIDs = bundle.getReceivingConnectionIDs();
		boolean isBundleSent = false;
		
		
		if (bundle.getPacketSendType().equals(EnumPacketSendType.BROADCAST)) {
			for (int i = 0; i < connectionThreads.length; i++) {
				if (connectionThreads[i] != null) {
					connectionThreads[i].sendBundleToThread(bundleData);
					LoggerUtil.logInfo(getClass(), "BUNDLE HAS BEEN SHIPPED TO CONNECTION: " + connectionThreads[i].getConnectionID());
					isBundleSent = true;
				}
			}
		}
		else if (bundle.getPacketSendType().equals(EnumPacketSendType.SELECT)) {
			for (int i = 0; i < receivingConnectionIDs.length; i++) {
				if (connectionThreads[receivingConnectionIDs[i]] != null) {
					connectionThreads[receivingConnectionIDs[i]].sendBundleToThread(bundleData);
					LoggerUtil.logInfo(getClass(), "BUNDLE HAS BEEN SHIPPED TO CONNECTION: " + connectionThreads[i].getConnectionID());
					isBundleSent = true;
				}
			}
		}
		else {
			if (connectionThreads[receivingConnectionIDs[0]] != null) {
				connectionThreads[receivingConnectionIDs[0]].sendBundleToThread(bundleData);
				LoggerUtil.logInfo(getClass(), "BUNDLE HAS BEEN SHIPPED TO CONNECTION: " + connectionThreads[0].getConnectionID());
				isBundleSent = true;
			}
		}
		
		if (!isBundleSent) {
			LoggerUtil.logWarn(getClass(), "THE BUNDLE WAS NEVER SENT.");
		}
		
		bundle.setBundleSent();
		bundle = null;
	}
	
//	public synchronized void sendPacket(byte[] packetData, boolean echoPacketBackToSender) {
//		for (int i = 0; i < connectionThreads.length; i++) {
//			if (connectionThreads[i] != null) {
//				
//				if ((echoPacketBackToSender) || (connectionThreads[i].getConnectionID() != TCPPacketUtil.getConnectionID(packetData))) {
//					System.out.println("SENDING PACKET TO CONNECTION: " + connectionThreads[i].getConnectionID());
//					connectionThreads[i].sendPacket(packetData, packetData.length);
//				}
//			}
//		}
//	}
//	
//	public synchronized void sendPacketToSpecificConnection(byte[] packetData, int byteLength, int connectionID) {
//		if (connectionThreads[connectionID] != null) {
//			connectionThreads[connectionID].sendPacket(packetData, byteLength);
//		}
//	}
	
	private synchronized void newUserBundleUpdate(int connectionID) {
		Color playerColor = new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1.0f);
		
		TCPServerPacketBundle newUserBundle = new TCPServerPacketBundle(connectionID);
		newUserBundle.addPacketToBundle(new TCPServerIDPacket(connectionID));
		newUserBundle.addPacketToBundle(new TCPServerCapacityPacket(SERVER_CAPACITY));
		newUserBundle.addPacketToBundle(new TCPServerLoginPacket(connectionID, true));
		newUserBundle.addPacketToBundle(new TCPServerPlayerColorPacket(connectionID, playerColor));
		newUserBundle.addPacketToBundle(new TCPServerLevelPacket(CodingPartyServerMain.getWorld().getGameManager().getLevel().getLevelType()));
		
		ConnectedUser[] users = CodingPartyServerMain.getWorld().getGameManager().getUsers();
		
		for (int i = 0; i < activeConnectionIDs.size(); i++) {
			if (activeConnectionIDs.get(i) != connectionID) {
				newUserBundle.addPacketToBundle(new TCPServerLoginPacket(activeConnectionIDs.get(i), true));
				newUserBundle.addPacketToBundle(new TCPServerPlayerColorPacket(activeConnectionIDs.get(i), users[activeConnectionIDs.get(i)].getEntityPlayerMP().getColor()));
			}
		}
		
		TCPOutgoingBundleManager.shipBundle(newUserBundle);
		
		TCPServerPacketBundle loginBundle = new TCPServerPacketBundle(new int[] {connectionID}, true);
		loginBundle.addPacketToBundle(new TCPServerLoginPacket(connectionID, true));
		loginBundle.addPacketToBundle(new TCPServerPlayerColorPacket(connectionID, playerColor));
		
		TCPOutgoingBundleManager.shipBundle(loginBundle);
	}
	
	public synchronized void disconnectThread(int connectionID) {
		CodingPartyServerMain.getWorld().getGameManager().removeUser(connectionID);
		connectionThreads[connectionID].closeConnection();
		connectionThreads[connectionID] = null;
		activeConnectionIDs.remove((Integer)connectionID);
	}
	
	public ArrayList<Integer> getConnectionIDs() {
		return activeConnectionIDs;
	}
	
	public int getSeverOccupancy() {
		return activeConnectionIDs.size();
	}
	
	public boolean isServerFull() {
		return (activeConnectionIDs.size() >= this.SERVER_CAPACITY);
	}
}

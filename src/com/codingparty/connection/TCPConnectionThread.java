package com.codingparty.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.codingparty.logger.LoggerUtil;
import com.codingparty.packet.tcp.TCPIncomingBundleManager;
import com.codingparty.server.tcp.CodingPartyTCPServer;

public class TCPConnectionThread extends Thread {

	//Add a user object here.
	private CodingPartyTCPServer serverHandle;
	private Socket TCPSocket;
	private int connectionID;
	private DataOutputStream dataOutStream;
	private DataInputStream dataInputStream;
	
	public TCPConnectionThread(CodingPartyTCPServer server, int id, Socket connectionSocket) throws IOException {
		serverHandle = server;
		connectionID = id;
		TCPSocket = connectionSocket;
		OutputStream out = TCPSocket.getOutputStream();
		dataOutStream = new DataOutputStream(out);
		InputStream in = TCPSocket.getInputStream();
		dataInputStream = new DataInputStream(in);
	}
	
	@Override
	public void run() {
		while (serverHandle.isListening() && !TCPSocket.isClosed()) {
			
			try {
				if (dataInputStream.available() > 0) {
					//Listens for any TCP Packets coming in from this client's connection thread.
					//This is then sent off to the TCPPacketHandler to decide what to do and update all the other threads.
					byte[] incomingBundleData = readBundleDataFromThread();
					if (incomingBundleData != null) {
						TCPIncomingBundleManager.retreiveBundle(incomingBundleData);
					}
				}
			} catch (IOException e) {
				LoggerUtil.logError(getClass(), e);
			}
		}
		System.out.println("TCP CONNECTION THREAD ENDED FOR CONNECTION: " + connectionID);
	}
	
	public synchronized byte[] readBundleDataFromThread() {
		try {
			int packetLength = dataInputStream.readInt();
			byte[] packetData = new byte[packetLength];
			dataInputStream.readFully(packetData);
			return packetData;
			
		} catch (SocketException e) {
			LoggerUtil.logWarn(getClass(), "Client ID: " + connectionID + " lost connection unexpectedly.");
			serverHandle.disconnectThread(connectionID);
		} catch (IOException e) {
			LoggerUtil.logError(getClass(), "Error reading packet data.", e);
		}
		return null;
	}
	
	public synchronized void sendBundleToThread(byte[] packetData) {
		try {
			dataOutStream.writeInt(packetData.length);
			dataOutStream.write(packetData);
		} catch (IOException e) {
			LoggerUtil.logWarn(getClass(), "Client ID: " + connectionID + " lost connection unexpectedly.");
			serverHandle.disconnectThread(connectionID);
			return;
		}
	}
	
	public synchronized void closeConnection() {
		try {
			TCPSocket.close();
		} catch (IOException e) {
			LoggerUtil.logError(getClass(), e);
		}
		System.out.println("Closed connection to connectionID: " + connectionID);
	}
	
	public int getConnectionID() {
		return connectionID;
	}
}

package com.codingparty.server.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.codingparty.logger.LoggerUtil;
import com.codingparty.server.AbstractServer;

public abstract class AbstractTCPServer extends AbstractServer {

	public final int SERVER_CAPACITY;
	protected volatile boolean isListening;
	private int timeoutMilliseconds;
	
	public AbstractTCPServer(String threadName, String ip, int port, int waitTimeMilliseconds, int maxConnections) {
		super(threadName, ip, port);
		SERVER_CAPACITY = maxConnections;
		timeoutMilliseconds = waitTimeMilliseconds;
	}
	
	public boolean isServerConnectable() {
		try (Socket testConnect = new Socket()) {
			testConnect.connect(new InetSocketAddress(InetAddress.getByName(IP_ADDRESS), portNumber), timeoutMilliseconds);
		} catch (IOException e) {
			LoggerUtil.logError(getClass(), "Could not connect to server. Is it running?", e);
			return false;
		}
		
		LoggerUtil.logInfo(getClass(), "The server is open! IP: " + IP_ADDRESS + " Port: " + portNumber);
		return true;
	}
	
	public synchronized void startListening() {
		isListening = true;
		this.start();
	}
	
	public synchronized boolean isListening() {
		return isListening;
	}
	
	public synchronized void stopListening() {
		isListening = false;
	}
}

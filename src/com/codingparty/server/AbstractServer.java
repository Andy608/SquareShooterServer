package com.codingparty.server;

public abstract class AbstractServer extends Thread {
	protected final String IP_ADDRESS;
	protected int portNumber;
	
	public AbstractServer(String threadName, String ip, int port) {
		setName(threadName);
		IP_ADDRESS = ip;
		portNumber = port;
	}
	
	public final String getHostIP() {
		return IP_ADDRESS;
	}
	
	public final int getPort() {
		return portNumber;
	}
}

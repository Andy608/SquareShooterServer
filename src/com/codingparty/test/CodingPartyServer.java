package com.codingparty.test;

import java.io.IOException;

public class CodingPartyServer {

	public static void main(String[] args) {
		System.out.println("HELLO");
		try {
			new UDPMulticastServer("239.255.1.255", 608).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

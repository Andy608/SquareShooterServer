package com.codingparty.packet;

public enum EnumPacketSendType {

	//Sends the packet to everyone connected to the server.
	BROADCAST,
	
	//Sends the packet to only the specified list of connected users in the packet class.
	SELECT,
	
	//Sends the packet to only one user.
	PRIVATE;
}

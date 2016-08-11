package com.codingparty.packet.tcp.old;

public class TCPIncomingPacketHandler {

//	public static void decipherIncomingPacketData(CodingPartyTCPServer server, byte[] packetData) {
//		System.out.println("Deciphering packet from bytes...");
//		EnumTCPPacketType packetType = TCPPacketUtil.getEnum(packetData);
//		
//		switch(packetType) {
//		case LOG_IN_OFF_PACKET:
//			executeIncomingLogOnOffPacket(server, packetData);
//			break;
//		case NAME_CHANGE_PACKET:
//			executeIncomingNameChangePacket(server, packetData);
//			break;
//		case SERVER_CAPACITY_PACKET:
//			executeIncomingServerCapacityPacket(server, packetData);
//			break;
//		case PLAYER_COLOR_CHANGE_PACKET:
//			executeIncomingPlayerColorChangePacket(server, packetData);
//			break;
//		default:
//			LoggerUtil.logWarn(TCPIncomingPacketHandler.class, "A packet could not be deciphered from the data given.");
//			return;
//		}
//	}
//	
//	private static void executeIncomingLogOnOffPacket(CodingPartyTCPServer server, byte[] packetData) {
//		int connectionID = TCPPacketUtil.getConnectionID(packetData);
//		
//		boolean isLoggingOn = (packetData[8] == 1 ? true : false);
//		System.out.println("RECEIVED LOG ON/OFF PACKET FROM CONNECTION: " + connectionID + " LOG IN / OFF: " + isLoggingOn);
//		
////		System.out.println("\nReceived log on/off packet from server: " + "\nUserID = " + userID + "\nisLoggingOn = " + isLoggingOn);
//		//TODO: Handle what to do when a user logs in or out!
//		if (!isLoggingOn) {
//			server.disconnectThread(connectionID);
//		}
//		TCPOutgoingPacketHandler.sendPacket(server, new TCPLoginPacket(connectionID, isLoggingOn));
//	}
//	
//	private static void executeIncomingNameChangePacket(CodingPartyTCPServer server, byte[] packetData) {
//		//TODO: Handle what to do when a user changes his/her name!
//		
////		int connectionID = TCPPacketUtil.getConnectionID(packetData);
////		System.out.println("RECEIVED NAME CHANGE PACKET FOR CONNECTION: " + connectionID + " UPDATED NAME: " + updatedName);
//	}
//	
//	private static void executeIncomingServerCapacityPacket(CodingPartyTCPServer server, byte[] packetData) {
//		int connectionID = TCPPacketUtil.getConnectionID(packetData);
//		System.out.println("RECEIVED LOG ON/OFF PACKET FROM CONNECTION: " + connectionID);
//		TCPOutgoingPacketHandler.sendPacket(server, new TCPSendServerCapacityPacket(connectionID, server.SERVER_CAPACITY));
//	}
//	
//	private static void executeIncomingPlayerColorChangePacket(CodingPartyTCPServer server, byte[] packetData) {
//		int connectionID = TCPPacketUtil.getConnectionID(packetData);
//		Color newColor = Color.fromInt(TCPPacketUtil.getInt(packetData, 8));
//		System.out.println("RECEIVED PLAYER COLOR CHANGE PACKET FROM CONNECTION: " + connectionID + ". COLOR = " + newColor);
//		TCPOutgoingPacketHandler.sendPacket(server, new TCPPlayerColorChangePacket(connectionID, newColor));
//	}
}

package com.codingparty.packet.tcp.old;

public class TCPOutgoingPacketHandler {
	
//	public static synchronized void sendPacket(CodingPartyTCPServer server, int receivingConnectionID, AbstractTCPPacket packet, boolean isPrivatePacket) {
//		EnumTCPPacketType packetType = packet.getPacketType();
//		byte[] packetData = packet.getPreparedData();
//		
//		System.out.println("PACKET TYPE : " + TCPPacketUtil.getInt(packetData, 0));
//		
//		switch(packetType) {
//		case LOG_IN_OFF_PACKET:
//			executeOutgoingLogOnOffPacket(server, packetData, receivingConnectionID, isPrivatePacket);
//			break;
//		case SET_ID_PACKET:
//			server.sendPacketToSpecificConnection(packetData, packetData.length, packet.getConnectionID());
//			break;
//		case LEVEL_CHANGE_PACKET:
//			executeLevelChangePacket(server, packetData, isPrivatePacket);
//			break;
//		case NAME_CHANGE_PACKET:
//			executeOutgoingNameChangePacket(server, packetData);
//			break;
//		case SERVER_CAPACITY_PACKET:
//			executeOutgoingServerCapacityPacket(server, packetData);
//			break;
//		case PLAYER_COLOR_CHANGE_PACKET:
//			executeOutgoingPlayerColorChangePacket(server, packetData);
//			break;
//		default:
//			LoggerUtil.logWarn(TCPOutgoingPacketHandler.class, "A packet could not be deciphered from the data given.");
//			return;
//		}
//	}
//	
//	public static synchronized void sendPacket(CodingPartyTCPServer server, AbstractTCPPacket packet) {
//		sendPacket(server, -1, packet, false);
//	}
//	
//	private static synchronized void executeOutgoingLogOnOffPacket(CodingPartyTCPServer server, byte[] packetData, int receivingConnectionID, boolean isPrivatePacket) {
//		int connectionID = TCPPacketUtil.getConnectionID(packetData);
//		
//		boolean isLoggingOn = (packetData[8] == 1 ? true : false);
//		
//		System.out.println("SENDING LOG ON/OFF PACKET FOR CONNECTION: " + connectionID + " LOG IN / OFF: " + isLoggingOn);
//		
//		if (isLoggingOn) {
//			CodingPartyServerMain.getCurrentWorld().getGameManager().addUser(connectionID);
//		}
//		else {
//			CodingPartyServerMain.getCurrentWorld().getGameManager().removeUser(connectionID);
//		}
//		
//		if (isPrivatePacket) {
//			server.sendPacketToSpecificConnection(packetData, packetData.length, receivingConnectionID);
//		}
//		else {
//			server.sendPacket(packetData, false);
//		}
//	}
//	
//	//TODO: when someone new logs in, send them all the things they need.
//	//DO NOT JUST SEND ANOTHER PACKET TO EVERYONE ON THE SERVER.
//	//Only send the packets the new client needs to be updated.
//	
//	private static synchronized void executeLevelChangePacket(CodingPartyTCPServer server, byte[] packetData, boolean isPrivatePacket) {
//		int levelID = TCPPacketUtil.getInt(packetData, 8);
//		System.out.println("SENDING LEVEL CHANGE PACKET. LEVEL = " + Levels.getLevelByID(levelID));
//		CodingPartyServerMain.getCurrentWorld().getGameManager().setLevel(levelID);
//		
//		if (isPrivatePacket) {
//			server.sendPacketToSpecificConnection(packetData, packetData.length, TCPPacketUtil.getConnectionID(packetData));
//		}
//		else {
//			server.sendPacket(packetData, true);
//		}
//	}
//	
//	private static synchronized void executeOutgoingNameChangePacket(CodingPartyTCPServer server, byte[] packetData) {
//		int connectionID = TCPPacketUtil.getConnectionID(packetData);
//		
//		byte[] nameInBytes = new byte[packetData.length - 8];
//		for (int i = 0; i < nameInBytes.length; i++) {
//			nameInBytes[i] = packetData[i + 8];
//		}
//		String updatedName = new String(nameInBytes);
//		
//		CodingPartyServerMain.getCurrentWorld().getGameManager().setNameForUser(connectionID, updatedName);
//		System.out.println("SENDING NAME CHANGE PACKET FOR CONNECTION: " + connectionID + " UPDATED NAME: " + updatedName);
//		server.sendPacket(packetData, true);
//	}
//	
//	private static synchronized void executeOutgoingServerCapacityPacket(CodingPartyTCPServer server, byte[] packetData) {
//		int connectionID = TCPPacketUtil.getConnectionID(packetData);
//		System.out.println("SENDING SERVER CAPACITY PACKET TO CONNECTION: " + connectionID + ". CAPACITY = " + TCPPacketUtil.getInt(packetData, 8));
//		server.sendPacketToSpecificConnection(packetData, packetData.length, connectionID);
//	}
//	
//	private static synchronized void executeOutgoingPlayerColorChangePacket(CodingPartyTCPServer server, byte[] packetData) {
//		int connectionID = TCPPacketUtil.getConnectionID(packetData);
//		Color newColor = Color.fromInt(TCPPacketUtil.getInt(packetData, 8));
//		CodingPartyServerMain.getCurrentWorld().getGameManager().setUserColor(connectionID, newColor);
//		System.out.println("SENDING PLAYER COLOR CHANGE PACKET TO CONNECTION: " + connectionID + ". COLOR = " + newColor);
//		server.sendPacket(packetData, packetData.length, true);
//	}
//	
//	//TODO: ADD PACKET BATCHES THAT SEND OUT PACKETS TOGETHER IN ONE PACKET SO THAT THE DELAY IS LESS.
//	//(EXAMPLE THE COLOR LAGS WHEN A NEW USER LOGS IN. THIS WILL GO AWAY (hopefully) IF THE LOGIN AND COLOR PACKETS GET SENT TOGETHER.
//	public static synchronized void informNewUser(CodingPartyTCPServer server, int connectionID) {
//		sendPacket(server, new TCPSetConnectionIDPacket(connectionID));
//		sendPacket(server, new TCPSendServerCapacityPacket(connectionID, server.SERVER_CAPACITY));
//		sendPacket(server, new TCPLoginPacket(connectionID, true));
//		sendPacket(server, connectionID, new TCPLevelChangePacket(connectionID, CodingPartyServerMain.getCurrentWorld().getGameManager().getLevel()), true);
//		
//		ArrayList<Integer> connectedIDs = server.getConnectionIDs();
//		
//		for (int i = 0; i < connectedIDs.size(); i++) {
//			if (connectedIDs.get(i) != connectionID) {
//				sendPacket(server, connectionID, new TCPLoginPacket(connectedIDs.get(i), true), true);
//				ConnectedUser[] users = CodingPartyServerMain.getCurrentWorld().getGameManager().getUsers();
//				sendPacket(server, connectionID, new TCPPlayerColorChangePacket(connectedIDs.get(i), users[connectedIDs.get(i)].getEntityPlayerMP().getColor()), true);
//			}
//		}
//	}
}

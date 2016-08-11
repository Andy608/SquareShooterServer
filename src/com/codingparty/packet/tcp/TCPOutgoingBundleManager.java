package com.codingparty.packet.tcp;

import java.util.ArrayList;

import com.codingparty.core.CodingPartyServerMain;
import com.codingparty.logger.LoggerUtil;

public class TCPOutgoingBundleManager {

	public static synchronized void shipBundle(TCPServerPacketBundle bundle) {
		ArrayList<AbstractTCPServerPacket> bundlePackets = bundle.getPackets();
		
		System.out.println("\n=============== BUNDLE START ===============");
		
		for (int i = 0; i < bundlePackets.size(); i++) {
			EnumTCPPacketType packetType = bundlePackets.get(i).getPacketType();
			
			switch(packetType) {
			case ID_PACKET:
				IDPacketUpdate((TCPServerIDPacket)bundlePackets.get(i));
				break;
			case SERVER_CAPACITY_PACKET:
				serverCapacityPacketUpdate((TCPServerCapacityPacket)bundlePackets.get(i));
				break;
			case LOGIN_PACKET:
				loginPacketUpdate((TCPServerLoginPacket)bundlePackets.get(i));
				break;
			case PLAYER_COLOR_PACKET:
				playerColorPacketUpdate((TCPServerPlayerColorPacket)bundlePackets.get(i));
				break;
			case LEVEL_PACKET:
				levelPacketUpdate((TCPServerLevelPacket)bundlePackets.get(i));
				break;
			default:
				LoggerUtil.logWarn(TCPIncomingBundleManager.class, "The outgoing packet is not in the desired format. Type: " + packetType);
				return;
			}
		}
		
		System.out.println("================ BUNDLE END ================\n");
		
		//Send bundle to targeted clients
		CodingPartyServerMain.getInstance().getTCPServer().sendBundle(bundle);
	}
	
	private static synchronized void IDPacketUpdate(TCPServerIDPacket packet) {
		int ID = packet.getPlayerConnectionID();
		System.out.println("SENDING ID PACKET: ID = " + ID);
	}
	
	private static synchronized void serverCapacityPacketUpdate(TCPServerCapacityPacket packet) {
		System.out.println("SENDING SERVER CAPACITY PACKET: CAPACITY = " + packet.getCapacity());
	}
	
	private static synchronized void loginPacketUpdate(TCPServerLoginPacket packet) {
		int loginConnectionID = packet.getPlayerConnectionID();
		System.out.println("SENDING LOGIN PACKET: " + loginConnectionID + " IS LOGGING " + (packet.isLoggingIn() ? "IN" : "OUT") + ".");
		
		if (packet.isLoggingIn()) {
			if (!CodingPartyServerMain.getWorld().getGameManager().isUserAlreadyAdded(loginConnectionID)) {
				CodingPartyServerMain.getWorld().getGameManager().addUser(loginConnectionID);
			}
		}
		else {
			if (!CodingPartyServerMain.getWorld().getGameManager().isUserAlreadyRemoved(loginConnectionID)) {
				CodingPartyServerMain.getInstance().getTCPServer().disconnectThread(loginConnectionID);
			}
		}
	}
	
	private static synchronized void playerColorPacketUpdate(TCPServerPlayerColorPacket packet) {
		int playerConnectionID = packet.getPlayerConnectionID();
		System.out.println("SENDING PLAYER COLOR PACKET: " + playerConnectionID + " COLOR: " + packet.getColor());
		CodingPartyServerMain.getWorld().getGameManager().setUserColor(playerConnectionID, packet.getColor());
	}
	
	private static synchronized void levelPacketUpdate(TCPServerLevelPacket packet) {
		System.out.println("SENDING LEVEL PACKET: LEVEL = " + packet.getLevelType());
	}
}

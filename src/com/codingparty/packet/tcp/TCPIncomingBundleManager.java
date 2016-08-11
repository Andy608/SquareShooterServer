package com.codingparty.packet.tcp;

import java.util.ArrayList;

import com.codingparty.entity.util.Color;
import com.codingparty.logger.LoggerUtil;
import com.codingparty.math.MathHelper;
import com.codingparty.packet.PacketUtil;

public class TCPIncomingBundleManager {

	private static ArrayList<AbstractTCPServerPacket> incomingPackets = new ArrayList<>();
	private static TCPServerPacketBundle bundle;
	
	public static synchronized void retreiveBundle(byte[] bundleData) {
		int packetsInBundle = PacketUtil.getInt(bundleData, 0);
		int offset = 4;
		
		for (int i = 0; i < packetsInBundle; i++) {
			EnumTCPPacketType packetType = PacketUtil.getTCPEnum(bundleData, offset);
			byte[] packetData = new byte[packetType.getByteLength()];
			
			for (int j = 0; j < packetData.length; j++) {
				packetData[j] = bundleData[offset];
				offset++;
			}
			
			switch(packetType) {
			case LOGIN_PACKET:
				addLoginPacket(packetData);
				break;
			case PLAYER_COLOR_PACKET:
				addColorPacket(packetData);
				break;
			default:
				LoggerUtil.logWarn(TCPIncomingBundleManager.class, "The incoming packet is not in the desired format. Type: " + packetType);
				return;
			}
		}
	}
	
	/**
	 * Adds a new login packet to the incoming packet stack.
	 * NOTE: The server will not allow the user to login until it receives a color packet for the user as well.
	 * @param loginPacketData : The login packet data.
	 */
	private static synchronized void addLoginPacket(byte[] loginPacketData) {
		int connectionID = PacketUtil.getInt(loginPacketData, 4);
		System.out.println("RECEIVED LOGIN PACKET: LOGIN = " + (loginPacketData[8] == (byte)1 ? true : false));
		incomingPackets.add(new TCPServerLoginPacket(connectionID, (loginPacketData[8] == (byte)1) ? true : false));
	}
	
	/**
	 * Adds a new color packet to the incoming packet stack.
	 * @param colorPacketData : The color packet data.
	 */
	private static synchronized void addColorPacket(byte[] colorPacketData) {
		int connectionID = PacketUtil.getInt(colorPacketData, 4);
		incomingPackets.add(new TCPServerPlayerColorPacket(connectionID, Color.fromInt(PacketUtil.getInt(colorPacketData, 8))));
	}
	
	public static synchronized void update() {
		checkForLoginPackets();
		checkForColorPackets();
		checkForLeftOverPackets();
		sendBundleToOutgoingBundleManager();
	}
	
	private static synchronized void checkForLoginPackets() {
		for (int packetIndex = 0; packetIndex < incomingPackets.size(); packetIndex++) {
			AbstractTCPServerPacket currentPacket = incomingPackets.get(packetIndex);
			if (currentPacket.getPacketType().equals(EnumTCPPacketType.LOGIN_PACKET)) {
				TCPServerLoginPacket loginPacket = (TCPServerLoginPacket) currentPacket;
				
				if (!loginPacket.isLoggingIn()) {
					//Update the server game. Remove player from game.
//					CodingPartyServerMain.getInstance().getTCPServer().disconnectThread(loginPacket.getPlayerConnectionID());
					
					//Add the login packet to the outgoing bundle.
					addPacketToOutgoingBundle(loginPacket);
					
					//Shift index since I removed 1 packet.
					packetIndex--;
				}
				else {
					TCPServerPlayerColorPacket colorPacket = (TCPServerPlayerColorPacket) getPacketWithConnectionIDFromIncomingList(EnumTCPPacketType.PLAYER_COLOR_PACKET, loginPacket.getPlayerConnectionID());
					
					if (colorPacket != null) {
						//Update the server game. Set the color of the player. Add player to game.
//						CodingPartyServerMain.getCurrentWorld().getGameManager().addUser(colorPacket.getPlayerConnectionID(), colorPacket.getColor());
						
						int indexOfColorPacket = incomingPackets.indexOf(colorPacket);
						
						//Add the login packet and the color packet to the outgoing bundle.
						addPacketToOutgoingBundle(loginPacket);
						addPacketToOutgoingBundle(colorPacket);
						
						//Shift indexes since I removed 2 packets.
						if (MathHelper.min(packetIndex, indexOfColorPacket) == indexOfColorPacket) {
							packetIndex = indexOfColorPacket - 1;
						}
						else {
							packetIndex--;
						}
					}
				}
			}
		}
	}
	
	private static synchronized void checkForColorPackets() {
		for (int packetIndex = 0; packetIndex < incomingPackets.size(); packetIndex++) {
			if (incomingPackets.get(packetIndex).getPacketType().equals(EnumTCPPacketType.PLAYER_COLOR_PACKET)) {
				addPacketToOutgoingBundle(incomingPackets.get(packetIndex));
				packetIndex--;
			}
		}
	}
	
	private static synchronized void checkForLeftOverPackets() {
		for (int packetIndex = 0; packetIndex < incomingPackets.size(); packetIndex++) {
			EnumTCPPacketType packetType = incomingPackets.get(packetIndex).getPacketType();
			LoggerUtil.logWarn(TCPIncomingBundleManager.class, "PACKET WITH TYPE: " + packetType + " is not being handled in the incomingPackets list.");
		}
	}
	
	private static synchronized AbstractTCPServerPlayerUpdatePacket getPacketWithConnectionIDFromIncomingList(EnumTCPPacketType type, int connectionID) {
		
		for (int packetIndex = 0; packetIndex < incomingPackets.size(); packetIndex++) {
			if (incomingPackets.get(packetIndex) instanceof AbstractTCPServerPlayerUpdatePacket) {
				AbstractTCPServerPlayerUpdatePacket packet = (AbstractTCPServerPlayerUpdatePacket) incomingPackets.get(packetIndex);
				
				if (packet.getPacketType().equals(type) && packet.getPlayerConnectionID() == connectionID) {
					return packet;
				}
			}
		}
		LoggerUtil.logWarn(TCPIncomingBundleManager.class, "Could not find a packet in the bundle with type = " + type + " and connectionID = " + connectionID);
		return null;
	}
	
	private static synchronized void addPacketToOutgoingBundle(AbstractTCPServerPacket packet) {
		if (bundle == null || bundle.isBundleShipped()) {
			bundle = new TCPServerPacketBundle();
		}
		bundle.addPacketToBundle(packet);
		incomingPackets.remove(packet);
	}
	
	private static synchronized void sendBundleToOutgoingBundleManager() {
		if (bundle != null && !bundle.isBundleShipped() && !bundle.isEmpty()) {
			TCPOutgoingBundleManager.shipBundle(bundle);
		}
	}
}

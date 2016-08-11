package com.codingparty.packet.udp;

import java.util.ArrayList;

import com.codingparty.core.CodingPartyServerMain;
import com.codingparty.math.ArrayUtil;

public class UDPOutgoingPacketManager {

	private static ArrayList<AbstractUDPServerPacket> outgoingPackets = new ArrayList<>();
	
	public static synchronized void shipPacket(AbstractUDPServerPacket packet) {
		EnumUDPPacketType packetType = packet.getPacketType();
		
		switch(packetType) {
		case PLAYER_POSITION_UPDATE_PACKET:
			outgoingPackets.add((UDPServerPlayerStateUpdatePacket)packet);
			playerStateUpdate((UDPServerPlayerStateUpdatePacket)packet);
			break;
		case OBJECT_STATE_UPDATE_PACKET:
			break;
		}
		
		//Broadcast packet using udp multicast server.
	}
	
	private static synchronized void playerStateUpdate(UDPServerPlayerStateUpdatePacket playerUpdatePacket) {
//		System.out.println("SENDING PLAYER STATE UPDATE PACKET: " + playerUpdatePacket.getPlayerConnectionID());
	}
	
	public static synchronized AbstractUDPServerPacket[] getOutgoingPackets() {
		AbstractUDPServerPacket[] copy = ArrayUtil.convertListToUDPPacketArray(outgoingPackets);
		outgoingPackets.clear();
		return copy;
	}
	
	public static synchronized void update() {
		if (!outgoingPackets.isEmpty()) {
			CodingPartyServerMain.getInstance().getUDPMulticastServer().sendPackets(outgoingPackets);
		}
	}
}

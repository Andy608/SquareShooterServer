package com.codingparty.packet.udp;

import java.util.ArrayList;

import com.codingparty.connection.ConnectedUser;
import com.codingparty.core.CodingPartyServerMain;
import com.codingparty.entity.EntityPlayerMP;
import com.codingparty.packet.PacketUtil;

public class UDPIncomingPacketManager {
	
	private static ArrayList<AbstractUDPServerPacket> incomingPackets = new ArrayList<>();
	
	public static synchronized void retreivePacket(byte[] packetData) {
		EnumUDPPacketType packetType = PacketUtil.getUDPEnum(packetData, 0);
		
		switch (packetType) {
		case PLAYER_CONTROL_UPDATE_PACKET:
			addPlayerState(packetData);
			break;
		case OBJECT_STATE_UPDATE_PACKET:
			//TODO: Add this.
			break;
		}
	}
	
	private static synchronized void addPlayerState(byte[] packetData) {
		int connectionID = PacketUtil.getInt(packetData, 4);
		long initialTime = PacketUtil.getLong(packetData, 8);
		boolean forward = PacketUtil.getBoolean(packetData[16]);
		boolean backward = PacketUtil.getBoolean(packetData[17]);
		boolean left = PacketUtil.getBoolean(packetData[18]);
		boolean right = PacketUtil.getBoolean(packetData[19]);
//		System.out.println("ADDING PLAYER STATE UPDATE PACKET: CONNECTION = " + connectionID);
		incomingPackets.add(new UDPClientPlayerStateUpdatePacket(connectionID, initialTime, forward, backward, left, right));
	}
	
	public static synchronized void update(double deltaTime) {
		ConnectedUser[] connectedUsers = CodingPartyServerMain.getWorld().getGameManager().getUsers();
		
		for (int packetIndex = 0; packetIndex < incomingPackets.size(); packetIndex++) {
			if (incomingPackets.get(packetIndex).getPacketType().equals(EnumUDPPacketType.PLAYER_CONTROL_UPDATE_PACKET)) {
				UDPClientPlayerStateUpdatePacket updatePacket = (UDPClientPlayerStateUpdatePacket) incomingPackets.get(packetIndex);
				if (connectedUsers[updatePacket.getPlayerConnectionID()] != null) {
					EntityPlayerMP playerMP = connectedUsers[updatePacket.getPlayerConnectionID()].getEntityPlayerMP();
					double delta = (System.nanoTime() - updatePacket.getInitialTime()) / 1000000000.0D;
					
//					System.out.println("UPDATING PLAYER POS FOR CONNECTION: " + updatePacket.getPlayerConnectionID());
					
					if (updatePacket.isForwardPressed) {
						playerMP.moveForward();
					}
					
					if (updatePacket.isBackPressed) {
						playerMP.moveBackward();
					}
					
					if(updatePacket.isLeftPressed) {
						playerMP.moveLeft();
					}
					
					if(updatePacket.isRightPressed) {
						playerMP.moveRight();
					}
					
					playerMP.update(deltaTime);
					
//					System.out.println("FORWARD: " + updatePacket.isForwardPressed + " | " + " BACKWARD: " + updatePacket.isBackPressed + " | " + " LEFT: " + updatePacket.isLeftPressed + " | " + " RIGHT: " + updatePacket.isRightPressed);
					
//					playerMP.updatePosition(updatePacket.positionX, updatePacket.positionY, updatePacket.positionZ, deltaTime);
//					playerMP.updateRotation(updatePacket.rotationX, updatePacket.rotationY, updatePacket.rotationZ, deltaTime);
					UDPOutgoingPacketManager.shipPacket(new UDPServerPlayerStateUpdatePacket(updatePacket.getPlayerConnectionID(), System.nanoTime(), playerMP.getX(), playerMP.getY(), playerMP.getZ(), playerMP.getRotAboutX(), playerMP.getRotAboutY(), playerMP.getRotAboutZ()));
				}
				incomingPackets.remove(updatePacket);
				packetIndex--;
			}
		}
	}
	
//	public static double getDeltaTimeFromIncomingPacket(UDPClientPlayerStateUpdatePacket packet) {
//		return (System.nanoTime() - packet.getInitialTime()) / 1000000000.0D;
//	}
}

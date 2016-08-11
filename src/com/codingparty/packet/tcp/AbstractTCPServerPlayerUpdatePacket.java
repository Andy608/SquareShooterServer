package com.codingparty.packet.tcp;

public abstract class AbstractTCPServerPlayerUpdatePacket extends AbstractTCPServerPacket {

	/**
	 * ::Packet Data::
	 * First 4 bytes will always be EnumPacketType
	 * Next 4 bytes will always be connectionID of the person the color is changing.
	 */
	
	protected int playerConnectionID;
	
	public AbstractTCPServerPlayerUpdatePacket(EnumTCPPacketType type, int connectionID) {
		super(type);
		playerConnectionID = connectionID;
	}
	
	public int getPlayerConnectionID() {
		return playerConnectionID;
	}
}

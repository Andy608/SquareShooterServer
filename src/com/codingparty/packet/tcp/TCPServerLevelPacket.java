package com.codingparty.packet.tcp;

import java.nio.ByteBuffer;

import com.codingparty.level.EnumLevelType;

public class TCPServerLevelPacket extends AbstractTCPServerPacket {

	/**
	 * ::Packet Data::
	 * First 4 bytes will always be EnumPacketType
	 * Next 4 bytes will always be the levelID.
	 */
	
	private EnumLevelType levelType;
	
	public TCPServerLevelPacket(EnumLevelType level) {
		super(EnumTCPPacketType.LEVEL_PACKET);
		levelType = level;
	}

	@Override
	protected byte[] prepare() {
		ByteBuffer packetData = ByteBuffer.allocate(getByteLength());
		packetData.putInt(getPacketType().ordinal());
		packetData.putInt(levelType.getLevelID());
		isPreparedForShipment = true;
		return (preparedData = packetData.array());
	}
	
	public EnumLevelType getLevelType() {
		return levelType;
	}
}

package com.codingparty.packet.tcp.old;

public class TCPLevelChangePacket {

//	private int levelID;
//	
//	public TCPLevelChangePacket(int id, AbstractLevel level) {
//		super(EnumTCPPacketType.LEVEL_CHANGE_PACKET, id);
//		levelID = level.getLevelInfo().getLevelID();
//	}
//
//	@Override
//	public byte[] prepare() {
//		byteLength = 12;
//		ByteBuffer b = ByteBuffer.allocate(byteLength);
//		b.putInt(packetType.ordinal());
//		b.putInt(connectionID);
//		b.putInt(levelID);
//		isPreparedForShipment = true;
//		return (preparedData = b.array());
//	}

}

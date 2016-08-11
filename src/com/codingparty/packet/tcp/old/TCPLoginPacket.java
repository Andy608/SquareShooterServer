package com.codingparty.packet.tcp.old;

public class TCPLoginPacket {

//	private boolean isLoggingOn;
//	
//	public TCPLoginPacket(int id, boolean loggingIn) {
//		super(EnumTCPPacketType.LOG_IN_OFF_PACKET, id);
//		isLoggingOn = loggingIn;
//	}
//	
//	public boolean isLoggingOn() {
//		return isLoggingOn;
//	}
//	
//	public boolean isLoggingOff() {
//		return !isLoggingOn;
//	}
//
//	@Override
//	public byte[] prepare() {
//		byteLength = 9;
//		ByteBuffer b = ByteBuffer.allocate(byteLength);
//		b.putInt(packetType.ordinal());
//		b.putInt(connectionID);
//		b.put((byte)(isLoggingOn ? 1 : 0));
//		isPreparedForShipment = true;
//		return (preparedData = b.array());
//	}
}

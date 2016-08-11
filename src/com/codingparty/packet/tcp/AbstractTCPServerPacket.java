package com.codingparty.packet.tcp;

public abstract class AbstractTCPServerPacket {

	/**
	 * ::Packet Headers::
	 * First 4 bytes will always be EnumPacketType.
	 */
	
	private static int nextAvailablePacketID;
	
	protected EnumTCPPacketType packetType;
	protected boolean isPreparedForShipment;
	protected byte[] preparedData;
	private int packetID;
	private boolean isPacketSent;
	
	public AbstractTCPServerPacket(EnumTCPPacketType type) {
		packetType = type;
		isPreparedForShipment = false;
		isPacketSent = false;
		packetID = nextAvailablePacketID++;
	}
	
	protected abstract byte[] prepare();
	
	public byte[] getPacketData() {
		if (!isPreparedForShipment) {
			preparedData = prepare();
			isPreparedForShipment = true;
			return preparedData;
		}
		return preparedData;
	}
	
	public EnumTCPPacketType getPacketType() {
		return packetType;
	}
	
	public int getByteLength() {
		return packetType.getByteLength();
	}
	
	public int getPacketID() {
		return packetID;
	}
	
	public boolean isPacketSent() {
		return isPacketSent;
	}
	
	public void setPacketSent() {
		isPacketSent = true;
		setNextAvailableID(packetID);
	}
	
	public void setNextAvailableID(int nextID) {
		nextAvailablePacketID = nextID;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof AbstractTCPServerPacket)) return false;
		AbstractTCPServerPacket otherPacket = (AbstractTCPServerPacket) other;
		return (packetID == otherPacket.packetID) && (isPacketSent == otherPacket.isPacketSent);
	}
}

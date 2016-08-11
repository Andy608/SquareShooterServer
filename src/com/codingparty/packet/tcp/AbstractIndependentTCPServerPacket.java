package com.codingparty.packet.tcp;

import com.codingparty.core.CodingPartyServerMain;
import com.codingparty.math.ArrayUtil;
import com.codingparty.packet.EnumPacketSendType;

public abstract class AbstractIndependentTCPServerPacket extends AbstractTCPServerPacket {

	//PROBABLY WILL NOT USE THIS. IF YOU WANT TO SEND PACKETS, JUST CREATE A BUNDLE AND PUT THEM ALL IN THERE.
	
	/**
	 * ::Packet Headers::
	 * First 4 bytes will always be EnumPacketType.
	 */
	
	protected EnumPacketSendType packetSendType;
	protected int receivingConnectionIDs[];
	
	public AbstractIndependentTCPServerPacket(EnumTCPPacketType type, int... receivingConnections) {
		this(type, EnumPacketSendType.SELECT, ArrayUtil.copyIntArray(receivingConnections));
	}
	
	public AbstractIndependentTCPServerPacket(EnumTCPPacketType type) {
		this(type, EnumPacketSendType.BROADCAST, ArrayUtil.convertListToIntegerArray(CodingPartyServerMain.getInstance().getTCPServer().getConnectionIDs()));
	}
	
	private AbstractIndependentTCPServerPacket(EnumTCPPacketType type, EnumPacketSendType sendType, int[] receivingConnections) {
		super(type);
		packetSendType = sendType;
		receivingConnectionIDs = receivingConnections;
	}
	
	public int[] getReceivingConnectionIDs() {
		return receivingConnectionIDs;
	}
	
	public EnumPacketSendType getSendType() {
		return packetSendType;
	}
}

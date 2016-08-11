package com.codingparty.packet.tcp;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.codingparty.core.CodingPartyServerMain;
import com.codingparty.math.ArrayUtil;
import com.codingparty.packet.EnumPacketPriorityLevel;
import com.codingparty.packet.EnumPacketSendType;

public class TCPServerPacketBundle {

	/**
	 * ::Packet Headers::
	 * First 4 bytes will always be the number of packets in the bundle.
	 * Next 4 bytes will always be EnumPacketType for packet #1. etc.
	 */
	
	private static int MAX_TCP_BUNDLE_BYTE_LENGTH = 1400;
	private static int PACKET_HEADER_BYTE_LENGTH = 4;
	private ByteBuffer bundleData;
	private int bundleByteLength;
	private boolean isPreparedforShipment;
	private volatile boolean isBundleShipped;
	protected EnumPacketSendType packetSendType;
	protected int receivingConnectionIDs[];
	private ArrayList<AbstractTCPServerPacket> packets;
	
	public TCPServerPacketBundle() {
		this(EnumPacketSendType.BROADCAST,  ArrayUtil.convertListToIntegerArray(CodingPartyServerMain.getInstance().getTCPServer().getConnectionIDs()));
	}
	
	public TCPServerPacketBundle(int recivingConnection) {
		this(EnumPacketSendType.PRIVATE, new int[] {recivingConnection});
	}
	
	public TCPServerPacketBundle(int[] connections, boolean excludesTheseConnections) {
		this(EnumPacketSendType.SELECT, getConnections(connections, excludesTheseConnections));
	}
	
	private TCPServerPacketBundle(EnumPacketSendType sendType, int[] receivingConnections) {
		packets = new ArrayList<>();
		packetSendType = sendType;
		receivingConnectionIDs = receivingConnections;
		isBundleShipped = false;
		isPreparedforShipment = false;
	}
	
	private static int[] getConnections(int[] connections, boolean excludesTheseConnections) {
		int[] receivingConnections;
		int indexCounter = 0;
		
		if (excludesTheseConnections) {
			ArrayList<Integer> connectedUsers = CodingPartyServerMain.getInstance().getTCPServer().getConnectionIDs();
			
			receivingConnections = new int[connectedUsers.size() - connections.length];
			for (int i = 0; i < connectedUsers.size(); i++) {
				for (int j = 0; j < connections.length; j++) {
					
					if (connectedUsers.get(i) == connections[j]) {
						continue;
					}
					else if (j == connections.length - 1) {
						receivingConnections[indexCounter++] = connectedUsers.get(i);
					}
				}
			}
			return receivingConnections;
		}
		else {
			return connections;
		}
	}
	
	public void addPacketToBundle(AbstractTCPServerPacket packet) {
		if (MAX_TCP_BUNDLE_BYTE_LENGTH >= bundleByteLength + packet.getByteLength() + PACKET_HEADER_BYTE_LENGTH) {
			if (!isBundleShipped && packets.add(packet)) {
				bundleByteLength += packet.getByteLength();
			}
		}
	}
	
	public byte[] getPacketData() {
		if (!isPreparedforShipment) {
			isPreparedforShipment = true;
			bundleData = ByteBuffer.allocate(PACKET_HEADER_BYTE_LENGTH + bundleByteLength);
			bundleData.putInt(packets.size());
			
			for (int priority = EnumPacketPriorityLevel.VERY_HIGH.ordinal(); priority <= EnumPacketPriorityLevel.LOW.ordinal(); priority++) {
				for (int packetIndex = 0; packetIndex < packets.size(); packetIndex++) {

					if (packets.get(packetIndex).getPacketType().getPriority().ordinal() == priority) {
						byte[] packetData = packets.get(packetIndex).getPacketData();
						
						for (int packetDataIndex = 0; packetDataIndex < packetData.length; packetDataIndex++) {
							bundleData.put(packetData[packetDataIndex]);
						}
					}
				}
			}
		}
		return bundleData.array();
	}
	
	public boolean isBundleShipped() {
		return isBundleShipped;
	}
	
	public synchronized void setBundleSent() {
		isBundleShipped = true;
		for (int i = 0; i < packets.size(); i++) {
			packets.get(i).setPacketSent();
		}
	}
	
	public ArrayList<AbstractTCPServerPacket> getPackets() {
		return packets;
	}
	
	public int getBundleByteLength() {
		return bundleByteLength;
	}
	
	public boolean isEmpty() {
		return packets.isEmpty();
	}
	
	public int[] getReceivingConnectionIDs() {
		return receivingConnectionIDs;
	}
	
	public EnumPacketSendType getPacketSendType() {
		return packetSendType;
	}
}

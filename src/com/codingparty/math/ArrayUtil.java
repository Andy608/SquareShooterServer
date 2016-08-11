package com.codingparty.math;

import java.util.Iterator;
import java.util.List;

import com.codingparty.packet.udp.AbstractUDPServerPacket;

public class ArrayUtil {
	
	public static int[] copyIntArray(int[] otherArray) {
		int[] newArray = new int[otherArray.length];
		System.arraycopy(otherArray, 0, newArray, 0, otherArray.length);
		return newArray;
	}
	
	public static int[] convertListToIntegerArray(List<Integer> integers) {
	    int[] intArray = new int[integers.size()];
	    Iterator<Integer> iterator = integers.iterator();
	    
	    for (int i = 0; i < intArray.length; i++) {
	    	intArray[i] = iterator.next().intValue();
	    }
	    return intArray;
	}
	
	public static synchronized AbstractUDPServerPacket[] convertListToUDPPacketArray(List<AbstractUDPServerPacket> packets) {
		AbstractUDPServerPacket[] packetArray = new AbstractUDPServerPacket[packets.size()];
	    
	    for (int i = 0; i < packetArray.length; i++) {
	    	packetArray[i] = packets.get(i);
	    }
	    return packetArray;
	}
	
	public static float[] convertListToFloatArray(List<Float> floats) {
	    float[] floatArray = new float[floats.size()];
	    Iterator<Float> iterator = floats.iterator();
	    
	    for (int i = 0; i < floatArray.length; i++) {
	    	floatArray[i] = iterator.next().floatValue();
	    }
	    return floatArray;
	}
}

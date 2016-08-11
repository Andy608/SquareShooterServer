package com.codingparty.connection;

import com.codingparty.entity.EntityPlayerMP;

public class ConnectedUser {

	private String username;
	private EntityPlayerMP player;
	private int connectionID;
	
	public ConnectedUser(int id, EntityPlayerMP p) {
		connectionID = id;
		player = p;
	}
	
	public int getConnectionID() {
		return connectionID;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String name) {
		username = name;
	}
	
	public void setEntityPlayerMP(EntityPlayerMP entityPlayer) {
		player = entityPlayer;
	}
	
	public EntityPlayerMP getEntityPlayerMP() {
		return player;
	}
}

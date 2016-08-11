package com.codingparty.connection;

import com.codingparty.entity.EntityPlayerMP;
import com.codingparty.entity.util.Color;
import com.codingparty.level.AbstractLevel;
import com.codingparty.level.LevelManager;
import com.codingparty.level.Levels;
import com.codingparty.logger.LoggerUtil;

public class GameManager {
	
	private ConnectedUser[] users;
	private LevelManager levelManager;
	
	public GameManager() {
		levelManager = new LevelManager();
	}
	
	public void initUsers(int serverCapacity) {
		if (users == null) {
			System.out.println("Server Capacity = " + serverCapacity);
			users = new ConnectedUser[serverCapacity];
		}
		else {
			LoggerUtil.logWarn(GameManager.class, "Users list has already been created.");
		}
	}
	
	public void setLevel(int levelID) {
		levelManager.setLevel(Levels.getLevelByID(levelID));
	}
	
	public AbstractLevel getLevel() {
		return levelManager.getCurrentLevel();
	}
	
	public void addUser(int connectionID) {
		if (!isUserAlreadyAdded(connectionID)) {
			System.out.println("ADDING USER: " + connectionID);
			users[connectionID] = new ConnectedUser(connectionID, new EntityPlayerMP());
		}
		else {
			LoggerUtil.logInfo(GameManager.class, "CANNOT ADD USER TO LIST.");
		}
	}
	
	public void removeUser(int connectionID) {
		if (users != null && users[connectionID] != null) {
			System.out.println("REMOVING USER: " + connectionID);
			users[connectionID] = null;
		}
	}
	
	public boolean isUserAlreadyAdded(int connectionID) {
		if (users != null && users[connectionID] != null) {
			return true;
		}
		return false;
	}
	
	public boolean isUserAlreadyRemoved(int connectionID) {
		if (users[connectionID] == null) {
			return true;
		}
		return false;
	}
	
	public void setNameForUser(int connectionID, String name) {
		if (users != null && users[connectionID] != null) {
			users[connectionID].setUsername(name);
		}
	}
	
	public void setUserColor(int connectionID, Color newColor) {
		if (users != null && users[connectionID] != null) {
			users[connectionID].getEntityPlayerMP().setColor(newColor);
		}
	}
	
	public ConnectedUser[] getUsers() {
		return users;
	}
	
	public void update(double deltaTime) {
		levelManager.update(deltaTime);
//		for (int i = 0; i < users.length; i++) {
//			if (users[i] != null) {
//				users[i].getEntityPlayerMP().update(deltaTime);
//			}
//		}
	}
}

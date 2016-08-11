package com.codingparty.level;

import com.codingparty.packet.tcp.TCPOutgoingBundleManager;
import com.codingparty.packet.tcp.TCPServerLevelPacket;
import com.codingparty.packet.tcp.TCPServerPacketBundle;

public class LevelManager {

	private LevelLobby defaultLevel;
	private AbstractLevel currentLevel;
	
	public LevelManager() {
		defaultLevel = Levels.getLobby();
	}
	
	public void restartLevel() {
		currentLevel.resetLevel();
	}
	
	public void setLevel(AbstractLevel newLevel) {
		if (newLevel == null) {
			currentLevel = defaultLevel;
		}
		else {
			currentLevel = newLevel;
		}
		
		TCPServerPacketBundle levelBundle = new TCPServerPacketBundle();
		levelBundle.addPacketToBundle(new TCPServerLevelPacket(currentLevel.getLevelType()));
		TCPOutgoingBundleManager.shipBundle(levelBundle);
		currentLevel.resetLevel();
	}
	
	public void update(double deltaTime) {
		currentLevel.update(deltaTime);
	}
	
	public AbstractLevel getCurrentLevel() {
		return currentLevel;
	}
}

package com.codingparty.world;

import java.util.Random;

import com.codingparty.component.callback.CursorPosCallback;
import com.codingparty.connection.GameManager;
import com.codingparty.core.CodingPartyServerMain;
import com.codingparty.entity.EntityTargetPoint;
import com.codingparty.file.setting.ProgramSettings;
import com.codingparty.level.EnumLevelType;

import math.Vector3f;

public class World {

	public static final Random rand = new Random();
	
	private boolean centerMouse;
	private boolean isPaused;
	
	private GameManager gameManager;
	private WorldRenderer worldRenderer;
	
//	private Camera debugCamera;
//	private TargetedCamera spectateCamera;
	private EntityTargetPoint entityTarget;
	
	public World() {
		gameManager = new GameManager();
//		debugCamera = new FreeRoamCamera(new Vector3f(0, 1, 0), new Vector3f(90, 0, 0));
		entityTarget = new EntityTargetPoint(new Vector3f(-13, 8, -2));
//		spectateCamera = new TargetedCamera(entityTarget, 30f, new Vector3f(45, 100, 0));
		
		centerMouse = false;
		isPaused = false;
		
		worldRenderer = new WorldRenderer();
	}
	
	public void initGameStartUp() {
		gameManager.initUsers(CodingPartyServerMain.getInstance().getTCPServer().SERVER_CAPACITY);
		gameManager.setLevel(EnumLevelType.LOBBY.getLevelID());
	}
	
	public void update(double deltaTime) {
		if (isPaused) return;
		
		if (centerMouse) {
			CursorPosCallback.centerMouse();
		}
		
		if (ProgramSettings.isDebugEnabled()) {
			centerMouse = true;
//			debugCamera.update(deltaTime);
		}
		else {
			centerMouse = false;
			entityTarget.update(deltaTime);
//			spectateCamera.update(deltaTime);
		}
		
		gameManager.update(deltaTime);
//		ModelResourceManager.update();
		
//		System.out.println(player.getPosition());
//		System.out.println(camera.getPosition());
	}
	
	public void render(double deltaTime) {
		worldRenderer.render(this);
//		spectateCamera.render(deltaTime);
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}
	
	public EntityTargetPoint getTarget() {
		return entityTarget;
	}
	
//	public Camera getSpectateCamera() {
//		return spectateCamera;
//	}
	
//	public Camera getDebugCamera() {
//		return debugCamera;
//	}
}

package com.codingparty.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;

import org.lwjgl.Sys;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.codingparty.camera.CameraMatrixManager;
import com.codingparty.component.Window;
import com.codingparty.component.callback.CursorPosCallback;
import com.codingparty.file.FileUtil;
import com.codingparty.level.Levels;
import com.codingparty.logger.LoggerUtil;
import com.codingparty.model.Models;
import com.codingparty.model.util.ModelResourceManager;
import com.codingparty.packet.tcp.TCPIncomingBundleManager;
import com.codingparty.packet.udp.UDPIncomingPacketManager;
import com.codingparty.packet.udp.UDPOutgoingPacketManager;
import com.codingparty.server.tcp.CodingPartyTCPServer;
import com.codingparty.server.udp.CodingPartyUDPMulticastServer;
import com.codingparty.server.udp.CodingPartyUDPServer;
import com.codingparty.texture.Spritesheets;
import com.codingparty.texture.TextureLoader;
import com.codingparty.world.World;

public class CodingPartyServerMain {

	private static CodingPartyServerMain instance;
	private CodingPartyTCPServer TCPServer;
	private CodingPartyUDPServer UDPReceivingServer;
	private CodingPartyUDPMulticastServer UDPMulticastServer;
	
	private static World world;
	private static final int TICKS_PER_SECOND = 60;
	private static final double TIME_SLICE = 1 / (double)TICKS_PER_SECOND;
	private static final float LAG_CAP = 0.15f;
	private int ticks;
//	private int frames;
	
	public CodingPartyServerMain() throws SocketException {
		TCPServer = new CodingPartyTCPServer("TCP Server", queryPublicIP(), 4406, 5000, 100);
		TCPServer.startListening();
		TCPServer.isServerConnectable();
		
		UDPReceivingServer = new CodingPartyUDPServer("UDP Receiving Server", queryPublicIP(), 4407);
		UDPReceivingServer.startRunning();
		
		UDPMulticastServer = new CodingPartyUDPMulticastServer("UDP Multicast Server", "227.214.1.225", 4408, 4409);
		UDPMulticastServer.startRunning();
	}
	
	public static void main(String[] args) {
		String path = (new File(CodingPartyServerMain.class.getProtectionDomain().getCodeSource().getLocation().getPath())).getParentFile().getPath();
		String decodedPath;
		try {
			decodedPath = URLDecoder.decode(path, "UTF-8");
			System.setProperty("org.lwjgl.librarypath", decodedPath + FileUtil.getFileSeparator(false));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		LoggerUtil.init();
		Thread.setDefaultUncaughtExceptionHandler(LoggerUtil.getInstance());
		
		try {
			instance = new CodingPartyServerMain();
		} catch (SocketException e) {
			LoggerUtil.logError(CodingPartyServerMain.class, e);
			System.exit(-1);
		}
		
		instance.run();
	}
	
	private void run() {
		LoggerUtil.logInfo(this.getClass(), "Initializing internal structure. Currently running on LWJGL " + Sys.getVersion() + ".");
		
		try {
			FileResourceTracker.init();
			Window.init(Info.NAME + " | " + Info.AUTHOR + " | "+ Info.VERSION);
			Window.buildScreen();
			CameraMatrixManager.init();
			GL.createCapabilities();
			ModelResourceManager.init();
			TextureLoader.init();
			Spritesheets.init();
			Models.buildModels();
			Levels.buildLevels();
//			TileList.initTiles();
			loop();
		} catch (Exception e) {
			LoggerUtil.logError(getClass(), e);
		} finally {
			TCPServer.stopListening();
			UDPReceivingServer.stopRunning();
			UDPMulticastServer.stopRunning();
			Window.save();
			FileResourceTracker.releaseResource(ModelResourceManager.getInstance());
			FileResourceTracker.releaseProgramResources();
			System.gc();
			System.exit(0);
		}
	}
	
	private void loop() {
		world = new World();
		world.initGameStartUp();
		CursorPosCallback.centerMouse();
//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		/////////////////////////////
		
		double lastTime;
		double currentTime;
		double deltaTime;
		double accumulatedTime = 0.0d;
		
		lastTime = GLFW.glfwGetTime();
		while (GLFW.glfwWindowShouldClose(Window.getID()) == GL11.GL_FALSE) {
			currentTime = GLFW.glfwGetTime();
			deltaTime = currentTime - lastTime;
			lastTime = currentTime;
			accumulatedTime += deltaTime;
			
			if (accumulatedTime > LAG_CAP) {
				accumulatedTime = LAG_CAP;
			}
			
			while (accumulatedTime >= TIME_SLICE) {
				accumulatedTime -= TIME_SLICE;
				GLFW.glfwPollEvents();
				update(TIME_SLICE);
			}
			render(TIME_SLICE); //TODO: Fix the lerp.
		}
	}
	
	private void update(double deltaTime) {
		world.update(deltaTime);
		
		if (ticks % 3 == 0) {
			TCPIncomingBundleManager.update();
		}
		
		if (ticks % 10 == 0) {
			UDPOutgoingPacketManager.update();
		}
		
		UDPIncomingPacketManager.update(deltaTime);
		tick();
	}
	
//	private int totalFPS, averageFPS, counter;
	
	private void tick() {
		ticks++;
		if (ticks % 60 == 0) {
//			counter++;
//			totalFPS += frames;
//			averageFPS = totalFPS / counter;
//			LoggerUtil.logInfo(getClass(), "Ticks: " + ticks + " | FPS: " + frames + " | Average FPS: " + averageFPS);
			ticks = 0;
//			frames = 0;
			
//			if (counter % 100 == 0) {
//				LoggerUtil.logInfo(getClass(), "Reset Average FPS counter.");
//				averageFPS = 0;
//				totalFPS = 0;
//				counter = 0;
//			}
		}
	}
	
	private void render(double deltaTime) {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL13.GL_MULTISAMPLE);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
//		frames++;
		world.render(deltaTime);
		GLFW.glfwSwapBuffers(Window.getID());
	}
	
	private static String queryPublicIP() {
		String ip = "localhost";
		try {
			URL whatismyip = new URL("http://ascottwebdesign.com/ip/index.php");
			LoggerUtil.logInfo(CodingPartyServerMain.class, "Trying to establish connection...");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			ip = in.readLine();
			System.out.println(ip);
			return ip;
		} catch (IOException e) {
			LoggerUtil.logError(CodingPartyServerMain.class, "Error trying to connect to ip checker. Returning localhost. \n", e);
		}
		return ip;
	}
	
	public static CodingPartyServerMain getInstance() {
		return instance;
	}
	
	public static World getWorld() {
		return world;
	}
	
	public CodingPartyTCPServer getTCPServer() {
		return TCPServer;
	}
	
	public CodingPartyUDPServer getUDPReceivingServer() {
		return UDPReceivingServer;
	}
	
	public CodingPartyUDPMulticastServer getUDPMulticastServer() {
		return UDPMulticastServer;
	}
}

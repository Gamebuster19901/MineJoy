package com.gamebuster19901.minejoy.controller;

import java.util.ArrayList;

import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import com.studiohartman.jamepad.ControllerUnpluggedException;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public enum ControllerHandler {
	INSTANCE;
	
	private ControllerManager controllerManager;
	private int activeController = 0;
	
	public void init(){
		controllerManager = new ControllerManager();
		controllerManager.initSDLGamepad();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Game ended, shutting down Minejoy and Jamepad!");
				ControllerHandler.this.controllerManager.quitSDLGamepad();
			}
		});
		vibrate(activeController, 1f, 1f, 1500);
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent e) {
		if(controllerManager.getNumControllers() > 0) {
			ControllerState state = controllerManager.getState(activeController);
			MinecraftForge.EVENT_BUS.post(new ControllerEvent(activeController, getActiveControllerState(), getActiveControllerIndex()));
		}
	}
	
	public ControllerIndex getActiveControllerIndex(){
		return controllerManager.getControllerIndex(activeController);
	}
	
	public ControllerState getActiveControllerState() {
		return controllerManager.getState(activeController);
	}
	
	public int getActiveController() {
		return activeController;
	}
	
	public void setActiveController(int index) {
		activeController = index;
	}
	
	public ControllerState getControllerState(int index) {
		return controllerManager.getState(index);
	}
	
	public ControllerIndex getControllerIndex(int index) throws ControllerUnpluggedException{
		return controllerManager.getControllerIndex(index);
	}
	
	public ArrayList<ControllerState> getAllControllerStates(){
		ArrayList<ControllerState> ret = new ArrayList<ControllerState>();
		int m = controllerManager.getNumControllers();
		for(int i = 0; i < m; i++) {
			ret.add(controllerManager.getState(i));
		}
		return ret;
	}
	
	public String getControllerName(int index) {
		try {
			return controllerManager.getControllerIndex(index).getName();
		} catch (ControllerUnpluggedException e) {
			return "Unplugged controller";
		}
	}
	
	public void vibrate(int index, float leftMagnatude, float rightMagnatude, int milliseconds){
		new Thread() {
			public void run() {
				int ms = milliseconds;
				ControllerIndex unsafe = controllerManager.getControllerIndex(index);
				if(leftMagnatude == 0 && rightMagnatude == 0) {
					if(unsafe.isVibrating() || unsafe.isConnected()) {
						unsafe.stopVibration();
					}
				}
				else {
					try {
						unsafe.startVibration(leftMagnatude, rightMagnatude);
					} catch (ControllerUnpluggedException e1) {} //while loop will fail because unsafe.isVibrating() will be false, we should swallow
					while(ms > 0 && unsafe.isVibrating()) {
						try {
							Thread.sleep(1);
							ms--;
						} catch (InterruptedException e) {
							unsafe.stopVibration();
							break;
						}
					}
					unsafe.stopVibration();
				}
			}
		}.start();

	}
	
}

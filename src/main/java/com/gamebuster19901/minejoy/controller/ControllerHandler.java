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
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent e) {
		if(controllerManager.getNumControllers() > 0) {
			ControllerState state = controllerManager.getState(activeController);
			MinecraftForge.EVENT_BUS.post(new ControllerEvent(activeController, getActiveControllerState(), getActiveControllerIndex()));
			try {
				controllerManager.getControllerIndex(activeController).startVibration(1f, 1f);
			} catch (ControllerUnpluggedException e1) {
				controllerManager.getControllerIndex(activeController).stopVibration();
				e1.printStackTrace();
			}
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
	
	public void vibrate(int index, float leftMagnatude, float rightMagnatude) throws ControllerUnpluggedException{
		ControllerIndex unsafe = controllerManager.getControllerIndex(index);
		if(leftMagnatude == 0 && rightMagnatude == 0) {
			if(unsafe.isVibrating() || unsafe.isConnected()) {
				unsafe.stopVibration();
			}
			else {
				unsafe.startVibration(leftMagnatude, rightMagnatude);
			}
		}
	}
	
}

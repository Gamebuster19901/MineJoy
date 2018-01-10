package com.gamebuster19901.minejoy.controller;

import java.util.ArrayList;
import org.lwjgl.input.Controllers;

import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public enum ControllerHandler {
	INSTANCE;
	
	private ArrayList<Controller> controllers = new ArrayList<Controller>();
	private Controller activeController = null;
	
	private boolean initialized = false;
	
	public void init(){
		if(!initialized) {
			initialized = true;
			ControllerEnvironment e = ControllerEnvironment.getDefaultEnvironment();
		
			Controller[] controllers = e.getControllers();
			for(Controller c : controllers) {
				if(c.getType().equals(Type.GAMEPAD)) {
					this.controllers.add(c);
					System.out.println(c.getName() + ": " + c.getType());
				}
			}
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	public void rescan() {
		controllers.clear();
		for(Controller c : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
			if(c.poll()) {
				if(c.getType().equals(Type.GAMEPAD)) {
					this.controllers.add(c);
					System.out.println(c.getName() + ": " + c.getType());
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent e){
		Controllers.poll();
		for(int i = 0; i < Controllers.getControllerCount(); i++) {
			System.out.println(Controllers.getController(i).getName());
		}
	}
	
	public Controller getActiveController() {
		return activeController;
	}
	
	public void setActiveController(Controller c) {
		activeController = c;
	}
	
	public ArrayList<Controller> getControllers(){
		return controllers;
	}
}

package com.gamebuster19901.minejoy.controller;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public enum ControllerHandler {
	INSTANCE;
	
	private boolean initialized = false;
	
	public void init() throws LWJGLException {
		if(!initialized) {
			initialized = true;
			Controllers.create();
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent e){
		Controllers.poll();
		for(int i = 0; i < Controllers.getControllerCount(); i++) {
			System.out.println(Controllers.getController(i).getName());
		}
	}
}

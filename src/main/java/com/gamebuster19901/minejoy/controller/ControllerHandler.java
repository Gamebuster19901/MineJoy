package com.gamebuster19901.minejoy.controller;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public enum ControllerHandler {
	INSTANCE;
	
	private boolean initialized = false;
	
	public void init() {
		if(!initialized) {
			initialized = true;
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent e){
		
	}
}

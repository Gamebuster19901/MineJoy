package com.gamebuster19901.minejoy.controller;

import org.lwjgl.input.Mouse;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public enum MouseHandler{
	INSTANCE;
	
	public void init() {
		new Thread(MouseRunnable.INSTANCE).start();
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent e){
		//System.out.println(MouseRunnable.INSTANCE.grab);
		if(MouseRunnable.INSTANCE.shouldSwitch) {
			Mouse.setGrabbed(MouseRunnable.INSTANCE.grab);
			MouseRunnable.INSTANCE.shouldSwitch = false;
		}
	}
}

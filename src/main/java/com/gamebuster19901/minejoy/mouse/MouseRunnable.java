package com.gamebuster19901.minejoy.mouse;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;

public enum MouseRunnable implements Runnable{
	INSTANCE;
	
	private volatile Minecraft mc = Minecraft.getMinecraft();
	private volatile int mouseTime = 3000;
	volatile boolean grab = true;
	volatile boolean shouldSwitch = true;

	@Override
	public void run() {
		Thread.currentThread().setName("Mouse Thread (MineJoy)");
		while(true) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				break; //just stop
			}
			if(Mouse.getDX() != 0 || Mouse.getDY() != 0) {
				mouseTime = 3000;
			}
			else if((mc.currentScreen != null)){
				if(mouseTime > 0) {
					if(Mouse.isGrabbed() && Mouse.isInsideWindow()) {
						grab = false;
						shouldSwitch = true;
					}
					mouseTime--;
				}
				else {
					if(!Mouse.isGrabbed() && Mouse.isInsideWindow()) {
						grab = true;
						shouldSwitch = true;
					}
				}
			}
		}
	}
}
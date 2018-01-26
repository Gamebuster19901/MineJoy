package com.gamebuster19901.minejoy.controller;

import java.util.NoSuchElementException;

import com.gamebuster19901.minejoy.Minejoy;
import com.gamebuster19901.minejoy.config.MineJoyConfig;
import com.gamebuster19901.minejoy.controller.layout.Layout;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class MovementInputFromOptionsMinejoy extends MovementInputFromOptions{
	
	private static  MovementInputFromOptionsMinejoy INSTANCE = new MovementInputFromOptionsMinejoy();
	
	private final Configuration config = Minejoy.getConfig();
	
	private Layout layout;
	private ControllerStateWrapper state;

	private MovementInputFromOptionsMinejoy() {
		super(Minecraft.getMinecraft().gameSettings);
		try {
			layout = Layout.getLayout(MineJoyConfig.controllerLayout);
		}
		catch(NoSuchElementException e){
			try {
				Layout.setLayout(Layout.getLayout("com.gamebuster19901.minejoy.controller.layout.Default"));
				Minejoy.getConfig().get("", "controllerLayout", "com.gamebuster19901.minejoy.controller.layout.Default").set("com.gamebuster19901.minejoy.controller.layout.Default");
				Minejoy.getConfig().save();
				e.printStackTrace();
				if(Minecraft.getMinecraft().player != null) {
					INSTANCE = this;
					Minecraft.getMinecraft().player.movementInput = this;
				}
			}
			catch(NoSuchElementException e2) {
				e2.initCause(e);
				throw new AssertionError(e2);
			}
		}
	}
	
	@Override
	public void updatePlayerMoveState() {
		if(Minecraft.getMinecraft().currentScreen != null) {
			backKeyDown = false;
			forwardKeyDown = false;
			jump = false;
			leftKeyDown = false;
			moveForward = 0;
			moveStrafe = 0;
			rightKeyDown = false;
			sneak = false;
			return;
		}
		super.updatePlayerMoveState();
		float moveStrafeController = 0f;
		float moveForwardController = 0f;

		boolean prevSneak = sneak;
		sneak = sneak ? true : state.leftStickClick;
		
		if(sneak) {
			if(Math.abs(state.leftStickY) > 0.5) {
				moveForwardController = state.leftStickY * 0.3f;
			}
			if(Math.abs(state.leftStickX) > 0.5) {
				moveStrafeController = state.leftStickX * 0.3f;
			}
		}
		else {
			if(Math.abs(state.leftStickY) > 0.5) {
				moveForwardController = state.leftStickY;
			}
			if(Math.abs(state.leftStickX) > 0.5) {
				moveStrafeController = state.leftStickX;
			}
		}
		
		if(Math.abs(moveForwardController) > moveForward) {
			moveForward = moveForwardController;
		}
		else if (!prevSneak && sneak) {
			moveForward = moveForward * 0.3f;
		}
		
		if(Math.abs(moveStrafeController) > moveStrafe) {
			moveStrafe = moveStrafeController;
		}
		else if (!prevSneak && sneak) {
			moveStrafe = moveStrafe * 0.3f;
		}
		
		jump = jump ? jump : state.a;

	}
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public void onControllerEvent(ControllerEventNoGL.Pre e) {
		if(Minecraft.getMinecraft().player != null && !(Minecraft.getMinecraft().player.movementInput instanceof MovementInputFromOptionsMinejoy)) {
			Minecraft.getMinecraft().player.movementInput = this;
		}
		this.state = e.getControllerState();
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerLoad(EntityJoinWorldEvent e) {
		if(e.getEntity() == Minecraft.getMinecraft().player) {
			EntityPlayerSP player = (EntityPlayerSP)e.getEntity();
			player.movementInput = this;
		}
	}
	
	public static final MovementInputFromOptionsMinejoy getInstance() {
		return INSTANCE;
	}
	
}

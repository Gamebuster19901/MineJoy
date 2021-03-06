package com.gamebuster19901.minejoy.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public final class MovementInputFromOptionsMinejoy extends MovementInputFromOptions{
	
	private static  MovementInputFromOptionsMinejoy INSTANCE = new MovementInputFromOptionsMinejoy();
	
	private int sprintToggleTimer = 0;
	private float lastY = 0;
	private float y = 0;

	private MovementInputFromOptionsMinejoy() {
		super(Minecraft.getMinecraft().gameSettings);
	}
	
	@Override
	public void updatePlayerMoveState() {
		if(Minecraft.getMinecraft().currentScreen != null) { //stop player from moving when a gui is open
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
		
		ControllerStateWrapper state = ControllerHandler.INSTANCE.getModifiedActiveContorllerState();
		
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
		
		if(Math.abs(moveForwardController) > Math.abs(moveForward)) {
			moveForward = moveForwardController;
		}
		else if (!prevSneak && sneak) {
			moveForward = moveForward * 0.3f;
		}
		
		if(Math.abs(moveStrafeController) > Math.abs(moveStrafe)) {
			moveStrafe = moveStrafeController;
		}
		else if (!prevSneak && sneak) {
			moveStrafe = moveStrafe * 0.3f;
		}
		jump = jump ? jump : state.a && !ControllerMouse.wasGUIJustOpen;
	}
	
	@SuppressWarnings("unused")
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onTick(ClientTickEvent e) {
		if(Minecraft.getMinecraft().player != null) {
			
			if(sprintToggleTimer > 0) {
				sprintToggleTimer--;
			}
			
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			player.movementInput = this;
			
			/*
			 * See EntityPlayerSP#OnUpdate
			 */
			
			boolean flag1 = !this.sneak;
			boolean flag2 = y >= 0.8 && lastY < 0.8;
			boolean flag4 = player.getFoodStats().getFoodLevel() > 6.0f || player.capabilities.allowFlying;
			
			if(flag1 && flag2 && flag4 && !player.isHandActive() && !player.isPotionActive(MobEffects.BLINDNESS)) {
				sprintToggleTimer += 20;
				if(sprintToggleTimer >= 25) {
					player.setSprinting(true);
				}
			}
			
			if(sprintToggleTimer > 15) {
				sprintToggleTimer = 15;
			}
		}
	}
	
	@SubscribeEvent
	public void onControllerEvent(ControllerEvent.Post e) {
		this.lastY = y;
		this.y = e.getControllerState().leftStickY;
	}
	
	public static final MovementInputFromOptionsMinejoy getInstance() {
		return INSTANCE;
	}
	
}

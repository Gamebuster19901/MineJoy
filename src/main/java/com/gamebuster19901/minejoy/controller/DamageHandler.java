package com.gamebuster19901.minejoy.controller;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public enum DamageHandler {
	INSTANCE;
	
	@SubscribeEvent
	public void onPlayerDamage(LivingHurtEvent e) {
		if(e.getEntity().equals(Minecraft.getMinecraft().player)) {
			if(e.getAmount() >= 1) {
				int milliseconds = (int) (e.getAmount() * 250);
				float intensity = (e.getAmount() / 20) * 5;
				
				if(milliseconds > 3000) {
					milliseconds = 3000;
				}
				
				if(intensity > 1f) {
					intensity = 1f;
				}
				
				if(ControllerHandler.INSTANCE.canSendControllerEvents()) {
					ControllerHandler.INSTANCE.vibrate(ControllerHandler.INSTANCE.getActiveControllerIndex().getIndex(), intensity, intensity, milliseconds);
				}
			}
		}
	}
}

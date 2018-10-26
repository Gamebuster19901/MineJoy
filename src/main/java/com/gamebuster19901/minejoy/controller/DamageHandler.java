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
				int milliseconds = (int) (e.getAmount() * 200);
				
				if(milliseconds > 3000) {
					milliseconds = 3000;
				}
				if(ControllerHandler.INSTANCE.canSendControllerEvents()) {
					ControllerHandler.INSTANCE.vibrate(ControllerHandler.INSTANCE.getActiveControllerIndex().getIndex(), 1f, 1f, milliseconds);
				}
			}
		}
	}
}

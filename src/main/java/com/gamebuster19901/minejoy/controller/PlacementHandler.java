package com.gamebuster19901.minejoy.controller;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public enum PlacementHandler {

	INSTANCE;
	
	public static final Field rightClickTimer = ReflectionHelper.findField(Minecraft.class, "rightClickDelayTimer", "field_71467_ac");
	
	@SubscribeEvent
	public void onTick(ClientTickEvent e) {
		if(e.phase.equals(Phase.END)) {
			if(ControllerHandler.INSTANCE.canSendControllerEvents()) {
				try {
					rightClickTimer.set(Minecraft.getMinecraft(), 6);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					throw new AssertionError();
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		if(e.phase == e.phase.END) {
			if(e.player == Minecraft.getMinecraft().player) {
				BlockPos b = new BlockPos(e.player.lastTickPosX + 0.5, e.player.lastTickPosY + 0.5, e.player.lastTickPosZ + 0.5);
				if(!b.equals(e.player.getPosition())) {
					MinecraftForge.EVENT_BUS.post(new MoveOneMeterEvent(e.player));
				}
			}
		}
	}
	
	public static class MoveOneMeterEvent extends Event{
		EntityPlayer player;
		
		public MoveOneMeterEvent(EntityPlayer player) {
			this.player = player;
		}
		
		public EntityPlayer getPlayer() {
			return player;
		}
	}
	
}

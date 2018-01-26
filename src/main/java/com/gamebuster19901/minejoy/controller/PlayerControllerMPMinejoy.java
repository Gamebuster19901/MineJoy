package com.gamebuster19901.minejoy.controller;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class PlayerControllerMPMinejoy extends PlayerControllerMP{

	@Deprecated
	public static final PlayerControllerMPMinejoy REGISTRY_INSTANCE = new PlayerControllerMPMinejoy(Minecraft.getMinecraft(), null);
	
	private static final Field BLOCK = ReflectionHelper.findField(PlayerControllerMP.class, "currentBlock", "field_178895_c");
	
	private boolean isRightTriggerDown = false;
	
	private PlayerControllerMPMinejoy(Minecraft mcIn, NetHandlerPlayClient netHandler) {
		super(mcIn, netHandler);
	}
	
	@Override
    /**
     * Resets current block damage
     * 
     * Overwriting the vanilla resetBlockRemoving method because vanilla calls it every tick if the keybindAttack
     * is not pressed. It is technically not pressed if the right trigger is pulled even though we still want the
     * block to get destroyed, so we must only allow vanilla to call this if the right trigger is not pressed.
     */
    public void resetBlockRemoving()
    {
    	if (ControllerHandler.INSTANCE.getActiveControllerState().rightTrigger < 0.5) {
    		super.resetBlockRemoving();
    	}
    	else {
    		 RayTraceResult trace = Minecraft.getMinecraft().objectMouseOver;
    		 if(trace.typeOfHit != RayTraceResult.Type.BLOCK) {
    			 super.resetBlockRemoving();
    		 }
    		 else {
    			 try {
					if (!(BLOCK.get(this).equals(trace.getBlockPos())) || trace.entityHit != null) {
						 super.resetBlockRemoving();
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new AssertionError(e);
				}
    		}
    	}
    }
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerLoad(EntityJoinWorldEvent e) {
		if(e.getEntity() == Minecraft.getMinecraft().player) {
			EntityPlayerSP player = (EntityPlayerSP)e.getEntity();
			Minecraft.getMinecraft().playerController = getNewInstance();
		}
	}
	
	private PlayerControllerMPMinejoy getNewInstance() {
		return new PlayerControllerMPMinejoy(Minecraft.getMinecraft(), Minecraft.getMinecraft().getConnection());
	}

}

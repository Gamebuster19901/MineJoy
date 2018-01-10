package com.gamebuster19901.minejoy.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public enum ControlGUIHandler {
	INSTANCE;
	
	@SubscribeEvent
	public void onControlsOpen(InitGuiEvent.Post e) {
		if(e.getGui() instanceof GuiControls) {
			for(int i = e.getButtonList().size() - 1; i > 0; i--) {
				GuiButton b = e.getButtonList().get(i);
				if(b.displayString.contains(new TextComponentTranslation("options.sensitivity").getUnformattedComponentText())) {
					e.getButtonList().remove(i);
					e.getButtonList().add(i, new GuiButton(i, e.getGui().width / 2 - 155 + i % 2 * 160, 18 + 24 * (i - 3), 150, 20, new TextComponentTranslation("options.mouse").getUnformattedComponentText()));
				}
				else if (b.displayString.contains(new TextComponentTranslation("options.invertMouse").getUnformattedComponentText())) {
					e.getButtonList().remove(i);
					e.getButtonList().add(i, new GuiButton(i, e.getGui().width / 2 - 155 + i % 2 * 160, 18 + 24 * (i - 2), 150, 20, new TextComponentTranslation("options.joy").getUnformattedComponentText()));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onControlsPress(ActionPerformedEvent.Pre e) {
		if(e.getButton().displayString.equals(new TextComponentTranslation("options.joy").getUnformattedComponentText())) {
			e.setCanceled(true);
			e.getButton().playPressSound(Minecraft.getMinecraft().getSoundHandler());
			System.out.println("Controller options!");
		}
		else if (e.getButton().displayString.equals(new TextComponentTranslation("options.mouse").getUnformattedComponentText())) {
			e.setCanceled(true);
			e.getButton().playPressSound(Minecraft.getMinecraft().getSoundHandler());
			System.out.println("Mouse Options!");
		}
	}
}

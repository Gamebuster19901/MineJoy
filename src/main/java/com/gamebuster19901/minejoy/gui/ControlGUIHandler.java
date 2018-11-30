package com.gamebuster19901.minejoy.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.resources.I18n;
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
				if(b.displayString.contains(I18n.format("options.minejoy.sensitivity"))) {
					e.getButtonList().remove(i);
					e.getButtonList().add(i, new GuiButton(i, e.getGui().width / 2 - 155 + i % 2 * 160, 18 + 24 * (i - 3), 150, 20, I18n.format("options.minejoy.mouse")));
				}
				else if (b.displayString.contains(I18n.format("options.minejoy.invertMouse"))) {
					e.getButtonList().remove(i);
					e.getButtonList().add(i, new GuiButton(i, e.getGui().width / 2 - 155 + i % 2 * 160, 18 + 24 * (i - 2), 150, 20, I18n.format("options.minejoy.controller")));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onControlsPress(ActionPerformedEvent.Pre e) {
		if(e.getButton().displayString.contains((I18n.format("options.minejoy.controller")))) {
			e.setCanceled(true);
			e.getButton().playPressSound(Minecraft.getMinecraft().getSoundHandler());
			Minecraft.getMinecraft().displayGuiScreen(new GuiControllerOptions(Minecraft.getMinecraft().currentScreen));
		}
		else if (e.getButton().displayString.equals(I18n.format("options.minejoy.mouse"))) {
			e.setCanceled(true);
			e.getButton().playPressSound(Minecraft.getMinecraft().getSoundHandler());
			Minecraft.getMinecraft().displayGuiScreen(new GuiMouseOptions());
		}
	}
}

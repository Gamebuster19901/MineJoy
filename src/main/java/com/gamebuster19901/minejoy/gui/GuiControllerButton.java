package com.gamebuster19901.minejoy.gui;

import com.gamebuster19901.minejoy.controller.ControllerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiControllerButton extends GuiButtonExt{
	private int controller;
	private String defaultTextString = this.displayString;
	
	public GuiControllerButton(int id, int index) {
		super(id, 0, 0, 260, 20, ControllerHandler.INSTANCE.getControllerName(index) + " (" + index + ")");
		this.controller = index;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		boolean active = controller == (ControllerHandler.INSTANCE.getActiveController());
		boolean enabled = ControllerHandler.INSTANCE.getControllerState(controller).isConnected;
		if(!active && enabled) {
			this.displayString = defaultTextString;
		}
		else if (!active && !enabled) {
			this.displayString = TextFormatting.RED + defaultTextString;
		}
		else if(active && enabled) {
			this.displayString = TextFormatting.DARK_GREEN + "" + TextFormatting.BOLD + defaultTextString;
		}
		else if (active && !enabled) {
			this.displayString = TextFormatting.RED + "" + TextFormatting.BOLD + defaultTextString;
		}
		else {
			throw new AssertionError();
		}
		super.drawButton(mc, mouseX, mouseY, partialTicks);
	}
	
	public int getController() {
		return controller;
	}

}

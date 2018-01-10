package com.gamebuster19901.minejoy.gui;

import com.gamebuster19901.minejoy.controller.ControllerHandler;

import net.java.games.input.Controller;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiControllerButton extends GuiButtonExt{
	private Controller controller;
	private String defaultTextString = this.displayString;
	
	public GuiControllerButton(int id, Controller c) {
		super(id, 0, 0, 260, 20, c.getName() + " (" + c.getPortNumber() + ")");
		this.controller = c;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		boolean active = controller.equals(ControllerHandler.INSTANCE.getActiveController());
		boolean enabled = controller.poll();
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
	
	public Controller getController() {
		return controller;
	}

}

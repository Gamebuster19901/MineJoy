package com.gamebuster19901.minejoy.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.resources.I18n;

public class GuiPossibleModIncompatability extends GuiScreen{
	/*
	 * Classes are added to known incompatibilities if they are not added to the compatible class list or overwritable class list during preinit
	 * 
	 * Continuing to run after an incompatible class has replaced mc.playercontroller is undefined behavior, and will probably break things. The best
	 * thing to do is to create another PlayerControllerMP that allows Minejoy and the other mod(s) to work simultaneously, and add it to compatibleControllers
	 */
	
	public static ArrayList<Class<? extends PlayerControllerMP>> knownIncompatabilities = new ArrayList<Class<? extends PlayerControllerMP>>();
	
	/*
	 * These controllers should be added during preinit. These controllers have defined behaviour and will work with Minejoy. They may possibly allow
	 * other mods and Minejoy to work simultaneously. Minejoy will not overwrite these controllers.
	 * 
	 * Contains PlayerControllerMPMinejoy by default
	 */
	public static ArrayList<Class<? extends PlayerControllerMP>> compatibleControllers = new ArrayList<Class<? extends PlayerControllerMP>>();
	
	/*
	 * These controllers are controllers that minejoy has full permission to overwrite in mc.playercontroller, only add a controller here if minejoy can
	 * safely overwrite the controller with PlayerControllerMPMinejoy without causing bugs; for example overriding the vanilla controller.
	 * 
	 * Contains PlayerControllerMP by default
	 */
	public static ArrayList<Class<? extends PlayerControllerMP>> overwriteableControllers = new ArrayList<Class<? extends PlayerControllerMP>>();
	private String message = "";
	
	public GuiPossibleModIncompatability(Class c) {
		message = c.getName();
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.addButton(new GuiButton(0, this.width / 2 - 32 - 100, this.height - 32, 100, 20, I18n.format("minejoy.gui.incompatiblemodsdetected.acknowledge")));
		this.addButton(new GuiButton(1, this.width / 2 + 32, this.height - 32, 100, 20, I18n.format("menu.quit")));

	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawBackground(0);
		for(GuiButton b : this.buttonList) {
			b.drawButton(mc, mouseX, mouseY, partialTicks);
		}
		this.fontRenderer.drawSplitString((I18n.format("minejoy.gui.incompatiblemodsdetected.description")), 0,0, width, 0xffffff);
		this.fontRenderer.drawString(message, 0,32, 0xffffff);
		final int fontHeight = this.fontRenderer.FONT_HEIGHT;
		final int textHeight = this.fontRenderer.getWordWrappedHeight(I18n.format("minejoy.gui.incompatiblemodsdetected.warning"), width);
		this.fontRenderer.drawSplitString(I18n.format("minejoy.gui.incompatiblemodsdetected.warning"), 0, this.height - 32 - (textHeight * (textHeight / fontHeight)), width, 0x0fffff);
		
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		if(button.id == 0) {
			mc.displayGuiScreen(null);
		}
		if(button.id == 1) {
			mc.shutdown();
		}
	}
}

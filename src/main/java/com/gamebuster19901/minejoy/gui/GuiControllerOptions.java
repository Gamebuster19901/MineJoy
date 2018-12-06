package com.gamebuster19901.minejoy.gui;

import java.io.IOException;

import com.gamebuster19901.minejoy.gui.lists.ControllerOptionsList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiControllerOptions extends GuiScreen{
	private String title = I18n.format("options.minejoy.layout");
	private GuiScreen parent;
	private ControllerOptionsList optionsList;
	
	public GuiControllerOptions(GuiScreen parent) {
		this.parent = parent;
	}
	
	public void initGui() {
		int i = 0;
		
		this.buttonList.add(new GuiButtonExt(i++, this.width / 2 - 100, this.height - 20, I18n.format("gui.done")));
		
		changed();
	}
	
	private void changed() {
		this.optionsList = new ControllerOptionsList(mc, 200, this.height, null);
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawBackground(0);
		
		this.drawString(this.fontRenderer, title, this.width / 2 - this.fontRenderer.getStringWidth(title) / 2, 5, 0xffffff);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void actionPerformed(GuiButton b) {
		if(b.id == 0) {
			exit();
		}
	}
	
	@Override
	public void keyTyped(char c, int keycode) {
		if(keycode == 1) {
			exit();
		}
	}
	
	private void exit() {
		Minecraft.getMinecraft().displayGuiScreen(parent);
	}
}

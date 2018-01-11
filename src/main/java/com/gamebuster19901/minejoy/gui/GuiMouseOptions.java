package com.gamebuster19901.minejoy.gui;

import net.minecraft.client.gui.GuiScreen;

public class GuiMouseOptions extends GuiScreen{
	
	private final GuiScreen parent;
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

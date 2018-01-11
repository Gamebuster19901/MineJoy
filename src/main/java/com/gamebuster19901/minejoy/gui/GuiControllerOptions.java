package com.gamebuster19901.minejoy.gui;

import com.gamebuster19901.minejoy.controller.ControllerHandler;

import net.java.games.input.Controller;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiControllerOptions extends GuiScreen{
	private int scroll = 0;
	private String title;
	private GuiScreen parent;
	
	public GuiControllerOptions(GuiScreen parent, String title) {
		this.title = title;
		this.parent = parent;
	}
	
	public void initGui() {
		int i = 0;
		ControllerHandler.INSTANCE.rescan();
		if(scroll > ControllerHandler.INSTANCE.getControllers().size()) {
			scroll = ControllerHandler.INSTANCE.getControllers().size();
		}
		for(Controller c : ControllerHandler.INSTANCE.getControllers()) {
			this.buttonList.add(new GuiControllerButton(i++, c));
		}
		
		this.buttonList.add(new GuiButtonExt(i, 16, 40, "" + (char)0x25B2));
		this.buttonList.get(i++).visible = false;
		
		this.buttonList.add(new GuiButtonExt(i, 16, this.height - 40, "" + (char)0x25BC));
		this.buttonList.get(i++).visible = false;
		
		this.buttonList.add(new GuiButtonExt(i++, this.width / 2 - 100, this.height - 20, I18n.format("gui.done")));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawBackground(0);
		boolean isTopButtonVisible = false;
		boolean isBottomButtonVisible = false;
		if(buttonList.size() >= 4) { //has at least one controller
			isTopButtonVisible = buttonList.get(0).y > this.height - 60;
			isBottomButtonVisible = buttonList.get(buttonList.size() - 4).y < 60;
			for(int i = 0; i < buttonList.size() - 3; i++) {
				GuiButton b = buttonList.get(i);
				b.y = 60 + ((i + scroll) * 20);
				b.visible = b.y >= 60 && b.y <= this.height - 60;
			}
		}
		
		buttonList.get(buttonList.size() - 3).visible = isTopButtonVisible;
		buttonList.get(buttonList.size() - 2).visible = isBottomButtonVisible;
		
		this.drawString(this.fontRenderer, title, this.width / 2 - this.fontRenderer.getStringWidth(title) / 2, 5, 0xffffff);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void actionPerformed(GuiButton b) {
		if(b.id == buttonList.size() - 3 && b.visible) {
			scroll--;
		}
		else if(b.id == buttonList.size() - 2 && b.visible) {
			scroll++;
		}
		else if (b.id == buttonList.size() - 1){
			Minecraft.getMinecraft().displayGuiScreen(parent);
		}
		else {
			if(b instanceof GuiControllerButton) {
				GuiControllerButton b2 = (GuiControllerButton)b;
				if(b2.getController().poll()) {
					ControllerHandler.INSTANCE.setActiveController(b2.getController());
				}
			}
		}
	}
}
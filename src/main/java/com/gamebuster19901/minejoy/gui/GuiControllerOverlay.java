package com.gamebuster19901.minejoy.gui;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import com.gamebuster19901.minejoy.controller.ControllerHandler;
import com.gamebuster19901.minejoy.controller.ControllerStateWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class GuiControllerOverlay extends Gui{
	private static final ResourceLocation TEX = new ResourceLocation("minejoy:textures/gui/overlay.png");
	public static final Field BUTTON_FIELD = ReflectionHelper.findField(GuiScreen.class, "buttonList", "field_146292_n");
	public static final GuiControllerOverlay INSTANCE = new GuiControllerOverlay();
	
	private GuiControllerOverlay() {}
	
	@SubscribeEvent
	public void onGuiRender(GuiScreenEvent.DrawScreenEvent.Post e) throws IllegalArgumentException, IllegalAccessException {
		GuiScreen gui = e.getGui();
		GuiButton mousedButton = null;
		boolean hasButtons = false;
		ArrayList<GuiButton> buttons = (ArrayList<GuiButton>)BUTTON_FIELD.get(gui);
		for(GuiButton b : buttons) {
			hasButtons = true;
			if(b.isMouseOver() && b.visible && b.enabled && Mouse.isGrabbed()) {
				mousedButton = b;
				this.drawBorder(b.x, b.y, b.width, b.height);
				break;
			}
		}
		
		ControllerStateWrapper state = ControllerHandler.INSTANCE.getActiveControllerState();
		
		if(state.dpadUpJustPressed || (state.leftStickAngle > 45 && state.leftStickAngle < 135 && state.leftStickY > 0.5f)){
			System.out.println("Up just pressed");
		}
	}
	
	private void drawBorder(int x, int y, int width, int height) {
		int color = 0xffffff;
		new RenderButton(x, y, width, height).drawButton(Minecraft.getMinecraft(), -1, -1, 0);
	}
	
	private class RenderButton extends GuiButtonExt{
		public RenderButton(int x, int y, int width, int height) {
			super(0, x, y, width, height, "");
		}
		
		@Override
	    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial)
	    {
	        if (this.visible)
	        {
	            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
	            int k = 2;
	            GuiUtils.drawContinuousTexturedBox(TEX, this.x, this.y, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, 0);
	            this.mouseDragged(mc, mouseX, mouseY);
	            int color = 14737632;

	            if (packedFGColour != 0)
	            {
	                color = packedFGColour;
	            }
	            else if (!this.enabled)
	            {
	                color = 10526880;
	            }
	            else if (this.hovered)
	            {
	                color = 16777120;
	            }

	            String buttonText = this.displayString;
	            int strWidth = mc.fontRenderer.getStringWidth(buttonText);
	            int ellipsisWidth = mc.fontRenderer.getStringWidth("...");

	            if (strWidth > width - 6 && strWidth > ellipsisWidth)
	                buttonText = mc.fontRenderer.trimStringToWidth(buttonText, width - 6 - ellipsisWidth).trim() + "...";

	            this.drawCenteredString(mc.fontRenderer, buttonText, this.x + this.width / 2, this.y + (this.height - 8) / 2, color);
	        }
	    }
	}
}

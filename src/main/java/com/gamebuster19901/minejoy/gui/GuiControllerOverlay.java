package com.gamebuster19901.minejoy.gui;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.gamebuster19901.minejoy.controller.ControllerHandler;
import com.gamebuster19901.minejoy.controller.ControllerStateWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class GuiControllerOverlay extends Gui{
	private static final ResourceLocation TEX = new ResourceLocation("minejoy:textures/gui/overlay.png");
	public static final Field BUTTON_FIELD = ReflectionHelper.findField(GuiScreen.class, "buttonList", "field_146292_n");
	public static final Method MOUSE_PRESS_METHOD = ReflectionHelper.findMethod(GuiScreen.class, "mouseClicked", "func_73864_a", int.class, int.class, int.class);
	public static final Method MOUSE_RELEASE_METHOD = ReflectionHelper.findMethod(GuiScreen.class, "mouseReleased", "func_146286_b", int.class, int.class, int.class);
	public static final Method KEY_TYPE_METHOD = ReflectionHelper.findMethod(GuiScreen.class, "keyTyped", "func_73869_a", char.class, int.class);
	public static final GuiControllerOverlay INSTANCE = new GuiControllerOverlay();
	
	private static Minecraft mc = Minecraft.getMinecraft();
	private static ControllerStateWrapper lastState = ControllerStateWrapper.DISCONNECTED_CONTROLLER;
	
	private GuiControllerOverlay() {}
	
	@SubscribeEvent
	public void onGuiRender(GuiScreenEvent.DrawScreenEvent.Post e) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
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
		
		if(Mouse.isButtonDown(1)) {
			System.out.println(Mouse.getX() + ", " + Mouse.getY());
		}
		
		if(mousedButton != null) {
			drawBorder(mousedButton.x, mousedButton.y, mousedButton.width, mousedButton.height);
			
			GuiButton closestButton = mousedButton;
			ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
			if(state.leftStickMagnitude > 0.5 && lastState.leftStickMagnitude <= 0.5) {
				Facing stickFace = Facing.getFacing(state.leftStickAngle);
				if(stickFace == Facing.getFacing(lastState.leftStickAngle)) {
					switch(stickFace) {
						case DOWN:
							closestButton = getClosestButton(mousedButton, buttons, Facing.DOWN);
							break;
						case LEFT:
							closestButton = getClosestButton(mousedButton, buttons, Facing.LEFT);
							break;
						case RIGHT:
							closestButton = getClosestButton(mousedButton, buttons, Facing.RIGHT);
							break;
						case UP:
							closestButton = getClosestButton(mousedButton, buttons, Facing.UP);
							break;
						default:
							throw new AssertionError();
					}
				}
			}
			else {
				if(state.dpadUp && !lastState.dpadUp) {
					closestButton = getClosestButton(mousedButton, buttons, Facing.UP);
				}
				if(state.dpadDown && !lastState.dpadDown) {
					closestButton = getClosestButton(mousedButton, buttons, Facing.DOWN);
				}
				if(state.dpadLeft && !lastState.dpadLeft) {
					closestButton = getClosestButton(mousedButton, buttons, Facing.LEFT);
				}
				if(state.dpadRight && !lastState.dpadRight) {
					closestButton = getClosestButton(mousedButton, buttons, Facing.RIGHT);
				}
			}
			setMousePosition(closestButton.x + closestButton.width / 2, closestButton.y + closestButton.height / 2, gui.width, gui.height, res);
			mousedButton = closestButton;
			
			if(state.a) {
				MOUSE_PRESS_METHOD.invoke(gui, mousedButton.x, mousedButton.y, 0);
				MOUSE_RELEASE_METHOD.invoke(gui, mousedButton.x, mousedButton.y, 0);
			}
			if(state.b) {
				KEY_TYPE_METHOD.invoke(gui, (char)0x1B, 1);
			}
		}
		
		lastState = state;
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
	
	private GuiButton getClosestButton(GuiButton center, List<GuiButton> buttons, Facing direction){
		List<GuiButton>clonedButtons = new ArrayList<GuiButton>(buttons);
		clonedButtons.remove(center);
		
		GuiButton closest = center;
		int distance = Integer.MAX_VALUE;
		for(GuiButton b : clonedButtons) {
			switch(direction) {
				case DOWN:
					if(b.y <= center.y) {
						System.out.println(b.displayString + "is not valid");
						continue;
					}
					break;
				case LEFT:
					if(b.x >= center.x) {
						System.out.println(b.displayString + "is not valid");
						continue;
					}
					break;
				case RIGHT:
					if(b.x <= center.x) {
						System.out.println(b.displayString + "is not valid");
						continue;
					}
					break;
				case UP:
					if(b.y >= center.y) {
						System.out.println(b.displayString + "is not valid");
						continue;
					}
					break;
				default:
					throw new AssertionError();
			}
			if(quickDistanceFrom(center, b) < distance) {
				distance = quickDistanceFrom(center, b);
				closest = b;
			}
			else {
				System.out.println(quickDistanceFrom(center, b) + " ! < " + distance);
			}
		}
		return closest;
	}
	
	private int quickDistanceFrom(GuiButton b1, GuiButton b2) {
		int x =  b1.x - b2.x;
		int y = b1.y - b2.y;
		return (x * x) + (y * y);
	}
	
	private void setMousePosition(int x, int y, int guiWidth, int guiHeight, ScaledResolution res) {
        int i = (this.mc.displayWidth * x / guiWidth);
        int j = (this.mc.displayHeight - 1 - (this.mc.displayHeight * y / guiHeight));
        Mouse.setCursorPosition(i, j);
	}
	
	private enum Facing{
		UP,
		RIGHT,
		DOWN,
		LEFT;
		
		public static boolean isUp(float angle) {
			angle = Math.abs(angle);
			return angle >= 45 && angle <= 135;
		}
		public static boolean isDown(float angle) {
			angle = Math.abs(angle);
			return angle >= 225 && angle <= 315;
		}
		public static boolean isLeft(float angle) {
			angle = Math.abs(angle);
			return angle > 135 && angle < 225;
		}
		public static boolean isRight(float angle) {
			angle = Math.abs(angle);
			return (angle > 315 && angle <= 360) || (angle >= 0 && angle < 45);
		}
		
		public static Facing getFacing(float angle) {
			if(isUp(angle)) {
				return UP;
			}
			if(isDown(angle)) {
				return DOWN;
			}
			if(isLeft(angle)) {
				return LEFT;
			}
			if(isRight(angle)) {
				return RIGHT;
			}
			throw new IndexOutOfBoundsException("angle must be between 0 and 360 " + angle);
		}
	}
}

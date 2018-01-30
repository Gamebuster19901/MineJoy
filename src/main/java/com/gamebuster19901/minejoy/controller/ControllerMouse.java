package com.gamebuster19901.minejoy.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public enum ControllerMouse{
	
	INSTANCE;
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	public static final Field MOVEMENT_FIELD = ReflectionHelper.findField(EntityPlayerSP.class, "movementInput", "field_71158_b");
	public static final Field BUTTON_FIELD = ReflectionHelper.findField(GuiScreen.class, "buttonList", "field_146292_n");
	
	public static final Method SEND_BLOCK_CLICK_TO_CONTROLLER_METHOD = ReflectionHelper.findMethod(Minecraft.class, "sendClickBlockToController", "func_147115_a", boolean.class);
	public static final Method CLICK_MOUSE_METHOD = ReflectionHelper.findMethod(Minecraft.class, "clickMouse", "func_147116_af");
	public static final Method RIGHT_CLICK_MOUSE_METHOD = ReflectionHelper.findMethod(Minecraft.class, "rightClickMouse", "func_147121_ag");
	public static final Method MOUSE_PRESS_METHOD = ReflectionHelper.findMethod(GuiScreen.class, "mouseClicked", "func_73864_a", int.class, int.class, int.class);
	public static final Method MOUSE_DRAG_METHOD = ReflectionHelper.findMethod(GuiScreen.class, "mouseClickMove", "func_146273_a", int.class, int.class, int.class, long.class);
 	public static final Method MOUSE_RELEASE_METHOD = ReflectionHelper.findMethod(GuiScreen.class, "mouseReleased", "func_146286_b", int.class, int.class, int.class);
 	public static final Method KEY_TYPE_METHOD = ReflectionHelper.findMethod(GuiScreen.class, "keyTyped", "func_73869_a", char.class, int.class);
 	public static final Method SLOT_CLICKED_METHOD = ReflectionHelper.findMethod(GuiContainer.class, "handleMouseClick", "func_184098_a", Slot.class, int.class, int.class, ClickType.class);
 	
 	private float deltaX = 0;
 	private float deltaY = 0;
 	
 	@SubscribeEvent
 	public void onControllerEvent(ControllerEventNoGL.Pre e) throws IllegalArgumentException, IllegalAccessException{
		int mouseX = Mouse.getEventX();
		int mouseY = Mouse.getEventY();
		ControllerStateWrapper state = e.getControllerState();
		
		GuiScreen gui = mc.currentScreen;
		EntityPlayer player = mc.player;
		
		
		if(state.leftStickMagnitude > 0.3) {
			
			if(gui != null) {
				deltaX += state.leftStickX * state.leftStickMagnitude;
				deltaY += state.leftStickY * state.leftStickMagnitude;
				while(deltaX > 1 || deltaX < -1) {
					if(deltaX > 1) {
						Mouse.setCursorPosition(Mouse.getX() + 1, Mouse.getY());
						deltaX--;
					}
					else if (deltaX < -1){
						Mouse.setCursorPosition(Mouse.getX() - 1, Mouse.getY());
						deltaX++;
					}
				}
				
				while(deltaY > 1 || deltaY < -1) {
					if(deltaY > 1) {
						Mouse.setCursorPosition(Mouse.getX(), Mouse.getY() + 1);
						deltaY--;
					}
					else if (deltaY < -1) {
						Mouse.setCursorPosition(Mouse.getX(), Mouse.getY() - 1);
						deltaY++;
					}
				}
			}
		}
		
		if(player != null && gui == null) {
			if(state.rightStickMagnitude > 0.3) {
				if(Math.abs(player.rotationPitch + state.rightStickY) < 90) {
					player.rotationPitch = player.rotationPitch + state.rightStickY * state.rightStickMagnitude * 0.25f;
				}
				player.rotationYaw = player.rotationYaw + state.rightStickX * state.rightStickMagnitude * 0.25f;
			}
			
			KeyBinding bind = mc.gameSettings.keyBindInventory;
			if(state.xJustPressed) {
				bind.setKeyBindState(bind.getKeyCode(), true);
			}
			else {
				bind.setKeyBindState(bind.getKeyCode(), false);
			}
		}
 	}
 	
 	private BlockPos prevLoc;
 	private int rightTriggerCooldown = 0;
 	private int leftTriggerCooldown = 0;
 	private boolean prevA = false;
 	private boolean prevX = false;
 	private float prevRightTrigger = 0f;
 	
 	@SubscribeEvent
	public void onControllerEvent(ControllerEvent.Post e) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		int mouseX = Mouse.getEventX();
		int mouseY = Mouse.getEventY();
		ControllerStateWrapper state = e.getControllerState();
		
		GuiScreen gui = mc.currentScreen;
		EntityPlayer player = mc.player;
		RayTraceResult rayTrace = null;
		
		if(gui != null) {
			
			List<GuiButton> buttons = (List<GuiButton>)BUTTON_FIELD.get(gui);
			GuiButton mousedButton = null;
			
			for(GuiButton b : buttons) {
				if(b.isMouseOver() && b.visible && b.enabled) {
					mousedButton = b;
					break;
				}
			}
			
			boolean hasItem = player != null ? !player.inventory.getItemStack().isEmpty() : false;

			if(state.aJustPressed) {
				MOUSE_PRESS_METHOD.invoke(gui, getMouseX(gui), getMouseY(gui), 0);
				System.out.println("pressed a");
			}
			else if(!state.a && prevA) {
				MOUSE_RELEASE_METHOD.invoke(gui, getMouseX(gui), getMouseY(gui), 0);
				System.out.println("released a");
			}
			else if (state.a && prevA) {
				MOUSE_DRAG_METHOD.invoke(gui, getMouseX(gui), getMouseY(gui), 0, 0l);
				System.out.println("drag a");
			}
			
			if(state.xJustPressed) {
				MOUSE_PRESS_METHOD.invoke(gui, getMouseX(gui), getMouseY(gui), 1);
				System.out.println("pressed x");
			}
			else if (!state.x && prevX) {
				MOUSE_RELEASE_METHOD.invoke(gui, getMouseX(gui), getMouseY(gui), 1);
				System.out.println("released x");
			}
			else if(state.x && prevX) {
				MOUSE_DRAG_METHOD.invoke(gui, getMouseX(gui), getMouseY(gui), 1, 0l);
			}
			
			if(state.yJustPressed) {
				if(gui instanceof GuiContainer) {
					GuiContainer guicontainer = (GuiContainer) gui;
					Slot slot = guicontainer.getSlotUnderMouse();
					if(slot != null) {
						if(!MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.MouseInputEvent(gui))) {
							SLOT_CLICKED_METHOD.invoke(guicontainer, slot, slot.getSlotIndex(), 0, ClickType.QUICK_MOVE);
						}
					}
				}
			}
			
			if(state.bJustPressed || state.backJustPressed) {
				KEY_TYPE_METHOD.invoke(gui, (char)0x1B, 1);
				System.out.println("pressed b");
			}
		}
		else {
			if(player != null) {
				BlockPos location = mc.player.getPosition();
				
				if(state.bJustPressed) {
					player.dropItem(false);
				}
				
				if(state.xJustPressed) {
					Minecraft.getMinecraft().displayGuiScreen(new GuiInventory(player));
				}
				
				if(state.rbJustPressed) {
					if(mc.player.inventory.currentItem < 8) {
						mc.player.inventory.currentItem++;
					}
					else {
						mc.player.inventory.currentItem = 0;
					}
				}
				
				if(state.lbJustPressed) {
					if(mc.player.inventory.currentItem > 0) {
						mc.player.inventory.currentItem--;
					}
					else {
						mc.player.inventory.currentItem = 8;
					}
				}
				
				if((state.rightTrigger > 0.5 && prevRightTrigger <= 0.5)) {
					CLICK_MOUSE_METHOD.invoke(mc);
				}
				
				if(state.leftTrigger > 0.5 && (leftTriggerCooldown == 0)) {
					RIGHT_CLICK_MOUSE_METHOD.invoke(mc);
					leftTriggerCooldown = 40;
					prevLoc = location;
				}
				if(prevLoc != null) {
					if(mc.objectMouseOver.typeOfHit == Type.BLOCK) {
						if(player.getActiveItemStack().getItemUseAction().equals(EnumAction.NONE)) {
							if (state.leftTrigger > 0.5 && location.getDistance(prevLoc.getX(), prevLoc.getY(), prevLoc.getZ()) >= 1 && leftTriggerCooldown < 40) {
								RIGHT_CLICK_MOUSE_METHOD.invoke(mc);
								leftTriggerCooldown = 40;
								prevLoc = location;
							}
						}
					}
				}
				else {
					prevLoc = location;
				}
			}
		}
		if(player != null && mc.objectMouseOver != null) {
			if(mc.objectMouseOver.typeOfHit == Type.BLOCK) {
				if(rightTriggerCooldown == 0 && mc.currentScreen == null && state.rightTrigger > 0.5 && mc.inGameHasFocus) {
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindAdvancements.getKeyCode(), true);
					SEND_BLOCK_CLICK_TO_CONTROLLER_METHOD.invoke(mc, true);
					rightTriggerCooldown = 2;
				}
			}
		}
		prevA = state.a;
		prevX = state.x;
		if(rightTriggerCooldown > 0) {
			rightTriggerCooldown--;
		}
		if(leftTriggerCooldown > 0) {
			leftTriggerCooldown--;
		}
		prevRightTrigger = state.rightTrigger;
	}
	
	private int getMouseX(GuiScreen gui) {
        return Mouse.getX() * gui.width / this.mc.displayWidth;
	}
	
	private int getMouseY(GuiScreen gui) {
		return gui.height - Mouse.getY() * gui.height / this.mc.displayHeight - 1;
	}	
}

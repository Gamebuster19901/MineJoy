package com.gamebuster19901.minejoy.controller;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.gamebuster19901.minejoy.controller.PlacementHandler.MoveOneMeterEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public enum ControllerMouse{
	
	INSTANCE;
	
	private static final int TOP;
	private static final int LEFT;
	private static final Method getX; 
	private static final Method getY;
	
	private static final Object display_impl;
	
	private static final Robot ROBOT;
	
	private static final Method click_mouse;
	private static final Method middle_click_mouse;
	private static final Method right_click_mouse;
	
	static {
		
		Class<?> DISPLAY_IMPLEMENTATION_CLASS;
		
		try {
			DISPLAY_IMPLEMENTATION_CLASS = Class.forName("org.lwjgl.opengl.DisplayImplementation") ;
		} catch (ClassNotFoundException e1) {
			throw new AssertionError(e1);
		}
		
		getX = ReflectionHelper.findMethod(DISPLAY_IMPLEMENTATION_CLASS, "getX", "getX");
		getY = ReflectionHelper.findMethod(DISPLAY_IMPLEMENTATION_CLASS, "getY", "getY");
		click_mouse = ReflectionHelper.findMethod(Minecraft.class, "clickMouse", "func_147116_af");
		middle_click_mouse = ReflectionHelper.findMethod(Minecraft.class, "middleClickMouse", "func_147112_ai");
		right_click_mouse = ReflectionHelper.findMethod(Minecraft.class, "rightClickMouse", "func_147121_ag");
		
		try {
			display_impl = ReflectionHelper.findField(Display.class, "display_impl").get(null);
		} catch (IllegalArgumentException | IllegalAccessException e1) {
			throw new AssertionError(e1);
		}
		 
		JFrame FRAME = new JFrame();
		JPanel PANEL = new JPanel();
		FRAME.add(PANEL);
		FRAME.setVisible(true);
		Point panelPoint = PANEL.getLocationOnScreen();
		TOP = panelPoint.y;
		LEFT = panelPoint.x;
		
		FRAME.setVisible(false);
		FRAME.dispose();
		
		try {
			ROBOT = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static final Minecraft mc = Minecraft.getMinecraft();
 	
 	public static boolean wasGUIJustOpen = true;
	
 	private float deltaX = 0;
 	private float deltaY = 0;
 	
 	ControllerStateWrapper lastState = ControllerStateWrapper.DISCONNECTED_CONTROLLER;
 	ControllerStateWrapper lastStateNoGL = ControllerStateWrapper.DISCONNECTED_CONTROLLER;
 	

 	
 	@SubscribeEvent
 	public void onControllerEvent(ControllerEventNoGL.Pre e) throws IllegalArgumentException, IllegalAccessException{
		ControllerStateWrapper state = e.getModifiedControllerState();
		
		GuiScreen gui = mc.currentScreen;
		EntityPlayer player = mc.player;
		
		if(state.leftStickMagnitude > 0.3) {
			if(gui != null) {
				deltaX += state.leftStickX * state.leftStickMagnitude;
				deltaY += state.leftStickY * state.leftStickMagnitude;
				
				while(deltaX > 1 || deltaX < -1) {
					if(deltaX > 1) {
						moveMouseRelative(1, 0);
						deltaX--;
					}
					else if (deltaX < -1){
						moveMouseRelative(-1, 0);
						deltaX++;
					}
				}
				
				while(deltaY > 1 || deltaY < -1) {
					if(deltaY > -1) {
						moveMouseRelative(0, 1);
						deltaY--;
					}
					else if (deltaY < 1) {
						moveMouseRelative(0, -1);
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
		}
 	}
 	
 	@SubscribeEvent
 	public void onControllerEventNoGL(ControllerEventNoGL.Post e) {
 		ControllerStateWrapper state = e.getModifiedControllerState();
 		GuiScreen gui = mc.currentScreen;
 		if(gui != null) {
 			if(state.aJustPressed) {
 				ROBOT.mousePress(InputEvent.BUTTON1_DOWN_MASK);
 			}
 			else if(!state.a && lastStateNoGL.a) {
 				ROBOT.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
 			}
 			
 			if(state.xJustPressed && wasGUIJustOpen) {
 				ROBOT.mousePress(InputEvent.BUTTON3_DOWN_MASK);
 			}
 			else if(!state.x && lastStateNoGL.x) {
 				ROBOT.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
 			}
 			
 			if(state.yJustPressed) {
 				ROBOT.keyPress(KeyEvent.VK_SHIFT);
 				ROBOT.mousePress(InputEvent.BUTTON1_DOWN_MASK);
 			}
 			else if(!state.y && lastStateNoGL.y) {
 				ROBOT.keyRelease(KeyEvent.VK_SHIFT);
 				ROBOT.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
 			}
 			
 			if(state.bJustPressed || state.backJustPressed) {
 				ROBOT.keyPress(KeyEvent.VK_ESCAPE);
 				ROBOT.keyRelease(KeyEvent.VK_ESCAPE);
 			}
 			
 			wasGUIJustOpen = true;
 		}
 		else if(gui == null) {
 			wasGUIJustOpen = false;
 		}
 		
		if(state.rightStickJustClicked) {
			try {
				middle_click_mouse.invoke(mc);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				throw new AssertionError(e1);
			}
		}
 		
		lastStateNoGL = state;
 	}
	
 	@SubscribeEvent
 	public void onControllerEvent(ControllerEvent.Post e) {
 		GuiScreen gui = mc.currentScreen;
 		ControllerStateWrapper state = e.getControllerState();
		EntityPlayer player = mc.player;
		if(gui == null && player != null && mc.world.isRemote) {
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
			
			if(state.rightTriggerJustReachedThreshold) {
				try {
					click_mouse.invoke(mc);
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					throw new AssertionError(e1);
				}
			}
			else if(state.rightTriggerJustStoppedInputting) {
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
			}
			
			if(!state.rightTriggerJustReachedThreshold && state.leftTriggerJustReachedThreshold) {
				try {
					right_click_mouse.invoke(mc);
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					throw new AssertionError(e1);
				}
			}
			else if(state.leftTriggerJustStoppedInputting) {
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
			}

			
			if(state.startJustPressed || state.guideJustPressed) {
				mc.displayInGameMenu();
			}
			
			if(state.yJustPressed) {
				if (!player.isSpectator()){
					mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
				}
			}
			
			if(player.getRidingEntity() instanceof EntityBoat) {
				
				byte triggerState = 0;
				
				if(state.leftTrigger > 0.5) {
					triggerState ^= 1; //Bit marked by X: [0000000X]
				}
				if(state.rightTrigger > 0.5) {
					triggerState ^= 2; //Bit marked by X: [000000X0]
				}
				if(state.back) {
					triggerState ^= 4; //Bit marked by X: [00000X00]
				}
				
				switch(triggerState) {
				
					//going forward
				
					case 1:
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
						break;
					case 2:
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
						break;
					case 3:
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
						break;
						
					//going backward
						
					case 5:
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
						break;
						
					case 6:
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
						break;
						
					case 7:
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
						break;
					default:
						System.out.println((int)triggerState);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
						break;
				}
			
			}
		}
 		lastState = e.getModifiedControllerState();
 	}

 	
 	@SubscribeEvent
 	public void onMove(MoveOneMeterEvent e) {
 		if(isHoldingPlaceButton()) {
 			try {
				right_click_mouse.invoke(mc);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				throw new AssertionError(e1);
			}
 		}
 	}
 	
 	@SubscribeEvent
 	public void onItemUse(LivingEntityUseItemEvent.Finish e) {
 		if(e.getEntity().equals(mc.player)) {
 			if(isHoldingPlaceButton()) {
 				try {
	 				right_click_mouse.invoke(mc);
	 				KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
 				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
 					throw new AssertionError(e1);
 				}
 			}
 		}
 	}
 	
 	private boolean isHoldingPlaceButton() {
 		if(ControllerHandler.INSTANCE.getActiveControllerState().leftTrigger > 0.5) {
 			return true;
 		}
 		
 		int keycode = mc.gameSettings.keyBindUseItem.getKeyCode();
 		
 		if(keycode < 0) {
 			keycode = keycode + 100;
 			return Mouse.isButtonDown(keycode);
 		}
 		return Keyboard.isKeyDown(keycode);
 	}
	
	private void moveMouseRelative(int relativeX, int relativeY) {
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		
		int top = TOP;
		int left = LEFT;
		if(mc.isFullScreen()) {
			left = 0;
			top = 0;
		}
		
		int[] pos = getWindowPosition();
		
		int newX = MathHelper.clamp((int)mouse.getX() + relativeX, pos[0] + left, pos[0] + mc.displayWidth + left - 1);
		int newY = MathHelper.clamp((int)mouse.getY() - relativeY, pos[1] + top, pos[1] + mc.displayHeight + top);
		
		ROBOT.mouseMove(newX, newY);
	}
	
	public boolean isMouseWithinBounds() {
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		
		int top = TOP;
		int left = LEFT;
		if(mc.isFullScreen()) {
			left = 0;
			top = 0;
		}
		
		int[] pos = getWindowPosition();
		
		return (int)mouse.getX() >= pos[0] + left 
			&& (int)mouse.getX() <= pos[0] + mc.displayWidth + left - 1
			&& (int)mouse.getY() >= pos[1] + top
			&& (int)mouse.getY() <= pos[1] + mc.displayHeight + top;
	}
	
	public int[] getWindowPosition() { //must use this method so we don't lock the minejoy thread
		try {
			return new int[] {(int) getX.invoke(display_impl), (int) getY.invoke(display_impl)};
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new AssertionError(e);
		}
	}
}

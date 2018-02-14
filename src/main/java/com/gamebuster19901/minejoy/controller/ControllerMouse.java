package com.gamebuster19901.minejoy.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.gamebuster19901.minejoy.gui.GuiPossibleModIncompatability;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public enum ControllerMouse{
	
	INSTANCE;
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	private static final Field BUTTON_FIELD = ReflectionHelper.findField(GuiScreen.class, "buttonList", "field_146292_n");
	
	private static final Method CLICK_MOUSE_METHOD = ReflectionHelper.findMethod(Minecraft.class, "clickMouse", "func_147116_af");
	private static final Method MOUSE_PRESS_METHOD = ReflectionHelper.findMethod(GuiScreen.class, "mouseClicked", "func_73864_a", int.class, int.class, int.class);
	private static final Method MOUSE_DRAG_METHOD = ReflectionHelper.findMethod(GuiScreen.class, "mouseClickMove", "func_146273_a", int.class, int.class, int.class, long.class);
 	private static final Method MOUSE_RELEASE_METHOD = ReflectionHelper.findMethod(GuiScreen.class, "mouseReleased", "func_146286_b", int.class, int.class, int.class);
 	private static final Method KEY_TYPE_METHOD = ReflectionHelper.findMethod(GuiScreen.class, "keyTyped", "func_73869_a", char.class, int.class);
 	private static final Method SLOT_CLICKED_METHOD = ReflectionHelper.findMethod(GuiContainer.class, "handleMouseClick", "func_184098_a", Slot.class, int.class, int.class, ClickType.class);
 	
 	private float deltaX = 0;
 	private float deltaY = 0;
 	
 	private ControllerStateWrapper lastState = ControllerStateWrapper.DISCONNECTED_CONTROLLER;
 	private BlockPos lastBlockPos = BlockPos.ORIGIN.offset(EnumFacing.DOWN);
 	
 	@SubscribeEvent
 	public void onControllerEvent(ControllerEventNoGL.Pre e) throws IllegalArgumentException, IllegalAccessException{
		Mouse.getEventX();
		Mouse.getEventY();
		ControllerStateWrapper state = e.getModifiedControllerState();
		
		
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
		}
 	}
 	
 	@SubscribeEvent
	public void onControllerEvent(ControllerEvent.Post e) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Mouse.getEventX();
		Mouse.getEventY();
		ControllerStateWrapper state = e.getModifiedControllerState();
		
		GuiScreen gui = mc.currentScreen;
		EntityPlayer player = mc.player;
		if(gui != null) {
			
			List<GuiButton> buttons = (List<GuiButton>)BUTTON_FIELD.get(gui);
			for(GuiButton b : buttons) {
				if(b.isMouseOver() && b.visible && b.enabled) {
					break;
				}
			}

			if(state.aJustPressed) {
				MOUSE_PRESS_METHOD.invoke(gui, getMouseX(gui), getMouseY(gui), 0);
				System.out.println("pressed a");
			}
			else if(!state.a && lastState.a) {
				MOUSE_RELEASE_METHOD.invoke(gui, getMouseX(gui), getMouseY(gui), 0);
				System.out.println("released a");
			}
			else if (state.a && lastState.a) {
				MOUSE_DRAG_METHOD.invoke(gui, getMouseX(gui), getMouseY(gui), 0, 0l);
				System.out.println("drag a");
			}
			
			if(state.xJustPressed) {
				MOUSE_PRESS_METHOD.invoke(gui, getMouseX(gui), getMouseY(gui), 1);
				System.out.println("pressed x");
			}
			else if (!state.x && lastState.x) {
				MOUSE_RELEASE_METHOD.invoke(gui, getMouseX(gui), getMouseY(gui), 1);
				System.out.println("released x");
			}
			else if(state.x && lastState.x) {
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
			
			if(state.bJustPressed || state.backJustPressed || state.startJustPressed || state.guideJustPressed) {
				KEY_TYPE_METHOD.invoke(gui, (char)0x1B, 1);
			}
			
		}
		else {
			if(player != null && mc.world.isRemote) {
				
				if(GuiPossibleModIncompatability.compatibleControllers.contains(mc.playerController.getClass())){

				}
				else if(GuiPossibleModIncompatability.overwriteableControllers.contains(mc.playerController.getClass())) {
					mc.playerController = PlayerControllerMPMinejoy.getNewInstance();
				}
				else if(GuiPossibleModIncompatability.knownIncompatabilities.contains(mc.playerController.getClass())) {

				}
				else {
					mc.displayGuiScreen(new GuiPossibleModIncompatability(mc.playerController.getClass()));
					mc.playerController = PlayerControllerMPMinejoy.getNewInstance();
				}
				
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
				
				if(!state.leftTriggerJustReachedThreshold && state.leftTrigger > 0.5) {
					if(mc.objectMouseOver.typeOfHit == Type.BLOCK) {
						BlockSnapshot blockSnapshot = new BlockSnapshot(player.world, mc.objectMouseOver.getBlockPos().offset(mc.objectMouseOver.sideHit), mc.world.getBlockState(mc.objectMouseOver.getBlockPos().offset(mc.objectMouseOver.sideHit)));
						IBlockState placedAgainst = player.world.getBlockState(mc.objectMouseOver.getBlockPos());
						if(MinecraftForge.EVENT_BUS.post(new BlockEvent.PlaceEvent(blockSnapshot, placedAgainst, player, EnumHand.MAIN_HAND))){
							mc.playerController.processRightClickBlock((EntityPlayerSP)player, (WorldClient)player.world, mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec, EnumHand.MAIN_HAND);
						}
					}
				}
				
				if(state.rightTriggerJustReachedThreshold) {
					CLICK_MOUSE_METHOD.invoke(mc);
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
		}
		
	 	lastState = state;
	}
	
	private int getMouseX(GuiScreen gui) {
		return Mouse.getX() * gui.width / this.mc.displayWidth;
	}
	
	private int getMouseY(GuiScreen gui) {
		return gui.height - Mouse.getY() * gui.height / this.mc.displayHeight - 1;
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onItemPlace(PlayerInteractEvent.RightClickBlock e) {
		if(e.isCanceled()) {return;}
		
		if(e.getWorld().isRemote) {
			if(lastBlockPos.equals(e.getPos())){
				e.setCanceled(true);
			}
			if(lastState.leftTriggerJustReachedThreshold) {
				System.out.println(lastState.leftTriggerJustReachedThreshold);
				e.setCanceled(false);
			}
			else {
				lastBlockPos = e.getPos().offset(e.getFace());
				System.out.println(lastBlockPos + " + " + e.getEntityPlayer().getPosition());
			}
		}
	}
}

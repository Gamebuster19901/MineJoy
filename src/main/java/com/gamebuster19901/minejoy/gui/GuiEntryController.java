package com.gamebuster19901.minejoy.gui;

import com.gamebuster19901.minejoy.config.MineJoyConfig;
import com.gamebuster19901.minejoy.controller.ControllerEvent;
import com.gamebuster19901.minejoy.controller.ControllerStateWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiEntryController extends ControllerStateWrapper implements IGuiListEntry{
	private Console console;
	
	public GuiEntryController(Console console) {
		super(ControllerStateWrapper.DISCONNECTED_CONTROLLER);
		this.console = console;
	}

	@Override
	public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
		ResourceLocation controllerImage = console.getResourceLocation();
		setConsole(Console.getConsole(MineJoyConfig.controllerType));
		Minecraft.getMinecraft().getTextureManager().bindTexture(controllerImage);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0f, 0f, 512, 512, 512, 512);
	}

	@Override
	public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
		return (mouseEvent != 0); 
	}

	@Override
	public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		
	}
	
	private void setConsole(Console c) {
		console = c;
	}
	
	@SubscribeEvent
	public void onControllerEvent(ControllerEvent.Post e) {
		ControllerStateWrapper state = e.getControllerState();
		jamePadState = state.getJamepadState();
		isConnected = state.isConnected;
		controllerType = state.controllerType;
		leftStickX = state.leftStickX;
		leftStickY = state.leftStickY;
		rightStickX = state.rightStickX;
		rightStickY = state.rightStickY;
		leftStickAngle = state.leftStickAngle;
		leftStickMagnitude = state.leftStickMagnitude;
		rightStickAngle = state.rightStickAngle;
		rightStickMagnitude = state.rightStickMagnitude;
		leftStickClick = state.leftStickClick;
		leftStickJustClicked = state.leftStickJustClicked;
		rightStickClick = state.rightStickClick;
		rightStickJustClicked = state.rightStickJustClicked;
		
		leftTrigger = state.leftTrigger;
		leftTriggerJustReachedThreshold = state.leftTriggerJustReachedThreshold;
		leftTriggerJustStoppedInputting = state.leftTriggerJustStoppedInputting;
		rightTrigger = state.rightTrigger;
		rightTriggerJustReachedThreshold = state.rightTriggerJustReachedThreshold;
		rightTriggerJustStoppedInputting = state.rightTriggerJustStoppedInputting;
		
		a = state.a;
		aJustPressed = state.aJustPressed; //
		b = state.b;
		bJustPressed = state.bJustPressed;
		x = state.x;
		xJustPressed = state.xJustPressed;
		y = state.y;
		yJustPressed = state.yJustPressed;
		lb = state.lb;
		lbJustPressed = state.lbJustPressed;
		rb = state.rb;
		rbJustPressed = state.rbJustPressed;
		start = state.start;
		startJustPressed = state.startJustPressed;
		back = state.back;
		backJustPressed = state.backJustPressed;
		guide = state.guide;
		guideJustPressed = state.guideJustPressed;
		dpadUp = state.dpadUp;
		dpadUpJustPressed = state.dpadUpJustPressed;
		dpadDown = state.dpadDown;
		dpadDownJustPressed = state.dpadDownJustPressed;
		dpadLeft = state.dpadLeft;
		dpadLeftJustPressed = state.dpadLeftJustPressed;
		dpadRight = state.dpadRight;
		dpadRightJustPressed = state.dpadRightJustPressed;
	}


}

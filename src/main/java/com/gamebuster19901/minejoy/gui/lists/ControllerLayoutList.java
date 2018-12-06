package com.gamebuster19901.minejoy.gui.lists;

import java.util.List;

import com.gamebuster19901.minejoy.gui.GuiControllerOptions;

import net.minecraft.client.Minecraft;

public class ControllerLayoutList extends ControllerOptionsList<LayoutEntry>{

	GuiControllerOptions parentScreen;
	boolean droppedDown = false;
	
	public ControllerLayoutList(GuiControllerOptions parentScreen, List<LayoutEntry> entries) {
		this(parentScreen, entries, false);
	}
	
	public ControllerLayoutList(GuiControllerOptions parentScreen, List<LayoutEntry> entries, boolean droppedDown) {
		super(Minecraft.getMinecraft(), parentScreen.width / 3, 16, entries);
	}

}

package com.gamebuster19901.minejoy.controller;

import com.studiohartman.jamepad.ControllerIndex;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ControllerEvent extends Event{
	private int index;
	private ControllerStateWrapper state;
	private ControllerIndex unsafe;
	public ControllerEvent(int index, ControllerStateWrapper state, ControllerIndex unsafe) {
		this.index = index;
		this.state = state;
		this.unsafe = unsafe;
	}
	
	public int getControllerIndex() {
		return index;
	}
	
	public ControllerIndex getUnsafeController() {
		return unsafe;
	}
	
	public ControllerStateWrapper getControllerState() {
		return state;
	}
}

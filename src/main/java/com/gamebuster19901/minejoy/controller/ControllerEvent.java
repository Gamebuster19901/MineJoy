package com.gamebuster19901.minejoy.controller;

import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerState;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ControllerEvent extends Event{
	private int index;
	private ControllerState state;
	private ControllerIndex unsafe;
	public ControllerEvent(int index, ControllerState state, ControllerIndex unsafe) {
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
	
	public ControllerState getControllerState() {
		return state;
	}
}

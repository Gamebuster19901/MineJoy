package com.gamebuster19901.minejoy.controller;

import com.studiohartman.jamepad.ControllerIndex;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class ControllerEvent extends Event{
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
	
	/**
	 * @deprecated for internal use only
	 */
	@Deprecated
	public static class Pre extends ControllerEvent{

		public Pre(int index, ControllerStateWrapper state, ControllerIndex unsafe) {
			super(index, state, unsafe);
		}
		
	}
	
	public static class Post extends ControllerEvent{

		public Post(int index, ControllerStateWrapper state, ControllerIndex unsafe) {
			super(index, state, unsafe);
		}
		
	}
}

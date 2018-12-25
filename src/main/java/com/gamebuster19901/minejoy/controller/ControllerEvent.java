package com.gamebuster19901.minejoy.controller;

import com.gamebuster19901.minejoy.controller.layout.Layout;
import com.studiohartman.jamepad.ControllerIndex;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This event is posted every millisecond
 * 
 * Note that when using this event there is no OpenGL context, therefore it is not safe for rendering.
 */
public abstract class ControllerEvent extends Event{
	private int index;
	private ControllerStateWrapper state;
	private ControllerStateWrapper modified;
	private ControllerIndex unsafe;
	
	public ControllerEvent(int index, ControllerStateWrapper state, ControllerIndex unsafe) {
		this.index = index;
		this.state = state;
		this.modified = Layout.getLayout().getWrapper(state);
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
	
	public ControllerStateWrapper getModifiedControllerState() {
		return modified;
	}
	
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

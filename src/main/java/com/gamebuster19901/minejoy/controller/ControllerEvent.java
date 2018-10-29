package com.gamebuster19901.minejoy.controller;

import com.gamebuster19901.minejoy.controller.layout.Layout;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This event is safe for rendering, but is only called once per tick.
 * 
 * This is useful for things that don't need to be updated as often as possible, but needs to be rendered by openGL, like
 * pressing a GuiButton.
 */

public abstract class ControllerEvent extends Event{
	private int index;
	private ControllerStateWrapper state;
	
	public ControllerEvent(int index, ControllerStateWrapper state) {
		this.index = index;
		this.state = state;
	}
	
	public int getControllerIndex() {
		return index;
	}
	
	public ControllerStateWrapper getControllerState() {
		return state;
	}
	
	public ControllerStateWrapper getModifiedControllerState() {
		return Layout.getLayout().getWrapper(state);
	}
	
	/**
	 * @deprecated for internal use only
	 */
	@Deprecated
	public static class Pre extends ControllerEvent{

		public Pre(int index, ControllerStateWrapper state) {
			super(index, state);
		}
		
	}
	
	public static class Post extends ControllerEvent{

		public Post(int index, ControllerStateWrapper state) {
			super(index, state);
		}
		
	}
}

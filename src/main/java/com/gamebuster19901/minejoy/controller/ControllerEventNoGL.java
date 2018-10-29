package com.gamebuster19901.minejoy.controller;

/**
 * This event is posted every millisecond but is not safe for rendering as the thread it is in has no OpenGL context
 * 
 * This is useful for things that must be updated as often as possible and don't need to be rendered by OpenGL, like
 * moving the mouse cursor.
 */
public abstract class ControllerEventNoGL extends ControllerEvent{
	public ControllerEventNoGL(int index, ControllerStateWrapper state) {
		super(index, state);
	}
	
	/**
	 * 
	 * This event is posted every millisecond but is not safe for rendering as the thread it is in has no OpenGL context
	 * 
	 * @deprecated for internal use only
	 */
	@Deprecated
	public static class Pre extends ControllerEventNoGL{

		public Pre(int index, ControllerStateWrapper state) {
			super(index, state);
		}
		
	}
	
	/**
	 * This event is posted every millisecond but is not safe for rendering as the thread it is in has no OpenGL context
	 */
	public static class Post extends ControllerEventNoGL{

		public Post(int index, ControllerStateWrapper state) {
			super(index, state);
		}
		
	}
}

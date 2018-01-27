package com.gamebuster19901.minejoy.controller.layout;

import com.gamebuster19901.minejoy.controller.ControllerStateWrapper;

public final class Default extends Layout {
	public Default() {
		
	}
	
	@Override
	public LayoutWrapper getWrapper(ControllerStateWrapper state) {
		return new DefaultWrapper(state);
	}
	
	private class DefaultWrapper extends LayoutWrapper{

		public DefaultWrapper(ControllerStateWrapper state) {
			super(state);
		}
		
		@Override
		public LayoutWrapper interpret() {
			return super.interpret(); //this is default, we don't need to change anything
		}
		
	}
}
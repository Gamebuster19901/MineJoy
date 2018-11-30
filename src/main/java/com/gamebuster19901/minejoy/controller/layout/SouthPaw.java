package com.gamebuster19901.minejoy.controller.layout;

import com.gamebuster19901.minejoy.controller.ControllerStateWrapper;

import net.minecraft.client.resources.I18n;

public class SouthPaw extends Layout{

	@Override
	public LayoutWrapper getWrapper(ControllerStateWrapper state) {
		return new SouthPawWrapper(state);
	}
	
	private class SouthPawWrapper extends LayoutWrapper{

		public SouthPawWrapper(ControllerStateWrapper state) {
			super(state);
		}
		
		@Override
		public LayoutWrapper interpret() {
			super.interpret(); //for stick inversion
			
			float tempLeftStickAngle = this.leftStickAngle;
			boolean tempLeftStickClick = this.leftStickClick;
			boolean tempLeftStickJustClicked = this.leftStickJustClicked;
			float tempLeftStickMagnitude = this.leftStickMagnitude;
			float tempLeftStickX = this.leftStickX;
			float tempLeftStickY = this.leftStickY;
			
			leftStickAngle = rightStickAngle;
			leftStickClick = rightStickClick;
			leftStickJustClicked = rightStickJustClicked;
			leftStickMagnitude = rightStickMagnitude;
			leftStickX = rightStickX;
			leftStickY = rightStickY;
			
			rightStickAngle = tempLeftStickAngle;
			rightStickClick = tempLeftStickClick;
			rightStickJustClicked = tempLeftStickJustClicked;
			rightStickMagnitude = tempLeftStickMagnitude;
			rightStickX = tempLeftStickX;
			rightStickY = tempLeftStickY;
			
			return this;
			
		}

		@Override
		public String getName() {
			return I18n.format("options.minejoy.layout.southpaw");
		}
		
	}

}

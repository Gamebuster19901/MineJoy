package com.gamebuster19901.minejoy.controller.layout;

import com.gamebuster19901.minejoy.controller.ControllerStateWrapper;

import net.minecraft.client.resources.I18n;

public class Legacy extends Layout{

	@Override
	public LayoutWrapper getWrapper(ControllerStateWrapper state) {
		return new LegacyWrapper(state);
	}
	
	public final class LegacyWrapper extends LayoutWrapper{

		protected LegacyWrapper(ControllerStateWrapper state) {
			super(state);
		}
		
		@Override
		protected LayoutWrapper interpret() {
			super.interpret(); //for stick inversion
			
			float tempLeftStickMagnitude = this.leftStickMagnitude;
			float tempLeftStickX = this.leftStickX;
			
			
			
			return this;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return I18n.format("options.minejoy.layout.legacy");
		}
		
	}

}

package com.gamebuster19901.minejoy.controller.layout;

import com.gamebuster19901.minejoy.controller.ControllerStateWrapper;
import com.gamebuster19901.minejoy.controller.ControllerStateWrapper.Axis;
import com.gamebuster19901.minejoy.controller.ControllerStateWrapper.Button;

public abstract class Control<T> {
	
	public static interface Axes{
		AxisControl L_X = new AxisControl(ControllerStateWrapper.Axis.L_X);
		AxisControl L_Y = new AxisControl(ControllerStateWrapper.Axis.L_Y);
		AxisControl R_X = new AxisControl(ControllerStateWrapper.Axis.R_X);
		AxisControl R_Y = new AxisControl(ControllerStateWrapper.Axis.R_Y);
		AxisControl LT = new AxisControl(ControllerStateWrapper.Axis.LT);
		AxisControl RT = new AxisControl(ControllerStateWrapper.Axis.RT);
		
		AxisControl[] ALL = new AxisControl[] {
				L_X,
				L_Y,
				R_X,
				R_Y,
				LT,
				RT
		};
	}
	
	public static interface Buttons{
		ButtonControl A = new ButtonControl(ControllerStateWrapper.Button.A);
		ButtonControl B = new ButtonControl(ControllerStateWrapper.Button.B);
		ButtonControl BACK = new ButtonControl(ControllerStateWrapper.Button.BACK);
		ButtonControl D_PAD_DOWN = new ButtonControl(ControllerStateWrapper.Button.D_PAD_DOWN);
		ButtonControl D_PAD_LEFT = new ButtonControl(ControllerStateWrapper.Button.D_PAD_LEFT);
		ButtonControl D_PAD_RIGHT = new ButtonControl(ControllerStateWrapper.Button.D_PAD_RIGHT);
		ButtonControl D_PAD_UP = new ButtonControl(ControllerStateWrapper.Button.D_PAD_UP);
		ButtonControl GUIDE = new ButtonControl(ControllerStateWrapper.Button.GUIDE);
		ButtonControl LB = new ButtonControl(ControllerStateWrapper.Button.LB);
		ButtonControl LEFT_STICK_BUTTON = new ButtonControl(ControllerStateWrapper.Button.LEFT_STICK_BUTTON);
		ButtonControl RB = new ButtonControl(ControllerStateWrapper.Button.RB);
		ButtonControl RIGHT_STICK_BUTTON = new ButtonControl(ControllerStateWrapper.Button.RIGHT_STICK_BUTTON);
		ButtonControl START= new ButtonControl(ControllerStateWrapper.Button.START);
		ButtonControl X = new ButtonControl(ControllerStateWrapper.Button.X);
		ButtonControl Y = new ButtonControl(ControllerStateWrapper.Button.Y);
		
		ButtonControl[] ALL = new ButtonControl[] {
				A,
				B,
				BACK,
				D_PAD_DOWN,
				D_PAD_LEFT,
				D_PAD_RIGHT,
				D_PAD_UP,
				GUIDE,
				LB,
				LEFT_STICK_BUTTON,
				RB,
				RIGHT_STICK_BUTTON,
				START,
				X,
				Y
		};
	}
	
	protected final String name;
	protected final transient T control;
	
	public Control(String name, T control) {
		this.name = name;
		this.control = control;
	}
	
	public T getControl() {
		return control;
	}
	
	
	@Override
	public final String toString() {
		return name;
	}
	
	public static class AxisControl extends Control<Axis>{
		public AxisControl(Axis axis) {
			super(axis.name(), axis);
		}
	}
	
	public static class ButtonControl extends Control<Button>{

		public ButtonControl(String name) {
			super(name, Button.getButton(name));
		}
		
		public ButtonControl(Button button) {
			super(button.name(), button);
		}

	}
}

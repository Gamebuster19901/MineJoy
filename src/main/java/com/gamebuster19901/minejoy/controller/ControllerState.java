package com.gamebuster19901.minejoy.controller;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;

import org.lwjgl.input.Controller;

import com.gamebuster19901.minejoy.Minejoy;

public final class ControllerState {

	private final Controller controller;
	
	private final ArrayList<Axis> axes;
	private final ArrayList<Button> buttons;
	private final ArrayList<Rumbler> rumblers;
	
	public ControllerState(Controller controller) {
		this.controller = controller;
		
		ArrayList<Axis> axes = new ArrayList<Axis>();
		ArrayList<Button> buttons = new ArrayList<Button>();
		ArrayList<Rumbler> rumblers = new ArrayList<Rumbler>();
		
		int max = Math.max(Math.max(controller.getAxisCount(), controller.getButtonCount()), controller.getRumblerCount());
		for(int i = 0; i < max; i++) {
			if(i < controller.getAxisCount()) {
				axes.add(new Axis(i));
			}
			if(i < controller.getButtonCount()) {
				buttons.add(new Button(i));
			}
			if(i < controller.getRumblerCount()) {
				rumblers.add(new Rumbler(i));
			}
		}
		
		this.axes = axes;
		this.buttons = buttons;
		this.rumblers = rumblers;
		
		poll();
	}
	
	private void poll() {
		this.controller.poll();
		
		for(Button button : buttons) {
			if(button.isPressed()) {
				Minejoy.LOGGER.log(Level.INFO, "[" + button.getIndex() + "] " + button.getName() + ": " + button.isPressed());
			}
		}
		
		for(Axis axis : axes) {
			if(Math.abs(axis.getValue()) > 0.5) {
				Minejoy.LOGGER.log(Level.INFO, "[" + axis.getName() + "]" + axis.getValue());
			}
		}
	}
	
	 /**
     * Whether or not the controller is currently connected.
     *
     * If the controller is disconnected, all other fields will be 0 or false.
     */
    public boolean isConnected = false;

    /**
     * A string describing the type of controller (i.e. "PS4 Controller" or "XInput Controller")
     */
    public String controllerType = "Unplugged Controller";

    /**
     * The x position of the left stick between -1 and 1
     */
    public float leftStickX = 0;

    /**
     * The y position of the left stick between -1 and 1
     */
    public float leftStickY = 0;

    /**
     * The x position of the right stick between -1 and 1
     */
    public float rightStickX = 0;

    /**
     * The y position of the right stick between -1 and 1
     */
    public float rightStickY = 0;

    /**
     * The angle of the left stick (for reference, 0 is right, 90 is up, 180 is left, 270 is down)
     */
    public float leftStickAngle = 0;

    /**
     * The amount the left stick is pushed in the current direction. This probably between 0 and 1,
     * but this can't be guaranteed due to weird gamepads (like the square holes on a Logitech Dual Action)
     */
    public float leftStickMagnitude = 0;

    /**
     * The angle of the right stick (for reference, 0 is right, 90 is up, 180 is left, 270 is down)
     */
    public float rightStickAngle = 0;

    /**
     * The amount the right stick is pushed in the current direction. This probably between 0 and 1,
     * but this can't be guaranteed due to weird gamepads (like the square holes on a Logitech Dual Action)
     */
    public float rightStickMagnitude = 0;

    /**
     * Whether or not the left stick is clicked in
     */
    public boolean leftStickClick = false;

    /**
     * Whether or not the right stick is clicked in
     */
    public boolean rightStickClick = false;

    /**
     * The position of the left trigger between 0 and 1
     */
    public float leftTrigger = 0;
    
    /**
     * Whether or not the left trigger has just reached it's threshold: I.E. Just pressed beyond 50%
     */
    public boolean leftTriggerJustReachedThreshold = false;
    
    /**
     * Whether or not the left trigger has just stopped inputting: I.E. Just released below 50%
     */
    
    public boolean leftTriggerJustStoppedInputting = false;

    /**
     * The position of the right trigger between 0 and 1
     */
    public float rightTrigger = 0;
    
    /**
     * Whether or not the right trigger has just reached it's threshold: I.E. Just pressed beyond 50%
     */
    
    public boolean rightTriggerJustReachedThreshold = false;
    
    /**
     * Whether or not the right trigger has just stopped inputting: I.E. Just released below 50%
     */
    
    public boolean rightTriggerJustStoppedInputting = false;

    /**
     * Whether or not the left stick was just is clicked in
     */
    public boolean leftStickJustClicked = false;

    /**
     * Whether or not the right stick was just is clicked in
     */
    public boolean rightStickJustClicked = false;

    /**
     * Whether or not the a button is pressed
     */
    public boolean a = false;

    /**
     * Whether or not the b button is pressed
     */
    public boolean b = false;

    /**
     * Whether or not the x button is pressed
     */
    public boolean x = false;

    /**
     * Whether or not the y button is pressed
     */
    public boolean y = false;

    /**
     * Whether or not the left bumper is pressed
     */
    public boolean lb = false;

    /**
     * Whether or not the right bumper is pressed
     */
    public boolean rb = false;

    /**
     * Whether or not the start button is pressed
     */
    public boolean start = false;

    /**
     * Whether or not the back button is pressed
     */
    public boolean back = false;

    /**
     * Whether or not the guide button is pressed. For some controller/platform combinations this
     * doesn't work. You probably shouldn't use this.
     */
    public boolean guide = false;

    /**
     * Whether or not the up button on the dpad is pushed
     */
    public boolean dpadUp = false;

    /**
     * Whether or not the down button on the dpad is pushed
     */
    public boolean dpadDown = false;

    /**
     * Whether or not the left button on the dpad is pushed
     */
    public boolean dpadLeft = false;

    /**
     * Whether or not the right button on the dpad is pushed
     */
    public boolean dpadRight = false;

    /**
     * Whether or not the a button was just pressed
     */
    public boolean aJustPressed = false;

    /**
     * Whether or not the b button was just pressed
     */
    public boolean bJustPressed = false;

    /**
     * Whether or not the x button was just pressed
     */
    public boolean xJustPressed = false;

    /**
     * Whether or not the y button was just pressed
     */
    public boolean yJustPressed = false;

    /**
     * Whether or not the left bumper was just pressed
     */
    public boolean lbJustPressed = false;

    /**
     * Whether or not the right bumper was just pressed
     */
    public boolean rbJustPressed = false;

    /**
     * Whether or not the start button was just pressed
     */
    public boolean startJustPressed = false;

    /**
     * Whether or not the back button was just pressed
     */
    public boolean backJustPressed = false;

    /**
     * Whether or not the guide button was just pressed
     */
    public boolean guideJustPressed = false;

    /**
     * Whether or not the up button on the dpad was just pressed
     */
    public boolean dpadUpJustPressed = false;

    /**
     * Whether or not the down button on the dpad was just pressed
     */
    public boolean dpadDownJustPressed = false;

    /**
     * Whether or not the left button on the dpad was just pressed
     */
    public boolean dpadLeftJustPressed = false;

    /**
     * Whether or not the right button on the dpad was just pressed
     */
    public boolean dpadRightJustPressed = false;
	
	public final class Axis {
		private final int index;
		private final String name;
		
		private Axis(int index) {
			this.index = index;
			this.name = ControllerState.this.controller.getAxisName(index);
		}
		
		public int getIndex() {
			return index;
		}
		
		public String getName() {
			return name;
		}
		
		public float getValue() {
			return ControllerState.this.controller.getAxisValue(index);
		}
	}
	
	public final class Button {
		private final int index;
		private final String name;
		
		private Button(int index) {
			this.index = index;
			this.name = ControllerState.this.controller.getButtonName(index);
		}
		
		public int getIndex() {
			return index;
		}
		
		public String getName() {
			return name;
		}
		
		public boolean isPressed() {
			return ControllerState.this.controller.isButtonPressed(index);
		}
	}
	
	public final class Rumbler {
		private final int index;
		private final String name;
		
		private Rumbler(int index) {
			this.index = index;
			this.name = ControllerState.this.controller.getRumblerName(index);
		}
		
		public int getIndex() {
			return index;
		}
		
		public String getName() {
			return name;
		}
		
		public void rumble() {
			rumble(1f);
		}
		
		public void rumble(float strength) {
			ControllerState.this.controller.setRumblerStrength(index, strength);
		}
		
		public void rumble(int milli) {
			rumble(milli, 1f);
		}
		
		public void rumble(int milli, float strength) {
			Thread t = new Thread() {
				public void run() {
					rumble(strength);
					try {
						int i = milli;
						while(i > 0) {
							Thread.sleep(1);
							i--;
						}
					}
					catch(InterruptedException e) {
						Minejoy.LOGGER.log(Level.ERROR, "Rumbler thread \"" + this.getName() + "\" interrupted, stopping rumble!");
					}
					stopRumbling();
				}
			};
			t.setName(ControllerState.this.controller.getName() + ": " + getName());
			t.setDaemon(true);
			t.start();
		}
		
		public void stopRumbling() {
			rumble(0f);
		}
	}
	
}

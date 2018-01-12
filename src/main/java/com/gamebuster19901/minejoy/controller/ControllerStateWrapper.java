package com.gamebuster19901.minejoy.controller;

import com.studiohartman.jamepad.ControllerState;

public class ControllerStateWrapper {
	
	public static final ControllerStateWrapper DISCONNECTED_CONTROLLER = new ControllerStateWrapper();
	
	public static long leftStickClickHeld = 0;
	public static long rightStickClickHeld = 0;
	public static long aHeld = 0;
	public static long bHeld = 0;
	public static long xHeld = 0;
	public static long yHeld = 0;
	public static long lbHeld = 0;
	public static long rbHeld = 0;
	public static long startHeld = 0;
	public static long backHeld = 0;
	public static long guideHeld = 0;
	public static long dpadUpHeld = 0;
	public static long dpadDownHeld = 0;
	public static long dpadLeftHeld = 0;
	public static long dpadRightHeld = 0;
	
	
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
     * The position of the right trigger between 0 and 1
     */
    public float rightTrigger = 0;

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
	
	public ControllerStateWrapper(ControllerState state) {
		isConnected = state.isConnected;
		controllerType = state.controllerType;
		leftStickX = state.leftStickX;
		leftStickY = state.leftStickY;
		rightStickX = state.rightStickX;
		rightStickY = state.rightStickY;
		leftStickAngle = state.leftStickAngle;
		leftStickMagnitude = state.leftStickMagnitude;
		rightStickAngle = state.rightStickAngle;
		rightStickMagnitude = state.rightStickMagnitude;
		leftStickClick = state.leftStickClick;
		rightStickClick = state.rightStickClick;
		leftTrigger = state.leftTrigger;
		rightTrigger = state.rightTrigger;
		
		a = state.a;
		b = state.b;
		x = state.x;
		y = state.y;
		lb = state.lb;
		rb = state.rb;
		start = state.start;
		back = state.back;
		guide = state.guide;
		dpadUp = state.dpadUp;
		dpadDown = state.dpadDown;
		dpadLeft = state.dpadLeft;
		dpadRight = state.dpadRight;
	}
	
	private ControllerStateWrapper() {}
	
}

package com.gamebuster19901.minejoy.gui;

import com.gamebuster19901.minejoy.exception.UnknownConsoleException;

public class Unicode {
	
	/*
	 * 
	 * Playstation
	 * 
	 */
	
	public static final char CROSS = 0xE000;
	public static final char CIRCLE = 0xE001;
	public static final char TRIANGLE = 0xE002;
	public static final char SQUARE = 0xE003;
	public static final char DPAD_PS = 0xE004;
	public static final char DPAD_DOWN_PS = 0xE005;
	public static final char DPAD_RIGHT_PS = 0xE006;
	public static final char DPAD_UP_PS = 0xE007;
	public static final char DPAD_LEFT_PS = 0xE008;
	public static final char SELECT = 0xE009;
	public static final char START_PS = 0xE00A;
	public static final char L1 = 0xE00B;
	public static final char L2 = 0xE00C;
	public static final char LSTICK_PS = 0xE00D;
	public static final char R1 = 0xE00E;
	public static final char R2 = 0xE00F;
	public static final char RSTICK_PS = 0xE010;
	
	/*
	 * 
	 * Xbox
	 * 
	 */
	
	public static final char A = 0xE020;
	public static final char B = 0xE021;
	public static final char Y = 0xE022;
	public static final char X = 0xE023;
	public static final char DPAD_XB = 0xE024;
	public static final char DPAD_DOWN_XB = 0xE025;
	public static final char DPAD_RIGHT_XB = 0xE026;
	public static final char DPAD_UP_XB = 0xE027;
	public static final char DPAD_LEFT_XB = 0xE028;
	public static final char BACK = 0xE029;
	public static final char START_XB = 0xE02A;
	public static final char LB = 0xE02B;
	public static final char LT = 0xE02C;
	public static final char LSTICK_XB = 0xE02D;
	public static final char RB = 0xE02E;
	public static final char RT = 0xE02F;
	public static final char RSTICK_XB = 0xE030;
	
	private Unicode() {
		throw new AssertionError();
	}
	
	public static final char getButton(Console console, short button) {
		for(Console c : Console.getConsoles()) {
			if(console.equals(console)) {
				if(button < 0 || button > c.getButtonCount()) {
					throw new IndexOutOfBoundsException("Button must be between 0 and " + c.getButtonCount() + " (inclusive)");
				}
				return (char)(console.getCodepoint() + button);
			}
		}
		throw new UnknownConsoleException(console.toString());
	}
}

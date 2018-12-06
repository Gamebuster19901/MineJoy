package com.gamebuster19901.minejoy.gui;

import java.util.ArrayList;

import com.gamebuster19901.minejoy.exception.UnknownConsoleException;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public final class Console {
	private static final ArrayList<Console> registeredConsoles = new ArrayList<Console>();
	
	private static final Console[] DEFAULT_CONSOLES = new Console[] {
		new Console("none", (short) 0x0000, (short)0, new ResourceLocation("minejoy:textures/gui/controller/nc")),
		new Console("xbox", (short) 0xE000, (short)16, new ResourceLocation("minejoy:textures/gui/controller/xb")),
		new Console("playstation", (short) 0xE020, (short)16, new ResourceLocation("minejoy:textures/gui/controller/ps"))
	};
	
	private final String name;
	private final short codepoint;
	private final short buttonCount;
	private final ResourceLocation image;
	
	public Console(String name, short beginningCodepoint, short buttonAmount, ResourceLocation controllerImage) {
		this.name = name;
		codepoint = beginningCodepoint;
		buttonCount = buttonAmount;
		image = controllerImage;
	}
	
	public short getCodepoint(){
		return codepoint;
	}
	
	public short getButtonCount() {
		return buttonCount;
	}
	
	public ResourceLocation getResourceLocation() {
		return image;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Console) {
			Console other = (Console)o;
			return other.name == this.name || other.codepoint == this.codepoint;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public static void init() {
		for(Console c : DEFAULT_CONSOLES) {
			register(c);
		}
	};
	
	public static void register(Console console) {
		registeredConsoles.add(console);
	}
	
	public static Console getConsole(String name) throws UnknownConsoleException{
		for(Console c : registeredConsoles) {
			if(c.toString().equals(name)) {
				return c;
			}
		}
		return registeredConsoles.get(0);
	}
	
	public static Console getConsole(short codepoint, boolean strict) {
		for(Console c : registeredConsoles) {
			if(c.getCodepoint() == codepoint) {
				return c;
			}
			if(!strict) {
				if(codepoint > c.getCodepoint() && codepoint <= c.getCodepoint() + c.buttonCount) {
					return c;
				}
			}
		}
		throw new UnknownConsoleException(codepoint);
	}
	
	public static Console getConsole(char c) {
		return getConsole((short)c, false);
	}
	
	public static Console[] getConsoles(){
		return registeredConsoles.toArray(new Console[] {});
	}

	public String getButton(int id) {
		return I18n.format("options.minejoy." + this + "." + id);
	}
}

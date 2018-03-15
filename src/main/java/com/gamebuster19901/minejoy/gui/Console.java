package com.gamebuster19901.minejoy.gui;

import java.util.ArrayList;

import com.gamebuster19901.minejoy.exception.UnknownConsoleException;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;

public final class Console {
	private static final ArrayList<Console> registeredConsoles = new ArrayList<Console>();
	
	public static final Console XB = new Console("Xbox", (short) 0xE000, (short)16, new ResourceLocation("minejoy:textures/gui/controller/xb"));
	public static final Console PS = new Console("Playstation", (short) 0xE020, (short)16, new ResourceLocation("minejoy:textures/gui/controller/ps"));
	
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
	
	public static void init() {
		LoaderState state = Loader.instance().getLoaderState();
		switch(state) {
			case PREINITIALIZATION:
			case INITIALIZATION:
			case POSTINITIALIZATION:
				register(XB);
				register(PS);
				break;
		default:
			throw new IllegalStateException("Cannot init during loader state " + state);
		}
	};
	
	public static void register(Console console) {
		LoaderState state = Loader.instance().getLoaderState();
		switch(state) {
			case PREINITIALIZATION:
			case INITIALIZATION:
			case POSTINITIALIZATION:
				if(registeredConsoles.contains(console)) {
					throw new IllegalStateException(console + " already registered");
				}
				registeredConsoles.add(console);
				break;
		default:
			throw new IllegalStateException("Cannot register during loader state " + state);
		}
	}
	
	public Console getConsole(String name) {
		for(Console c : registeredConsoles) {
			if(c.toString() == name) {
				return c;
			}
		}
		throw new UnknownConsoleException(name);
	}
	
	public Console getConsole(short codepoint, boolean strict) {
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
	
	public static Console[] getConsoles(){
		return registeredConsoles.toArray(new Console[] {});
	}
}

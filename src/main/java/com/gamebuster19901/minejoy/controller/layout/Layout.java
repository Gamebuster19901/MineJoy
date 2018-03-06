package com.gamebuster19901.minejoy.controller.layout;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.gamebuster19901.minejoy.controller.ControllerStateWrapper;
import com.gamebuster19901.minejoy.exception.DuplicateElementException;

import net.minecraft.client.Minecraft;

public abstract class Layout{
	
	private static Layout currentLayout = new Default();
	
	public static boolean cameraYAxisInverted = false;
	
	public static boolean cameraXAxisInverted = false;
	
	public static boolean movementYAxisInverted = false;
	
	public static boolean movementXAxisInverted = false;
	
	public static boolean mouseYAxisInverted = false;
	
	public static boolean mouseXAxisInverted = false;
	
	private static ArrayList<Layout> registry = new ArrayList<Layout>();
	
	public abstract LayoutWrapper getWrapper(ControllerStateWrapper state);
	
	public abstract class LayoutWrapper extends ControllerStateWrapper{

		protected LayoutWrapper(ControllerStateWrapper state) {
			super(state);
			interpret();
		}
		
		protected LayoutWrapper interpret() {
			if(Minecraft.getMinecraft() == null || Minecraft.getMinecraft().currentScreen == null) {
				leftStickX = -leftStickX; //X axis for player movment is inverted when minecraft first processes it, so we must invert it back to make movement normal
				rightStickY = -rightStickY; //Y axis for camera movement is inverted when minecraft first processes it, so we must invert it back to make camera movement normal
				
				if(movementXAxisInverted) {
					leftStickX = -leftStickX;
				}
				
				if(movementYAxisInverted) {
					leftStickY = -leftStickY;
				}
				
				if(cameraXAxisInverted) {
					rightStickX = -rightStickX;
				}
				
				if(cameraYAxisInverted) {
					rightStickY = -rightStickY;
				}
				
			}
			
			else {
				if(mouseYAxisInverted) {
					leftStickY = -leftStickY;
				}
			}
			
			return this;
			
		}
		
		public abstract String getName();
		
	}
	
	public final String toString() {
		return this.getClass().getSimpleName();
	}
	
	public static final Layout getLayout() {
		return currentLayout;
	}
	
	public static final Layout getLayout(String clazz) {
		for(Layout l : registry) {
			if(l.getClass().getName().equals(clazz)) {
				return l;
			}
			else {
				System.out.println(l.getClass().getName() + " != " + clazz);
			}
		}
		throw new NoSuchElementException(clazz);
	}
	
	public static final void register(Layout layout) {
		if(!registry.contains(layout)) {
			registry.add(layout);
		}
		else {
			throw new DuplicateElementException(layout);
		}
	}
	
	public static final void setLayout(Layout l) {
		currentLayout = l;
	}

}

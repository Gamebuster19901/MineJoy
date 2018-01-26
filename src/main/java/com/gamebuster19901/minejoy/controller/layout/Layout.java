package com.gamebuster19901.minejoy.controller.layout;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.gamebuster19901.minejoy.controller.ControllerEvent;
import com.gamebuster19901.minejoy.controller.ControllerEventNoGL;
import com.gamebuster19901.minejoy.controller.ControllerStateWrapper;
import com.gamebuster19901.minejoy.exception.DuplicateElementException;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
	
	protected abstract class LayoutWrapper extends ControllerStateWrapper{

		public LayoutWrapper(ControllerStateWrapper state) {
			super(state);
			interpret();
		}
		
		protected LayoutWrapper interpret() {
			if(Minecraft.getMinecraft().currentScreen == null) {
				if(!cameraYAxisInverted) {//is inverted by jamepad, so we must invert it again
					rightStickY = -rightStickY;
				}
				
				if(cameraXAxisInverted) {
					rightStickX = -rightStickX;
				}
				
				if(movementYAxisInverted) {
					leftStickY = -leftStickY;
				}
				
				if(!movementXAxisInverted) { //is inverted by jamepad, so we must invert it again
					leftStickX = -leftStickX;
				}
				
			}
			
			else {
				
				if(mouseYAxisInverted) {
					leftStickY = -leftStickY;
				}
				
				if(mouseXAxisInverted) {
					leftStickX = -leftStickX;
				}
				
			}
			
			return this;
			
		}
		
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
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onControllerEvent(ControllerEventNoGL.Pre e) {
		e.setControllerState(currentLayout.getWrapper(e.getControllerState()));
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onControllerEvent(ControllerEventNoGL.Post e) {
		e.setControllerState(currentLayout.getWrapper(e.getControllerState()));
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onControllerEvent(ControllerEvent.Pre e) {
		e.setControllerState(currentLayout.getWrapper(e.getControllerState()));
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onControllerEvent(ControllerEvent.Post e) {
		e.setControllerState(currentLayout.getWrapper(e.getControllerState()));
	}

}

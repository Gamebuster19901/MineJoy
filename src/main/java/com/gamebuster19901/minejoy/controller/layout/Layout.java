package com.gamebuster19901.minejoy.controller.layout;

import com.gamebuster19901.minejoy.Minejoy;
import com.gamebuster19901.minejoy.controller.ControllerStateWrapper;
import com.gamebuster19901.minejoy.controller.layout.Control.AxisControl;
import com.gamebuster19901.minejoy.controller.layout.Control.ButtonControl;

import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.Level;

public abstract class Layout{
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	protected static final HashMap<Control, LayoutElement> DEFAULT_CONTROLS = new HashMap<Control, LayoutElement>();
	static {
		for(AxisControl a : Control.Axes.ALL) {
			DEFAULT_CONTROLS.put(a, new AxisElement());
		}
		for(ButtonControl b : Control.Buttons.ALL) {
			DEFAULT_CONTROLS.put(b, new ButtonElement());
		}
	}
	
	private static Layout currentLayout = new Default();
	
	private static HashSet<Layout> registry = new HashSet<Layout>();
	
	protected final HashMap<Control, LayoutElement> controls;
	
	private String name;
	
	public transient final boolean isDefault;
	
	public abstract LayoutWrapper getWrapper(ControllerStateWrapper state);
	
	public Layout(String name, HashMap<Control, LayoutElement> controls) {
		this(name, controls, false);
	}
	
	public Layout (String name, HashMap<Control, LayoutElement> controls, boolean isDefault) {
		this.name = name;
		this.controls = controls;
		this.isDefault = isDefault;
	}
	
	public abstract class LayoutWrapper extends ControllerStateWrapper{

		public LayoutWrapper(ControllerStateWrapper state) {
			super(state);
			interpret();
		}
		
		protected LayoutWrapper interpret() {
			if(Minecraft.getMinecraft() != null) {
				if(mc.currentScreen == null) {
					leftStickX = -leftStickX; //X axis for player movment is inverted when minecraft first processes it, so we must invert it back to make movement normal
					rightStickY = -rightStickY; //Y axis for camera movement is inverted when minecraft first processes it, so we must invert it back to make camera movement normal
				}
				
				for(Control c : controls.keySet()) {
					if(c.getControl() instanceof Axis) {
						AxisElement e = (AxisElement)controls.get(c);
						Axis axis = (Axis) c.getControl();
						switch(axis) {
							case L_X:
								this.leftStickX = e.eval(leftStickX);
								break;
							case L_Y:
								this.leftStickY = e.eval(leftStickY);
								break;
							case R_X:
								this.rightStickX = e.eval(rightStickX);
								break;
							case R_Y:
								this.rightStickY = e.eval(rightStickY);
								break;
							case LT:
								this.leftTrigger = e.eval(leftTrigger);
								break;
							case RT:
								this.rightTrigger = e.eval(rightTrigger);
								break;
							default:
								Minejoy.LOGGER.log(Level.WARN, "Unknown axis " + axis);
								break;
						}
					}
					else if (c.getControl() instanceof Button) {
						ButtonElement e = (ButtonElement)controls.get(c);
						Button button = (Button) c.getControl();
						switch(button) {
							case A:
								a = e.getValue(a);
								break;
							case B:
								b = e.getValue(b);
								break;
							case BACK:
								back = e.getValue(back);
								break;
							case D_PAD_DOWN:
								dpadDown = e.getValue(dpadDown);
								break;
							case D_PAD_LEFT:
								dpadLeft = e.getValue(dpadLeft);
								break;
							case D_PAD_RIGHT:
								dpadRight = e.getValue(dpadRight);
								break;
							case D_PAD_UP:
								dpadUp = e.getValue(dpadUp);
								break;
							case GUIDE:
								guide = e.getValue(guide);
								break;
							case LB:
								lb = e.getValue(lb);
								break;
							case LEFT_STICK_BUTTON:
								leftStickClick = e.getValue(leftStickClick);
								break;
							case RB:
								rb = e.getValue(rb);
								break;
							case RIGHT_STICK_BUTTON:
								rightStickClick = e.getValue(rightStickClick);
								break;
							case START:
								start = e.getValue(start);
								break;
							case X:
								x = e.getValue(x);
								break;
							case Y:
								y = e.getValue(y);
								break;
							default:
								Minejoy.LOGGER.log(Level.WARN, "Unknown button: " + button);
								break;
						}
					}
				}
				
			}
			return this;
		}
		
	}
	
	public static final Layout getLayout() {
		return currentLayout;
	}
	
	public final String getName() {
		return name;
	}
	
	public final String toString() {
		return getName();
	}
	
	public static final Layout getLayout(String name) {
		for(Layout layout : registry) {
			if(layout.getName().equals(name)) {
				return layout;
			}
		}
		throw new NoSuchElementException(name);
	}
	
	public static final void register(Layout layout) {
		if(!registry.contains(layout)) {
			registry.add(layout);
		}
	}
	
	public static final void setLayout(Layout l) {
		currentLayout = l;
	}

}

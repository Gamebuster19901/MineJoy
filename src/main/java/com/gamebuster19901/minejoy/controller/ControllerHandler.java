package com.gamebuster19901.minejoy.controller;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.Display;

import com.gamebuster19901.minejoy.Minejoy;
import com.gamebuster19901.minejoy.controller.layout.Layout;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import com.studiohartman.jamepad.ControllerUnpluggedException;

import net.minecraftforge.common.MinecraftForge;

public enum ControllerHandler {
	INSTANCE;
	
	private boolean previouslyInitialized = false;
	private boolean gameHasFocus = false;
	
	private final Thread CONTROLLER_THREAD = new Thread() {
		public void run() {
			try {
				while(Minejoy.isEnabled()) {
					if(canSendControllerEvents()) {
						try {
							Thread.sleep(1);
							if (Thread.interrupted()) {
							      throw new InterruptedException();
							}
						} catch (InterruptedException e) {
							Minejoy.setAvailibility(false);
							break;
						}
						ControllerStateWrapper state = getActiveControllerState();
						
						MinecraftForge.EVENT_BUS.post(new ControllerEvent.Pre(activeController, state, getActiveControllerIndex()));
						
						state.leftStickJustClicked = !lastState.leftStickClick && state.leftStickClick;
						state.rightStickJustClicked = !lastState.rightStickClick &&  state.rightStickClick;
						state.aJustPressed = !lastState.a && state.a;
						state.bJustPressed = !lastState.b && state.b;
						state.xJustPressed = !lastState.x && state.x;
						state.yJustPressed = !lastState.y && state.y;
						state.lbJustPressed = !lastState.lb && state.lb;
						state.rbJustPressed = !lastState.rb && state.rb;
						state.startJustPressed = !lastState.start && state.start;
						state.backJustPressed = !lastState.back && state.back;
						state.guideJustPressed = !lastState.guide && state.guide;
						state.dpadUpJustPressed = !lastState.dpadUp && state.dpadUp;
						state.dpadDownJustPressed = !lastState.dpadDown && state.dpadDown;
						state.dpadLeftJustPressed = !lastState.dpadLeft && state.dpadLeft;
						state.dpadRightJustPressed = !lastState.dpadRight && state.dpadRight;
						
						state.leftTriggerJustReachedThreshold = lastState.leftTrigger <= 0.5 && state.leftTrigger > 0.5;
						state.leftTriggerJustStoppedInputting = lastState.leftTrigger > 0.5 && state.leftTrigger <= 0.5;
						state.rightTriggerJustReachedThreshold = lastState.rightTrigger <= 0.5 && state.rightTrigger > 0.5;
						state.rightTriggerJustStoppedInputting = lastState.rightTrigger > 0.5 && state.rightTrigger <= 0.5;
						
						lastState = state;
						
						MinecraftForge.EVENT_BUS.post(new ControllerEvent.Post(activeController, state, getActiveControllerIndex()));
					}
				}
			}
			catch(Exception e) {
				RuntimeException ex = new RuntimeException(e);
				if(Minejoy.isEnabled()) {
					throw ex;
				}
				else {
					ex = new RuntimeException("Minejoy had an exception while processing inputs while shutting down, we can usually ignore this, but we will print the stacktrace just in case", e);
					Minejoy.LOGGER.catching(ex);
				}
			}
		}
	};
	
	private final Thread ACTIVE_WINDOW_CHECKER = new Thread() {
		public void run() {
			while(Minejoy.isEnabled()) {
				try {
					gameHasFocus = Display.isCreated() && Display.isActive();
				}
				catch(IllegalStateException e) {
					gameHasFocus = false;
				}
			}
		}
	};
	
	private ControllerManager controllerManager;
	private volatile ControllerStateWrapper lastState = ControllerStateWrapper.DISCONNECTED_CONTROLLER;
	private int activeController = 0;
	
	public void init(){
		controllerManager = new ControllerManager();
		controllerManager.initSDLGamepad();
		if(!previouslyInitialized) {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					Thread.currentThread().setName("Minejoy Shutdown Thread");
					Minejoy.LOGGER.log(Level.INFO, "Game ended, shutting down Minejoy and Jamepad!");
					if(CONTROLLER_THREAD.isAlive()) {
						Minejoy.setAvailibility(false);
						while(CONTROLLER_THREAD.isAlive());
						while(ACTIVE_WINDOW_CHECKER.isAlive());
					}
					ControllerHandler.this.controllerManager.quitSDLGamepad();
				}
			});
			previouslyInitialized = true;
		}
		int index = Minejoy.getConfig().getInt("defaultController", "", 0, 0, Integer.MAX_VALUE, "");
		if(isControllerIndexPluggedIn(index)) {
			activeController = index;
		}
		CONTROLLER_THREAD.setName("Minejoy ControllerEventNoGL Thread");
		ACTIVE_WINDOW_CHECKER.setName("Minejoy Active Display Checker");
		CONTROLLER_THREAD.setDaemon(true);
		ACTIVE_WINDOW_CHECKER.setDaemon(true);
		ACTIVE_WINDOW_CHECKER.setPriority(1);
		CONTROLLER_THREAD.start();
		ACTIVE_WINDOW_CHECKER.start();
		vibrate(activeController, 1f, 1f, 1500);
	}
	
	public void disable() {
		Minejoy.LOGGER.log(Level.INFO, "Minejoy disabled, shutting down Minejoy and Jamepad!");
		if(CONTROLLER_THREAD.isAlive()) {
			Minejoy.setAvailibility(false);
			while(CONTROLLER_THREAD.isAlive());
		}
		ControllerHandler.this.controllerManager.quitSDLGamepad();
	}
	
	public ControllerIndex getActiveControllerIndex(){
		return controllerManager.getControllerIndex(activeController);
	}
	
	public synchronized ControllerStateWrapper getActiveControllerState() {
		return new ControllerStateWrapper(controllerManager.getState(activeController));
	}
	
	public synchronized Layout.LayoutWrapper getModifiedActiveContorllerState() {
		if(controllerManager.getState(activeController).isConnected) {
			return Layout.getLayout().getWrapper(getActiveControllerState());
		}
		return Layout.getLayout().getWrapper(ControllerStateWrapper.DISCONNECTED_CONTROLLER);
	}
	
	public int getActiveController() {
		return activeController;
	}
	
	public void setActiveController(int index) {
		activeController = index;
	}
	
	/**
	 * Should only be called on ControllerEvent.Pre, or it will return the current state instead
	 * of the previous state.
	 * 
	 * This wrapper will reflect the unmodified state of the controller 1 millisecond ago
	 */
	public ControllerStateWrapper getLastControllerState() {
		return lastState;
	}
	
	/**
	 * Should only be called on ControllerEvent.Pre, or it will return the current state instead
	 * of the previous state.
	 * 
	 * This wrapper will reflect the state of the controller 1 millisecond ago, as modified by any enabled layouts
	 * 
	 * @see com.gamebuster19901.minejoy.controller.layout.Layout
	 */
	public Layout.LayoutWrapper getModifiedLastControllerState(){
		return Layout.getLayout().getWrapper(lastState);
	}
	
	/**
	 * Should only be called on ControllerEvent.Pre, or it will return the current state instead
	 * of the previous state.
	 * 
	 * This wrapper will reflect the state of the controller 1 tick ago, as modified by any enabled layouts
	 * 
	 * @see com.gamebuster19901.minejoy.controller.layout.Layout
	 */
	public Layout.LayoutWrapper getModifiedLastControllerGLState() {
		return Layout.getLayout().getWrapper(lastState);
	}
	
	public ControllerIndex getControllerIndex(int index){
		return controllerManager.getControllerIndex(index);
	}
	
	public boolean isControllerIndexPluggedIn(int index) {
		return getControllerIndex(index).isConnected();
	}
	
	public boolean isActiveControllerPluggedIn() {
		return getControllerIndex(getActiveControllerIndex().getIndex()).isConnected();
	}
	
	public ArrayList<ControllerState> getAllControllerStates(){
		ArrayList<ControllerState> ret = new ArrayList<ControllerState>();
		int m = controllerManager.getNumControllers();
		for(int i = 0; i < m; i++) {
			ret.add(controllerManager.getState(i));
		}
		return ret;
	}
	
	public String getControllerName(int index) {
		try {
			return controllerManager.getControllerIndex(index).getName();
		} catch (ControllerUnpluggedException e) {
			return "Unplugged controller";
		}
	}
	
	public void vibrate(int index, float leftMagnitude, float rightMagnitude, int milliseconds){
		ControllerIndex controller = controllerManager.getControllerIndex(index);
		try {
			controller.doVibration(leftMagnitude, rightMagnitude, milliseconds);
		} catch (ControllerUnpluggedException e) {
			Minejoy.LOGGER.log(Level.WARN, "Controller " + index + " was unplugged while vibrating");
		}
	}
	
	public boolean canSendControllerEvents() {
		return gameHasFocus && Minejoy.isEnabled() && getActiveControllerIndex().isConnected() && ControllerMouse.INSTANCE.isMouseWithinBounds();
	}

	public ControllerStateWrapper getControllerState(int controller) {
		return new ControllerStateWrapper(this.controllerManager.getState(controller));
	}
	
	public Layout.LayoutWrapper getModifiedControllerState(int controller) {
		return Layout.getLayout().getWrapper(getControllerState(controller));
	}
}

package com.gamebuster19901.minejoy.controller;

import java.util.ArrayList;
import java.util.logging.Level;

import org.lwjgl.opengl.Display;

import com.gamebuster19901.minejoy.Minejoy;
import com.gamebuster19901.minejoy.controller.layout.Layout;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import com.studiohartman.jamepad.ControllerUnpluggedException;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public enum ControllerHandler {
	INSTANCE;
	
	private boolean previouslyInitialized = false;
	
	private final Thread CONTROLLER_THREAD = new Thread() {
		public void run() {
			Thread.currentThread().setName("Minejoy Controller Thread");
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
						
						MinecraftForge.EVENT_BUS.post(new ControllerEventNoGL.Pre(activeController, state, getActiveControllerIndex()));
						
						state.leftStickJustClicked = !lastNoGLState.leftStickClick && state.leftStickClick;
						state.rightStickJustClicked = !lastNoGLState.rightStickClick &&  state.rightStickClick;
						state.aJustPressed = !lastNoGLState.a && state.a;
						state.bJustPressed = !lastNoGLState.b && state.b;
						state.xJustPressed = !lastNoGLState.x && state.x;
						state.yJustPressed = !lastNoGLState.y && state.y;
						state.lbJustPressed = !lastNoGLState.lb && state.lb;
						state.rbJustPressed = !lastNoGLState.rb && state.rb;
						state.startJustPressed = !lastNoGLState.start && state.start;
						state.backJustPressed = !lastNoGLState.back && state.back;
						state.guideJustPressed = !lastNoGLState.guide && state.guide;
						state.dpadUpJustPressed = !lastNoGLState.dpadUp && state.dpadUp;
						state.dpadDownJustPressed = !lastNoGLState.dpadDown && state.dpadDown;
						state.dpadLeftJustPressed = !lastNoGLState.dpadLeft && state.dpadLeft;
						state.dpadRightJustPressed = !lastNoGLState.dpadRight && state.dpadRight;
						
						state.leftTriggerJustReachedThreshold = lastNoGLState.leftTrigger <= 0.5 && state.leftTrigger > 0.5;
						state.leftTriggerJustStoppedInputting = lastNoGLState.leftTrigger > 0.5 && state.leftTrigger <= 0.5;
						state.rightTriggerJustReachedThreshold = lastNoGLState.rightTrigger <= 0.5 && state.rightTrigger > 0.5;
						state.rightTriggerJustStoppedInputting = lastNoGLState.rightTrigger > 0.5 && state.rightTrigger <= 0.5;
						
						lastNoGLState = state;
						
						MinecraftForge.EVENT_BUS.post(new ControllerEventNoGL.Post(activeController, state, getActiveControllerIndex()));
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
	
	@SubscribeEvent
	public final void everyTick(ClientTickEvent e) {
		if(canSendControllerEvents()) {
			ControllerStateWrapper state = getActiveControllerState();
			
			MinecraftForge.EVENT_BUS.post(new ControllerEvent.Pre(activeController, state, getActiveControllerIndex()));
			
			state.leftStickJustClicked = !lastGLState.leftStickClick && state.leftStickClick;
			state.rightStickJustClicked = !lastGLState.rightStickClick &&  state.rightStickClick;
			state.aJustPressed = !lastGLState.a && state.a;
			state.bJustPressed = !lastGLState.b && state.b;
			state.xJustPressed = !lastGLState.x && state.x;
			state.yJustPressed = !lastGLState.y && state.y;
			state.lbJustPressed = !lastGLState.lb && state.lb;
			state.rbJustPressed = !lastGLState.rb && state.rb;
			state.startJustPressed = !lastGLState.start && state.start;
			state.backJustPressed = !lastGLState.back && state.back;
			state.guideJustPressed = !lastGLState.guide && state.guide;
			state.dpadUpJustPressed = !lastGLState.dpadUp && state.dpadUp;
			state.dpadDownJustPressed = !lastGLState.dpadDown && state.dpadDown;
			state.dpadLeftJustPressed = !lastGLState.dpadLeft && state.dpadLeft;
			state.dpadRightJustPressed = !lastGLState.dpadRight && state.dpadRight;
			
			state.leftTriggerJustReachedThreshold = lastGLState.leftTrigger <= 0.5 && state.leftTrigger > 0.5;
			state.leftTriggerJustStoppedInputting = lastGLState.leftTrigger > 0.5 && state.leftTrigger <= 0.5;
			state.rightTriggerJustReachedThreshold = lastGLState.rightTrigger <= 0.5 && state.rightTrigger > 0.5;
			state.rightTriggerJustStoppedInputting = lastGLState.rightTrigger > 0.5 && state.rightTrigger <= 0.5;
			
			lastGLState = state;
			
			MinecraftForge.EVENT_BUS.post(new ControllerEvent.Post(activeController, state, getActiveControllerIndex()));
		}
	}
	
	private ControllerManager controllerManager;
	private volatile ControllerStateWrapper lastNoGLState = ControllerStateWrapper.DISCONNECTED_CONTROLLER;
	private ControllerStateWrapper lastGLState = ControllerStateWrapper.DISCONNECTED_CONTROLLER;
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
		CONTROLLER_THREAD.setDaemon(true);
		CONTROLLER_THREAD.start();
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
	 * Should only be called on ControllerEventNoGL.Pre, or it will return the current state instead
	 * of the previous state.
	 * 
	 * This wrapper will reflect the unmodified state of the controller 1 millisecond ago
	 */
	public ControllerStateWrapper getLastControllerNoGLState() {
		return lastNoGLState;
	}
	
	/**
	 * Should only be called on ControllerEvent.Pre, or it will return the current state instead
	 * of the previous state.
	 * 
	 * This wrapper will reflect the unmodified state of the controller 1 tick ago
	 */
	public ControllerStateWrapper getLastControllerGLState() {
		return lastGLState;
	}
	
	/**
	 * Should only be called on ControllerEventNoGL.Pre, or it will return the current state instead
	 * of the previous state.
	 * 
	 * This wrapper will reflect the state of the controller 1 millisecond ago, as modified by any enabled layouts
	 * 
	 * @see com.gamebuster19901.minejoy.controller.layout.Layout
	 */
	public Layout.LayoutWrapper getModifiedLastControllerNoGLState(){
		return Layout.getLayout().getWrapper(lastNoGLState);
	}
	
	/**
	 * Should only be called on ControllerEvent.Pre, or it will return the current state instead
	 * of the previous state.
	 * 
	 * This wrapper will reflect the unmodified state of the controller 1 tick ago, as modified by any enabled layouts
	 * 
	 * @see com.gamebuster19901.minejoy.controller.layout.Layout
	 */
	public Layout.LayoutWrapper getModifiedLastControllerGLState() {
		return Layout.getLayout().getWrapper(lastGLState);
	}
	
	public ControllerIndex getControllerIndex(int index){
		return controllerManager.getControllerIndex(index);
	}
	
	public boolean isControllerIndexPluggedIn(int index) {
		return getControllerIndex(index).isConnected();
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
	
	public void vibrate(int index, float leftMagnatude, float rightMagnatude, int milliseconds){
		new Thread() {
			public void run() {
				int ms = milliseconds;
				ControllerIndex unsafe = controllerManager.getControllerIndex(index);
				if(leftMagnatude == 0 && rightMagnatude == 0) {
					if(unsafe.isVibrating() || unsafe.isConnected()) {
						unsafe.stopVibration();
					}
				}
				else {
					try {
						unsafe.startVibration(leftMagnatude, rightMagnatude);
					} catch (ControllerUnpluggedException e1) {} //while loop will fail because unsafe.isVibrating() will be false, we should swallow
					while(ms > 0 && unsafe.isVibrating()) {
						try {
							Thread.sleep(1);
							ms--;
						} catch (InterruptedException e) {
							unsafe.stopVibration();
							break;
						}
					}
					unsafe.stopVibration();
				}
			}
		}.start();

	}
	
	public boolean canSendControllerEvents() {
		return Minejoy.isEnabled() && controllerManager.getNumControllers() > 0 && Display.isCreated() && Display.isActive();
	}

	public ControllerStateWrapper getControllerState(int controller) {
		return new ControllerStateWrapper(this.controllerManager.getState(controller));
	}
	
	public Layout.LayoutWrapper getModifiedControllerState(int controller) {
		return Layout.getLayout().getWrapper(getControllerState(controller));
	}
}

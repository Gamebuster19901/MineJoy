package com.gamebuster19901.minejoy.controller;

import java.util.ArrayList;

import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import com.studiohartman.jamepad.ControllerUnpluggedException;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public enum ControllerHandler {
	INSTANCE;
	private final Thread CONTROLLER_THREAD = new Thread() {
		public void run() {
			while(true) {
				if(controllerManager.getNumControllers() > 0) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						break;
					}
					ControllerStateWrapper state = getActiveControllerState();
					
					state.leftStickJustClicked = !lastState.leftStickClick && state.leftStickClick;
					state.rightStickJustClicked = !lastState.rightStickClick &&  state.rightStickClick;
					state.aJustPressed = !lastState.a && state.a;
					System.out.println(state.aJustPressed);
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
					
					MinecraftForge.EVENT_BUS.post(new ControllerEvent(activeController, state, getActiveControllerIndex()));
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
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Game ended, shutting down Minejoy and Jamepad!");
				if(CONTROLLER_THREAD.isAlive()) {
					CONTROLLER_THREAD.interrupt();
					CONTROLLER_THREAD.stop();
				}
				ControllerHandler.this.controllerManager.quitSDLGamepad();
			}
		});
		CONTROLLER_THREAD.start();
		vibrate(activeController, 1f, 1f, 1500);
	}
	
	public ControllerIndex getActiveControllerIndex(){
		return controllerManager.getControllerIndex(activeController);
	}
	
	public synchronized ControllerStateWrapper getActiveControllerState() {
		return new ControllerStateWrapper(controllerManager.getState(activeController));
	}
	
	public int getActiveController() {
		return activeController;
	}
	
	public void setActiveController(int index) {
		activeController = index;
	}
	
	public ControllerState getControllerState(int index) {
		return controllerManager.getState(index);
	}
	
	public ControllerIndex getControllerIndex(int index) throws ControllerUnpluggedException{
		return controllerManager.getControllerIndex(index);
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
	
	@SubscribeEvent
	public void onController(ControllerEvent e) {
		lastState = e.getControllerState();
	}
	
}

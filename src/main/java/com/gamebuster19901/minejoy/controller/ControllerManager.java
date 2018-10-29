package com.gamebuster19901.minejoy.controller;

import org.lwjgl.input.Controllers;

public enum ControllerManager {
	INSTANCE;

	public ControllerState getState(int index) {
		return new ControllerState(Controllers.getController(index));
	}

	public int getNumControllers() {
		return Controllers.getControllerCount();
	}
}

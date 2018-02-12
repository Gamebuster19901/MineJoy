package com.gamebuster19901.minejoy.binding;

import com.gamebuster19901.minejoy.controller.ControllerHandler;
import com.gamebuster19901.minejoy.controller.ControllerStateWrapper;
import com.gamebuster19901.minejoy.controller.layout.Layout;

import net.minecraft.client.settings.KeyBinding;

public class ControllerButtonBinding extends KeyBinding{
	
	protected int button;
	protected boolean wasLastPressController;
	
    public ControllerButtonBinding(KeyBinding binding, int button)
    {
        super(binding.getKeyDescription(), binding.getKeyCode(), binding.getKeyCategory());
        this.button = button;
    }
    
    
    
    @Override
    public boolean isKeyDown()
    {
    	if(super.isKeyDown()) {
    		wasLastPressController = false;
    		return true;
    	}
    	else {
    		boolean pressed = ControllerStateWrapper.Button.getButton(button).isPressed(Layout.getLayout().getWrapper(ControllerHandler.INSTANCE.getActiveControllerState()));
    		wasLastPressController = pressed;
    		return pressed;
    	}
    }
    
    public boolean lastPressWasController() {
    	return wasLastPressController;
    }
	
}

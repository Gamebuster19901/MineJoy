package com.gamebuster19901.minejoy.binding;

import com.gamebuster19901.minejoy.controller.ControllerHandler;
import com.gamebuster19901.minejoy.controller.ControllerStateWrapper;
import com.gamebuster19901.minejoy.controller.layout.Layout;

import net.minecraft.client.settings.KeyBinding;

public class ControllerButtonBinding extends KeyBinding{
	
	protected int button;
	
    public ControllerButtonBinding(KeyBinding binding, int button)
    {
        super(binding.getKeyDescription(), binding.getKeyCode(), binding.getKeyCategory());
        this.button = button;
    }
    
    @Override
    public boolean isKeyDown()
    {
    	if(super.isKeyDown()) {
    		return true;
    	}
    	else {
    		return ControllerStateWrapper.Button.getButton(button).isPressed(Layout.getLayout().getWrapper(ControllerHandler.INSTANCE.getActiveControllerState()));
    	}
    }
	
}

package com.gamebuster19901.minejoy.binding;

import net.minecraft.client.settings.KeyBinding;

public class ControllerButtonBinding extends KeyBinding{
	
	
	
    public ControllerButtonBinding(String description, int keyCode, String category)
    {
        super(description, keyCode, category);
    }
	
    /**
     * Convenience constructor for creating KeyBindings with keyConflictContext set.
     */
    public ControllerButtonBinding(String description, net.minecraftforge.client.settings.IKeyConflictContext keyConflictContext, int keyCode, String category)
    {
        this(description, keyConflictContext, net.minecraftforge.client.settings.KeyModifier.NONE, keyCode, category);
    }

    /**
     * Convenience constructor for creating KeyBindings with keyConflictContext and keyModifier set.
     */
    public ControllerButtonBinding(String description, net.minecraftforge.client.settings.IKeyConflictContext keyConflictContext, net.minecraftforge.client.settings.KeyModifier keyModifier, int keyCode, String category)
    {
        super(description, keyConflictContext, keyModifier, keyCode, category);
    }
	
}

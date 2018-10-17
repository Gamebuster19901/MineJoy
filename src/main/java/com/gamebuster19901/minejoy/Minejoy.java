package com.gamebuster19901.minejoy;

import static com.gamebuster19901.minejoy.Minejoy.MODID;
import static com.gamebuster19901.minejoy.Minejoy.MODNAME;
import static com.gamebuster19901.minejoy.Minejoy.VERSION;

import java.awt.HeadlessException;

import com.gamebuster19901.minejoy.binding.ControllerButtonBinding;
import com.gamebuster19901.minejoy.config.MineJoyConfig;
import com.gamebuster19901.minejoy.controller.ControllerHandler;
import com.gamebuster19901.minejoy.controller.ControllerMouse;
import com.gamebuster19901.minejoy.controller.ControllerStateWrapper;
import com.gamebuster19901.minejoy.controller.MovementInputFromOptionsMinejoy;
import com.gamebuster19901.minejoy.controller.PlayerControllerMPMinejoy;
import com.gamebuster19901.minejoy.controller.layout.Default;
import com.gamebuster19901.minejoy.controller.layout.Layout;
import com.gamebuster19901.minejoy.controller.layout.SouthPaw;
import com.gamebuster19901.minejoy.gui.ControlGUIHandler;
import com.gamebuster19901.minejoy.gui.GuiPossibleModIncompatability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = MODID, name = MODNAME, version = VERSION, clientSideOnly = true)
public class Minejoy {

	
	public static final String MODID = "minejoy";
	public static final String MODNAME = "MineJoy";
	public static final String VERSION = "0.8.0.0";
	private static boolean enabled = true;
	
	@Mod.Instance()
	public static Minejoy instance;
	
	private static Configuration CONFIG;
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent e){
		if(e.getSide() == Side.SERVER) {
			throw new HeadlessException("Remove MineJoy from the server, it is for clients only");
		}
		GameSettings settings = Minecraft.getMinecraft().gameSettings;
		
		/*
		 * 
		 * ControllerButtonBinding only seems to work on mouse buttons, unsure why...
		 * 
		 */
		
		settings.keyBindUseItem = new ControllerButtonBinding(settings.keyBindUseItem, ControllerStateWrapper.Button.LT.getIndex());
		settings.keyBindAttack = new ControllerButtonBinding(settings.keyBindAttack, ControllerStateWrapper.Button.RT.getIndex());
		
		CONFIG = new Configuration(e.getSuggestedConfigurationFile());
		ControllerHandler.INSTANCE.init();
		
		Layout.register(new Default());
		Layout.register(new SouthPaw());
		
		MinecraftForge.EVENT_BUS.register(ControllerHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(ControlGUIHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(ControllerMouse.INSTANCE);
		MinecraftForge.EVENT_BUS.register(MovementInputFromOptionsMinejoy.getInstance());
		MinecraftForge.EVENT_BUS.register(PlayerControllerMPMinejoy.REGISTRY_INSTANCE);
		MinecraftForge.EVENT_BUS.register(MineJoyConfig.reflection.INSTANCE);
		
		
		//Main.main(new String[]{}); //debug
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e){

	}
			
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) throws IllegalArgumentException, IllegalAccessException {
		GuiPossibleModIncompatability.overwriteableControllers.add(PlayerControllerMP.class); //Minejoy is designed to overwrite the vanilla controller
		GuiPossibleModIncompatability.compatibleControllers.add(PlayerControllerMPMinejoy.class); //Minejoy is compatible with itself, no need to overwrite itself
	}	
	
	public static Configuration getConfig() {
		return CONFIG;
	}
	
	public static void setAvailibility(boolean enabled) {
		Minejoy.enabled = enabled;
	}
	
	public static boolean isEnabled() {
		return enabled;
	}
}

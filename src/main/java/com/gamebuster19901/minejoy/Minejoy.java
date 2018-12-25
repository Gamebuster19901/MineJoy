package com.gamebuster19901.minejoy;

import static com.gamebuster19901.minejoy.Minejoy.MODID;
import static com.gamebuster19901.minejoy.Minejoy.MODNAME;
import static com.gamebuster19901.minejoy.Minejoy.VERSION;

import java.awt.HeadlessException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gamebuster19901.minejoy.config.MineJoyConfig;
import com.gamebuster19901.minejoy.controller.ControllerHandler;
import com.gamebuster19901.minejoy.controller.MovementInputFromOptionsMinejoy;
import com.gamebuster19901.minejoy.controller.PlacementHandler;
import com.gamebuster19901.minejoy.controller.layout.Default;
import com.gamebuster19901.minejoy.controller.layout.Layout;
import com.gamebuster19901.minejoy.controller.ControllerMouse;
import com.gamebuster19901.minejoy.controller.DamageHandler;
import com.gamebuster19901.minejoy.gui.ControlGUIHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = MODID, name = MODNAME, version = VERSION, clientSideOnly = true)
public class Minejoy {

	public static final String MODID = "minejoy";
	public static final String MODNAME = "MineJoy";
	public static final String VERSION = "0.8.0.0";
	public static final Logger LOGGER = LogManager.getLogger(MODNAME);
	private static volatile boolean enabled = true;
	
	@Mod.Instance()
	public static Minejoy instance;
	
	private static Configuration CONFIG;
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent e){
		if(e.getSide() == Side.SERVER) {
			throw new HeadlessException("Remove MineJoy from the server, it is for clients only");
		}
		
		CONFIG = new Configuration(e.getSuggestedConfigurationFile());
		ControllerHandler.INSTANCE.init();
		
		Layout.register(new Default());
		
		MinecraftForge.EVENT_BUS.register(ControllerHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(ControlGUIHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(DamageHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(PlacementHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(ControllerMouse.INSTANCE);
		MinecraftForge.EVENT_BUS.register(MovementInputFromOptionsMinejoy.getInstance());
		MinecraftForge.EVENT_BUS.register(MineJoyConfig.reflection.INSTANCE);
		
	}
	
	@SuppressWarnings("unused")
	@EventHandler
	public void init(FMLInitializationEvent e){

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

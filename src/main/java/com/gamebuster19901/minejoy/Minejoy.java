package com.gamebuster19901.minejoy;

import static com.gamebuster19901.minejoy.Minejoy.MODID;
import static com.gamebuster19901.minejoy.Minejoy.MODNAME;
import static com.gamebuster19901.minejoy.Minejoy.VERSION;

import java.awt.HeadlessException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;

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
	public static final String VERSION = "0.0.0.0";
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent e) throws LWJGLException {
		if(e.getSide() == Side.SERVER) {
			throw new HeadlessException("Remove MineJoy from the server, it is for clients only");
		}
		Controllers.create();
		Controllers.poll();
		for(int i = 0; i < Controllers.getControllerCount(); i++) {
			System.out.println(Controllers.getController(i).getName());
		}
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e){

	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		
	}
	
	
}

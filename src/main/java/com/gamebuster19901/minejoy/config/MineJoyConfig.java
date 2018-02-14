package com.gamebuster19901.minejoy.config;

import static com.gamebuster19901.minejoy.Minejoy.MODID;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.gamebuster19901.minejoy.gui.GuiControllerOptions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@Config(modid = MODID)
public class MineJoyConfig{
	
	public static interface reflection{
		Field configButton = ReflectionHelper.findField(GuiModList.class, "configModButton");
		Field selectedMod = ReflectionHelper.findField(GuiModList.class, "selectedMod");
		Method getModId = ReflectionHelper.findMethod(ModContainer.class, "getModId", "getModId");
		
		public MineJoyConfig INSTANCE = new MineJoyConfig();
	}
	
	public static int defaultController = 0;
	
	public static String controllerLayout = "com.gamebuster19901.minejoy.controller.layout.Default";
	
	@Config.RangeDouble(min = 0, max = 1)
	public static float leftStickDeadZone = 0.2f;
	
	@Config.RangeDouble(min = 0, max = 1)
	public static float rightStickDeadZone = 0.2f;
	
	public static boolean cameraYAxisInverted = false;
	
	public static boolean cameraXAxisInverted = false;
	
	public static boolean movementYAxisInverted = false;
	
	public static boolean movementXAxisInverted = false;
	
	public static boolean mouseYAxisInverted = false;
	
	public static boolean mouseXAxisInverted = false;
	
	@SubscribeEvent
	public void onModListConfigButtonClick(ActionPerformedEvent e) {
		if(e.getGui() instanceof GuiModList) {
			try {
				if(e.getButton().equals(reflection.configButton.get(e.getGui()))) {
					if(reflection.getModId.invoke((ModContainer)reflection.selectedMod.get(e.getGui())).equals(MODID)) {
						e.setCanceled(true);
						e.getButton().playPressSound(Minecraft.getMinecraft().getSoundHandler());
						Minecraft.getMinecraft().displayGuiScreen(new GuiControllerOptions(e.getGui(), I18n.format("options.choosejoy")));
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e1) {
				throw new AssertionError(e1);
			}
		}
	}
	
}

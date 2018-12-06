package com.gamebuster19901.minejoy.controller.layout;

import org.mariuszgromada.math.mxparser.Expression;

import com.gamebuster19901.minejoy.gson.LayoutElementAdapter;
import com.google.gson.annotations.JsonAdapter;

import net.minecraft.client.Minecraft;

@JsonAdapter(LayoutElementAdapter.class)
public class AxisElement extends LayoutElement<Float>{
	
	private boolean invertedInGame;
	private boolean invertedInGui;

	public AxisElement() {
		super();
	}
	
	public AxisElement(boolean invertedInGame, boolean invertedInGui, String expression) {
		super(expression);
		this.invertedInGame = invertedInGame;
		this.invertedInGui = invertedInGui;
	}
	
	public AxisElement(boolean invertedInGame, boolean invertedInGui, Expression expression) {
		super(expression);
		this.invertedInGame = invertedInGame;
		this.invertedInGui = invertedInGui;
	}
	
	@Override
	public Float getValue() {
		boolean inverted = (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().currentScreen == null) ? invertedInGame : invertedInGui;
		if(inverted) {
			return (float) -expressionValue;
		}
		return (float) expressionValue;
	}
	
	public boolean isInvertedInGame() {
		return invertedInGame;
	}
	
	public boolean isInvertedInGui() {
		return invertedInGui;
	}
}

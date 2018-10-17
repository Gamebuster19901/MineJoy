package com.gamebuster19901.minejoy.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

public class GuiControllerOptionsList extends GuiListExtended{
	
	protected List<GuiEntryController> entries;

	public GuiControllerOptionsList(Minecraft mcIn, int widthIn, int heightIn, List<GuiEntryController> entries) {
		super(mcIn, widthIn, heightIn, 32, heightIn - 55, 36);
		this.entries = entries;
		this.centerListVertically = true;
		this.setHasListHeader(false, (int)(mcIn.fontRenderer.FONT_HEIGHT * 1.5f));
	}
	
	public List<GuiEntryController> getList(){
		return entries;
	}
	
	@Override
	public GuiEntryController getListEntry(int index) {
		return getList().get(index);
	}
	
	@Override
    public int getListWidth()
    {
        return this.width;
    }

    @Override
    protected int getScrollBarX()
    {
        return this.right - 6;
    }

	@Override
	protected int getSize() {
		return this.getList().size();
	}

}

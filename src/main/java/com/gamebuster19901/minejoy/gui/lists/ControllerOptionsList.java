package com.gamebuster19901.minejoy.gui.lists;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.gui.GuiListExtended;

public class ControllerOptionsList<T extends IGuiListEntry> extends GuiListExtended{
	
	protected List<T> entries;

	public ControllerOptionsList(Minecraft mcIn, int widthIn, int heightIn, List<T> entries) {
		super(mcIn, widthIn, heightIn, 32, heightIn - 55, 36);
		this.entries = entries;
		this.centerListVertically = true;
		this.setHasListHeader(false, (int)(mcIn.fontRenderer.FONT_HEIGHT * 1.5f));
	}
	
	public List<T> getList(){
		return entries;
	}
	
	@Override
	public T getListEntry(int index) {
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

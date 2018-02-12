package com.gamebuster19901.minejoy.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.gamebuster19901.minejoy.binding.ControllerButtonBinding;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class PlayerControllerMPMinejoy extends PlayerControllerMP{

	@Deprecated
	public static final PlayerControllerMPMinejoy REGISTRY_INSTANCE = new PlayerControllerMPMinejoy(Minecraft.getMinecraft(), null);
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	private static final Field BLOCK = ReflectionHelper.findField(PlayerControllerMP.class, "currentBlock", "field_178895_c");
	
	private static final Method SYNC_CURRENT_PLAY_ITEM = ReflectionHelper.findMethod(PlayerControllerMP.class, "syncCurrentPlayItem", "func_78750_j");
	
	private PlayerControllerMPMinejoy(Minecraft mcIn, NetHandlerPlayClient netHandler) {
		super(mcIn, netHandler);
	}
	
	/**
	 * Resets current block damage
	 * 
	 * Overwriting the vanilla resetBlockRemoving method because vanilla calls it every tick if the keybindAttack
	 * is not pressed. It is technically not pressed if the right trigger is pulled even though we still want the
	 * block to get destroyed, so we must only allow vanilla to call this if the right trigger is not pressed.
	 */
	@Override
	public void resetBlockRemoving()
	{
		if (ControllerHandler.INSTANCE.getActiveControllerState().rightTrigger < 0.5) {
			super.resetBlockRemoving();
		}
		else {
			 RayTraceResult trace = Minecraft.getMinecraft().objectMouseOver;
			 if(trace.typeOfHit != RayTraceResult.Type.BLOCK) {
				 super.resetBlockRemoving();
			 }
			 else {
				 try {
					if (!(BLOCK.get(this).equals(trace.getBlockPos())) || trace.entityHit != null) {
						 super.resetBlockRemoving();
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new AssertionError(e);
				}
			}
		}
	}
	
	/**
	 * Overridden to allow  Minejoy to prevent sending packets to the server when it cancels right click events
	 * 
	 * If Minejoy is not calling this method, it uses vanilla implementation. See
	 */
	
	@Override
	public EnumActionResult processRightClickBlock(EntityPlayerSP player, WorldClient worldIn, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand){
		/*
		 * 
		 * Check if minejoy is calling this so we don't break other people's stuff
		 * 
		 */
		if(((ControllerButtonBinding)mc.gameSettings.keyBindUseItem).lastPressWasController()){
		
			try {
				SYNC_CURRENT_PLAY_ITEM.invoke(this);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new AssertionError(e);
			}
			
			ItemStack itemstack = player.getHeldItem(hand);
			float f = (float)(vec.x - (double)pos.getX());
			float f1 = (float)(vec.y - (double)pos.getY());
			float f2 = (float)(vec.z - (double)pos.getZ());
			boolean flag = false;
	
			if (!this.mc.world.getWorldBorder().contains(pos))
			{
				return EnumActionResult.FAIL;
			}
			else
			{
				net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock event = net.minecraftforge.common.ForgeHooks
						.onRightClickBlock(player, hand, pos, direction, net.minecraftforge.common.ForgeHooks.rayTraceEyeHitVec(player, getBlockReachDistance() + 1));
				if (event.isCanceled())
				{
					// Do not give the server a chance to fire the event, That way server event is dependant on client event.
					
					//mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
					
					return event.getCancellationResult();
				}
				EnumActionResult result = EnumActionResult.PASS;
	
				if (this.getCurrentGameType() != GameType.SPECTATOR)
				{
					EnumActionResult ret = itemstack.onItemUseFirst(player, worldIn, pos, hand, direction, f, f1, f2);
					if (ret != EnumActionResult.PASS)
					{
						// The server needs to process the item use as well. Otherwise onItemUseFirst won't ever be called on the server without causing weird bugs
						mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
						return ret;
					}
	
					IBlockState iblockstate = worldIn.getBlockState(pos);
					boolean bypass = player.getHeldItemMainhand().doesSneakBypassUse(worldIn, pos, player) && player.getHeldItemOffhand().doesSneakBypassUse(worldIn, pos, player);
	
					if ((!player.isSneaking() || bypass || event.getUseBlock() == net.minecraftforge.fml.common.eventhandler.Event.Result.ALLOW))
					{
						if (event.getUseBlock() != net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
						flag = iblockstate.getBlock().onBlockActivated(worldIn, pos, iblockstate, player, hand, direction, f, f1, f2);
						if (flag) result = EnumActionResult.SUCCESS;
					}
	
					if (!flag && itemstack.getItem() instanceof ItemBlock)
					{
						ItemBlock itemblock = (ItemBlock)itemstack.getItem();
	
						if (!itemblock.canPlaceBlockOnSide(worldIn, pos, direction, player, itemstack))
						{
							return EnumActionResult.FAIL;
						}
					}
				}
	
				mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
	
				if (!flag && getCurrentGameType() != GameType.SPECTATOR || event.getUseItem() == net.minecraftforge.fml.common.eventhandler.Event.Result.ALLOW)
				{
					if (itemstack.isEmpty())
					{
						return EnumActionResult.PASS;
					}
					else if (player.getCooldownTracker().hasCooldown(itemstack.getItem()))
					{
						return EnumActionResult.PASS;
					}
					else
					{
						if (itemstack.getItem() instanceof ItemBlock && !player.canUseCommandBlock())
						{
							Block block = ((ItemBlock)itemstack.getItem()).getBlock();
	
							if (block instanceof BlockCommandBlock || block instanceof BlockStructure)
							{
								return EnumActionResult.FAIL;
							}
						}
	
						if (getCurrentGameType().isCreative())
						{
							int i = itemstack.getMetadata();
							int j = itemstack.getCount();
							if (event.getUseItem() != net.minecraftforge.fml.common.eventhandler.Event.Result.DENY) {
							EnumActionResult enumactionresult = itemstack.onItemUse(player, worldIn, pos, hand, direction, f, f1, f2);
							itemstack.setItemDamage(i);
							itemstack.setCount(j);
							return enumactionresult;
							} else return result;
						}
						else
						{
							ItemStack copyForUse = itemstack.copy();
							if (event.getUseItem() != net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
							result = itemstack.onItemUse(player, worldIn, pos, hand, direction, f, f1, f2);
							if (itemstack.isEmpty()) net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copyForUse, hand);
							return result;
						}
					}
				}
				else
				{
					return EnumActionResult.SUCCESS;
				}
			}
		}
		else {
			return super.processRightClickBlock(player, worldIn, pos, direction, vec, hand);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerLoad(EntityJoinWorldEvent e) {
		if(e.getEntity() == Minecraft.getMinecraft().player) {
			EntityPlayerSP player = (EntityPlayerSP)e.getEntity();
			Minecraft.getMinecraft().playerController = getNewInstance();
		}
	}
	
	private PlayerControllerMPMinejoy getNewInstance() {
		return new PlayerControllerMPMinejoy(Minecraft.getMinecraft(), Minecraft.getMinecraft().getConnection());
	}

}

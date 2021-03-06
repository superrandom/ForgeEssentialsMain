package com.ForgeEssentials.commands;

import com.ForgeEssentials.core.PlayerInfo;
import com.ForgeEssentials.core.commands.ForgeEssentialsCommandBase;
import com.ForgeEssentials.util.FunctionHelper;
import com.ForgeEssentials.util.Localization;
import com.ForgeEssentials.util.OutputHandler;
import com.ForgeEssentials.util.TeleportCenter;
import com.ForgeEssentials.util.AreaSelector.Point;
import com.ForgeEssentials.util.AreaSelector.WarpPoint;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;

public class CommandTp extends ForgeEssentialsCommandBase
{

	/** Spawn point for each dimension */
	public static HashMap<Integer, Point> spawnPoints = new HashMap<Integer, Point>();

	@Override
	public String getCommandName()
	{
		return "tp";
	}

	@Override
	public void processCommandPlayer(EntityPlayer sender, String[] args)
	{
		if (args.length == 1)
		{
			EntityPlayer target = FunctionHelper.getPlayerFromPartialName(args[0]);
			if(PlayerSelector.hasArguments(args[0]))
			{
				target = PlayerSelector.matchOnePlayer(sender, args[0]);
			}
			if (target != null)
			{
				EntityPlayerMP player = (EntityPlayerMP) sender;
				PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(player.username);
				playerInfo.back = new WarpPoint(player);
				TeleportCenter.addToTpQue(new WarpPoint(target), player);
			}
			else
			{
				OutputHandler.chatError(sender, Localization.format(Localization.ERROR_NOPLAYER, args[0]));
			}
		}
		else if (args.length == 2)
		{
			EntityPlayerMP player = FunctionHelper.getPlayerFromPartialName(args[0]);
			if(PlayerSelector.hasArguments(args[0]))
			{
				player = PlayerSelector.matchOnePlayer(sender, args[0]);
			}
			EntityPlayer target = FunctionHelper.getPlayerFromPartialName(args[1]);
			if(PlayerSelector.hasArguments(args[1]))
			{
				target = PlayerSelector.matchOnePlayer(sender, args[1]);
			}
			if (player != null && target != null)
			{
				PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(player.username);
				playerInfo.back = new WarpPoint(player);
				TeleportCenter.addToTpQue(new WarpPoint(target), player);
			}
			else
			{
				if (player == null)
				{
					OutputHandler.chatError(sender, Localization.format(Localization.ERROR_NOPLAYER, args[0]));
				}
				if (target == null)
				{
					OutputHandler.chatError(sender, Localization.format(Localization.ERROR_NOPLAYER, args[1]));
				}
				return;
			}
		}
		else if (args.length >= 3)
		{
			if (args.length == 3)
			{
				int x = 0, y = 0, z = 0;
				try
				{
					x = new Integer(args[0]);
				}
				catch (NumberFormatException e)
				{
					OutputHandler.chatError(sender, Localization.format(Localization.ERROR_NAN, args[0]));
					return;
				}
				try
				{
					y = new Integer(args[1]);
				}
				catch (NumberFormatException e)
				{
					OutputHandler.chatError(sender, Localization.format(Localization.ERROR_NAN, args[1]));
					return;
				}
				try
				{
					z = new Integer(args[2]);
				}
				catch (NumberFormatException e)
				{
					OutputHandler.chatError(sender, Localization.format(Localization.ERROR_NAN, args[2]));
					return;
				}
				EntityPlayerMP player = (EntityPlayerMP) sender;
				PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(player.username);
				playerInfo.back = new WarpPoint(player);
				TeleportCenter.addToTpQue(new WarpPoint(player.dimension, x, y, z, player.rotationPitch, player.rotationYaw), player);
			}
			else if (args.length == 4)
			{

				int x = 0, y = 0, z = 0;
				try
				{
					x = new Integer(args[1]);
				}
				catch (NumberFormatException e)
				{
					OutputHandler.chatError(sender, Localization.format(Localization.ERROR_NAN, args[1]));
					return;
				}
				try
				{
					y = new Integer(args[2]);
				}
				catch (NumberFormatException e)
				{
					OutputHandler.chatError(sender, Localization.format(Localization.ERROR_NAN, args[2]));
					return;
				}
				try
				{
					z = new Integer(args[3]);
				}
				catch (NumberFormatException e)
				{
					OutputHandler.chatError(sender, Localization.format(Localization.ERROR_NAN, args[3]));
					return;
				}
				EntityPlayerMP player = FunctionHelper.getPlayerFromPartialName(args[0]);
				if(PlayerSelector.hasArguments(args[0]))
				{
					player = PlayerSelector.matchOnePlayer(sender, args[0]);
				}
				if (player != null)
				{
					PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(player.username);
					playerInfo.back = new WarpPoint(player);
					TeleportCenter.addToTpQue(new WarpPoint(player.dimension, x, y, z, player.rotationPitch, player.rotationYaw), player);
				}
				else
				{
					OutputHandler.chatError(sender, Localization.format(Localization.ERROR_NOPLAYER, args[0]));
				}
			}
			else
			{
				OutputHandler.chatError(sender, Localization.get(Localization.ERROR_BADSYNTAX));
			}
		}
		else
		{
			OutputHandler.chatError(sender, Localization.get(Localization.ERROR_BADSYNTAX));
		}
	}

	@Override
	public void processCommandConsole(ICommandSender sender, String[] args)
	{
		if (args.length == 2)
		{
			EntityPlayerMP player = FunctionHelper.getPlayerFromPartialName(args[0]);
			if(PlayerSelector.hasArguments(args[0]))
			{
				player = PlayerSelector.matchOnePlayer(sender, args[0]);
			}
			EntityPlayer target = FunctionHelper.getPlayerFromPartialName(args[1]);
			if(PlayerSelector.hasArguments(args[1]))
			{
				target = PlayerSelector.matchOnePlayer(sender, args[1]);
			}
			if (player != null && target != null)
			{
				PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(player.username);
				playerInfo.back = new WarpPoint(player);
				TeleportCenter.addToTpQue(new WarpPoint(target), player);
			}
			else
			{
				if (player == null)
				{
					sender.sendChatToPlayer(Localization.format(Localization.ERROR_NOPLAYER, args[0]));
				}
				if (target == null)
				{
					sender.sendChatToPlayer(Localization.format(Localization.ERROR_NOPLAYER, args[1]));
				}
				return;
			}
		}else if (args.length == 4)
		{

			int x = 0, y = 0, z = 0;
			try
			{
				x = new Integer(args[1]);
			}
			catch (NumberFormatException e)
			{
				sender.sendChatToPlayer(Localization.format(Localization.ERROR_NAN, args[1]));
				return;
			}
			try
			{
				y = new Integer(args[2]);
			}
			catch (NumberFormatException e)
			{
				sender.sendChatToPlayer(Localization.format(Localization.ERROR_NAN, args[2]));
				return;
			}
			try
			{
				z = new Integer(args[3]);
			}
			catch (NumberFormatException e)
			{
				sender.sendChatToPlayer(Localization.format(Localization.ERROR_NAN, args[3]));
				return;
			}
			EntityPlayerMP player = FunctionHelper.getPlayerFromPartialName(args[0]);
			if(PlayerSelector.hasArguments(args[0]))
			{
				player = PlayerSelector.matchOnePlayer(sender, args[0]);
			}
			if (player != null)
			{
				PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(player.username);
				playerInfo.back = new WarpPoint(player);
				TeleportCenter.addToTpQue(new WarpPoint(player.dimension, x, y, z, player.rotationPitch, player.rotationYaw), player);
			}
			else
			{
				sender.sendChatToPlayer(Localization.format(Localization.ERROR_NOPLAYER, args[0]));
			}
		}
		else
		{
			sender.sendChatToPlayer(Localization.get(Localization.ERROR_BADSYNTAX));
		}
	}

	@Override
	public boolean canConsoleUseCommand()
	{
		return true;
	}

	@Override
	public String getCommandPerm()
	{
		return "ForgeEssentials.BasicCommands." + getCommandName();
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args)
	{
		if (args.length == 1 || args.length == 2)
		{
			return getListOfStringsMatchingLastWord(args, FMLCommonHandler.instance().getMinecraftServerInstance().getAllUsernames());
		}
		else
		{
			return null;
		}
	}
}

package com.ForgeEssentials.commands;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;

import com.ForgeEssentials.api.data.DataStorageManager;
import com.ForgeEssentials.api.modules.FEModule;
import com.ForgeEssentials.api.modules.FEModule.Config;
import com.ForgeEssentials.api.modules.FEModule.Init;
import com.ForgeEssentials.api.modules.FEModule.ModuleDir;
import com.ForgeEssentials.api.modules.FEModule.PreInit;
import com.ForgeEssentials.api.modules.FEModule.ServerInit;
import com.ForgeEssentials.api.modules.FEModule.ServerPostInit;
import com.ForgeEssentials.api.modules.FEModule.ServerStop;
import com.ForgeEssentials.api.modules.event.FEModuleInitEvent;
import com.ForgeEssentials.api.modules.event.FEModulePreInitEvent;
import com.ForgeEssentials.api.modules.event.FEModuleServerInitEvent;
import com.ForgeEssentials.api.modules.event.FEModuleServerPostInitEvent;
import com.ForgeEssentials.api.modules.event.FEModuleServerStopEvent;
import com.ForgeEssentials.api.permissions.IPermRegisterEvent;
import com.ForgeEssentials.api.permissions.PermRegister;
import com.ForgeEssentials.api.permissions.RegGroup;
import com.ForgeEssentials.commands.util.CommandRegistrar;
import com.ForgeEssentials.commands.util.ConfigCmd;
import com.ForgeEssentials.commands.util.EventHandler;
import com.ForgeEssentials.commands.util.MobTypeLoader;
import com.ForgeEssentials.commands.util.PlayerTrackerCommands;
import com.ForgeEssentials.commands.util.TickHandlerCommands;
import com.ForgeEssentials.core.ForgeEssentials;
import com.ForgeEssentials.data.DataDriver;
import com.ForgeEssentials.util.DataStorage;
import com.ForgeEssentials.util.OutputHandler;
import com.ForgeEssentials.util.TeleportCenter;
import com.ForgeEssentials.util.Warp;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@FEModule(configClass = ConfigCmd.class, name = "CommandsModule", parentMod = ForgeEssentials.class)
public class ModuleCommands
{
	@Config
	public static ConfigCmd	conf;
	
	@ModuleDir
	public static File cmddir;

	public DataDriver		data;

	@PreInit
	public void preLoad(FEModulePreInitEvent e)
	{
		OutputHandler.info("Commands module is enabled. Loading...");
		MobTypeLoader.preLoad(e);
	}

	@Init
	public void load(FEModuleInitEvent e)
	{
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		MinecraftForge.EVENT_BUS.register(this); // for the permissions.
		GameRegistry.registerPlayerTracker(new PlayerTrackerCommands());
		CommandRegistrar.commandConfigs(conf.config);
	}

	@ServerInit
	public void serverStarting(FEModuleServerInitEvent e)
	{
		DataStorage.load();

		data = DataStorageManager.getReccomendedDriver();
		
		CommandRegistrar.load((FMLServerStartingEvent) e.getFMLEvent());
	}

	@ServerPostInit
	public void serverStarted(FEModuleServerPostInitEvent e)
	{
		loadWarps();
		TickRegistry.registerScheduledTickHandler(new TickHandlerCommands(), Side.SERVER);
		
	}

	@PermRegister(ident = "ModuleBasicCommands")
	public static void registerPermissions(IPermRegisterEvent event)
	{
		event.registerPermissionLevel("ForgeEssentials.BasicCommands._ALL_", RegGroup.MEMBERS);
		
		event.registerPermissionLevel("ForgeEssentials.BasicCommands.compass", RegGroup.MEMBERS);
		event.registerPermissionLevel("ForgeEssentials.BasicCommands.afk", RegGroup.MEMBERS);
		event.registerPermissionLevel("ForgeEssentials.BasicCommands.back", RegGroup.MEMBERS);
		event.registerPermissionLevel("ForgeEssentials.BasicCommands.bed", RegGroup.MEMBERS);
		event.registerPermissionLevel("ForgeEssentials.BasicCommands.colorize", RegGroup.MEMBERS);
		
		event.registerPermissionLevel("ForgeEssentials.BasicCommands.list", RegGroup.GUESTS);
		event.registerPermissionLevel("ForgeEssentials.BasicCommands.rules", RegGroup.GUESTS);
		event.registerPermissionLevel("ForgeEssentials.BasicCommands.motd", RegGroup.GUESTS);
		event.registerPermissionLevel("ForgeEssentials.BasicCommands.tps", RegGroup.GUESTS);
		event.registerPermissionLevel("ForgeEssentials.BasicCommands.modlist", RegGroup.GUESTS);
		event.registerPermissionLevel("ForgeEssentials.BasicCommands.spawn", RegGroup.GUESTS);
	}

	@ServerStop
	public void serverStopping(FEModuleServerStopEvent e)
	{
		saveWarps();
	}

	private void saveWarps()
	{
		for (Warp warp : TeleportCenter.warps.values())
		{
			data.saveObject(warp);
		}
	}

	private void loadWarps()
	{
		Object[] objs = data.loadAllObjects(Warp.class);
		for (Object obj : objs)
		{
			Warp warp = ((Warp) obj);
			TeleportCenter.warps.put(warp.getName(), warp);
		}
	}
}
package net.tkdkid1000.armiworld.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.LegacyWorldData;

import net.tkdkid1000.armiworld.ArmiWorldEconomy;
import net.tkdkid1000.armiworld.utils.Locstring;

public class Lumberjack implements Listener {

	private ArmiWorldEconomy armiworldeconomy;

	public Lumberjack(ArmiWorldEconomy armiworldeconomy) {
		this.armiworldeconomy = armiworldeconomy;
	}

	public void register() {
		armiworldeconomy.getServer().getPluginManager().registerEvents(this, armiworldeconomy);
		build();
	}
	
	public void build() {
		Location loc = Locstring.stringToLoc(ArmiWorldEconomy.getInstance().config.getString("jobs.lumberjack.loc"));
		new BukkitRunnable() {

			@Override
			public void run() {
				new BukkitRunnable() {

					@Override
					public void run() {
						Clipboard clipboard;
						File schem = new File("plugins"+File.separator+"WorldEdit"+File.separator+"schematics"+File.separator+ArmiWorldEconomy.getInstance().config.getString("jobs.lumberjack.schematic")+".schematic");
						ClipboardFormat format = ClipboardFormat.findByFile(schem);
						try {
							ClipboardReader reader = format.getReader(new FileInputStream(schem));
							clipboard = reader.read(LegacyWorldData.getInstance());
							
							EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(FaweAPI.getWorld(loc.getWorld().getName()), -1);
							Operation operation = new ClipboardHolder(clipboard, LegacyWorldData.getInstance())
						            .createPaste(editSession, LegacyWorldData.getInstance())
						            .to(new Vector(loc.getX(), loc.getY(), loc.getZ()))
						            .build();
							try {
								Operations.complete(operation);
							} catch (WorldEditException e) {
								e.printStackTrace();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				}.runTaskAsynchronously(ArmiWorldEconomy.getInstance());
			}
			
		}.runTaskTimer(armiworldeconomy, 120*20, 120*20);
	}
}
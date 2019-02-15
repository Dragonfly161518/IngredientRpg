package me.yuuma.plugin;

import java.util.HashMap;
import java.util.UUID;

import javax.xml.bind.annotation.XmlEnumValue;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.badbones69.blockparticles.api.BlockParticles;
import me.badbones69.blockparticles.api.Particles;

public class IngredientRpg extends JavaPlugin implements Listener, CommandExecutor {

	public static HashMap<Location, String> locBlock = new HashMap<Location, String>();
	public static SettingsManager settings = SettingsManager.getInstance();
	public static PluginManager pl;
	public HashMap<UUID, Long> PlayerCl = new HashMap<UUID, Long>();
	public HashMap<UUID,Integer> TaskId = new HashMap<UUID, Integer>();
	public int COOLDOWN;
	public Location LOCATION;
	public int CHANCE;
	public ItemStack ITEM;
	public int AMOUNT;
	public BlockParticles bp;

	@Override
	public void onEnable() {
		pl = Bukkit.getServer().getPluginManager();
		settings.setup(this);
		getConfig().options().copyDefaults(true);
		saveConfig();
		loadData();
		bp = BlockParticles.getInstance();
		pl.registerEvents(this, this);
		getCommand("Ig").setExecutor(new Command());

	}

	@Override
	public void onDisable() {

	}

	public static void loadData() {
		for (String name : settings.getData().getKeys(false)) {
			String w = settings.getData().getString(name + ".World");
			int X = settings.getData().getInt(name + ".X");
			int Y = settings.getData().getInt(name + ".Y");
			int Z = settings.getData().getInt(name + ".Z");
			World W = Bukkit.getServer().getWorld(w);
			Location l = new Location(W, X, Y, Z);
			locBlock.put(l, name);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(!e.getHand().equals(EquipmentSlot.HAND)) {
			return;
		}
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		if (e.getClickedBlock().getType() == null) {
			return;
		}
		Location l = e.getClickedBlock().getLocation();
		if (!locBlock.containsKey(l)) {
			return;
		}
		Player p = e.getPlayer();
		if (e.getItem() == null) {
			p.sendMessage(ChatColor.RED + "สมุนไพรถูกเก็บผิดวิธี");
			return;
		}
		if (!e.getItem().equals(new ItemStack(Material.SHEARS))) {
			p.sendMessage(ChatColor.RED + "สมุนไพรถูกเก็บผิดวิธี");
			return;
		}
		if(PlayerCl.containsKey(e.getPlayer().getUniqueId())) {
			p.sendMessage(ChatColor.RED + "สามารถเก็บสมุนไพรได้ทีละชนิดเท่านั้น");
			return;
		}
		String root = locBlock.get(l);
		COOLDOWN = settings.getData().getInt(root+".Cooldown");
		AMOUNT = settings.getData().getInt(root + ".Amount");
		CHANCE = settings.getData().getInt(root + ".Chance");
		ITEM = settings.getData().getItemStack(root + ".Item");
		ITEM.setAmount(AMOUNT);
		String w = settings.getData().getString(root + ".World");
		int x = settings.getData().getInt(root + ".X");
		int y = settings.getData().getInt(root + ".Y");
		int z = settings.getData().getInt(root + ".Z");
		World W = Bukkit.getWorld(w);
		LOCATION = new Location(W, x, y, z);
		PlayerCl.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
		bp.setParticle(Particles.DOUBLEWITCH, LOCATION, e.getPlayer().getDisplayName());
		int ID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
	
			public void run() {
				int cool = (int) (PlayerCl.get(p.getUniqueId())/1000 + COOLDOWN - (System.currentTimeMillis()/1000));
				if(cool == 0) {
					Bukkit.getServer().getWorld(LOCATION.getWorld().getName()).dropItemNaturally(p.getLocation(), ITEM);
					PlayerCl.remove(p.getUniqueId());
					Bukkit.getScheduler().cancelTask(TaskId.get(e.getPlayer().getUniqueId()));
					bp.removeParticle(e.getPlayer().getDisplayName());
				}
			}
		}, 20, 20);
		TaskId.put(e.getPlayer().getUniqueId(),ID);
	}
	
	@EventHandler
	public void onPlayerSneaking(PlayerToggleSneakEvent e) {
		if(!PlayerCl.containsKey(e.getPlayer().getUniqueId())) {
			return;
		}
		if(e.isSneaking())  {
			PlayerCl.remove(e.getPlayer().getUniqueId());
			Bukkit.getScheduler().cancelTask(TaskId.get(e.getPlayer().getUniqueId()));
			e.getPlayer().sendMessage(ChatColor.RED + "ยกเลิกการเก็บสมุนไพร");
			bp.removeParticle(e.getPlayer().getDisplayName());
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(!PlayerCl.containsKey(e.getPlayer().getUniqueId())) {
			return;
		}
		int cool = (int) (PlayerCl.get(e.getPlayer().getUniqueId())/1000 + COOLDOWN - (System.currentTimeMillis()/1000));
		e.getPlayer().sendMessage(ChatColor.YELLOW + "ระยะเวลาการเก็บเกี่ยว " + ChatColor.RED + cool + ChatColor.YELLOW +" วิ");
		e.setCancelled(true);
	}
	
}

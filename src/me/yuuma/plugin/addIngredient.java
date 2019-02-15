package me.yuuma.plugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class addIngredient {
	
	public void setUp(Player p,String name) {
		for(String n: IngredientRpg.settings.getData().getKeys(false)) {
			if(n.equalsIgnoreCase("name")) {
				p.sendMessage(ChatColor.RED + "ชื่อนี้ใช้ไปแล้ว!");
				return;
			}
		}
		Block b = p.getTargetBlock(null, 5);
		if(b.isEmpty()) {
			p.sendMessage(ChatColor.RED+ "จุดที่มองไม่ใช่บล็อก!");
			return;
		}
		if(p.getInventory().getItemInMainHand()==null ||p.getInventory().getItemInMainHand().getType().equals(Material.AIR) ) {
			p.sendMessage(ChatColor.RED+ "ไอเทมไม่มีบนมือ");
			return;
		}
		ItemStack i = p.getInventory().getItemInMainHand();
		if(!i.hasItemMeta()) {
			return;
		}
		if(!i.getItemMeta().hasLore()) {
			p.sendMessage(ChatColor.RED +"ไอเทมนี้ไม่มี Lore");
			return;
		}
		
		org.bukkit.Location l = b.getLocation();
		String w = l.getWorld().getName();
		int x = l.getBlockX();
		int y = l.getBlockY();
		int z = l.getBlockZ();
		IngredientRpg.settings.getData().set(name + ".World", w);
		IngredientRpg.settings.getData().set(name + ".X", x);
		IngredientRpg.settings.getData().set(name + ".Y", y);
		IngredientRpg.settings.getData().set(name + ".Z", z);
		IngredientRpg.settings.getData().set(name + ".Item", p.getInventory().getItemInMainHand());
		IngredientRpg.settings.getData().set(name + ".Cooldown", 10);
		IngredientRpg.settings.getData().set(name + ".Chance", 100);
		IngredientRpg.settings.getData().set(name + ".Amount", 1);
		IngredientRpg.settings.saveData();
		IngredientRpg.loadData();
	}
	
	public void removeIngredient(String name) {
		IngredientRpg.settings.getData().set(name, null);
	}
}

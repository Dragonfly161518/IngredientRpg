package me.yuuma.plugin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {
	
	public addIngredient addIng = new addIngredient();
	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String str, String[] args) {
		if(!sender.isOp()) {
			return false;
		}
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("Add")) {
				if(args.length > 1) {
						addIng.setUp((Player)sender,args[1]);
						return true;
					}
				}
			if (args[0].equalsIgnoreCase("reload")) {
				IngredientRpg.settings.reloadConfig();
				IngredientRpg.settings.reloadData();
				return true;
			}
			if (args[0].equalsIgnoreCase("list")) {
				for(String l : IngredientRpg.settings.getData().getKeys(false)) {
					if(l.equals("") || l==null) {
						sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW +"IngredientRPG&7] " + ChatColor.WHITE+ ": "+ ChatColor.RED+"No list of Ingredients");
					}
					sender.sendMessage(ChatColor.GRAY+"- " + ChatColor.YELLOW + l);
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("remove")) {
				if(args.length > 1) {
					for(String l : IngredientRpg.settings.getData().getKeys(false)) {
						if(l.equalsIgnoreCase(args[1])) {
							sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW +"IngredientRPG&7] " + ChatColor.WHITE+ ": "+ ChatColor.RED+"Remove " + args[1]);
							addIng.removeIngredient(args[1]);
							return true;
						}
					}
					sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW +"IngredientRPG&7] " + ChatColor.WHITE+ ": "+ ChatColor.RED+"This name is null");
					return true;
				}
			}
		}
		
		
		if(args.length == 0 || args[0].equalsIgnoreCase("Help")) {
			sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW +"IngredientRPG&7] " + ChatColor.WHITE+ ": "+ ChatColor.RED+" By Porror List of all Command.");
			sender.sendMessage(ChatColor.GRAY+"- " + ChatColor.YELLOW +"/ig help "+ ChatColor.GRAY+ "List of all Command.");
			sender.sendMessage(ChatColor.GRAY+"- " + ChatColor.YELLOW +"/ig list "+ ChatColor.GRAY+ "List of all Ingredients.");
			sender.sendMessage(ChatColor.GRAY+"- " + ChatColor.YELLOW +"/ig remove <name> "+ ChatColor.GRAY+ "Remove Ingredients.");
			sender.sendMessage(ChatColor.GRAY+"- " + ChatColor.YELLOW +"/ig add <name> "+ ChatColor.GRAY+ "Create a new Ingredients");
			sender.sendMessage(ChatColor.GRAY+"- " + ChatColor.YELLOW +"/ig reload "+ ChatColor.GRAY+ "Reload a Config");
			return true;
		}
		sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW +"IngredientRPG&7] " + ChatColor.WHITE+ ": "+ ChatColor.RED+" Command incorrect Please /ig help for Help.");
		return true;
	}
	
}

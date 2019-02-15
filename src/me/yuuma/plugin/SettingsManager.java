package me.yuuma.plugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class SettingsManager {
	
	public static SettingsManager instance = new SettingsManager();
	Plugin p;
	
	FileConfiguration config;
	File cfile;
	FileConfiguration data;
	File dfile;
	
	public static SettingsManager getInstance() {
		return instance;
	}
	
	public void setup(Plugin p) {
		this.p = p;
		this.cfile = new File(p.getDataFolder(), "config.yml");
		this.config = p.getConfig();
		if(!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}
		this.dfile = new File(p.getDataFolder(), "data.yml");
		if(!this.dfile.exists()) {
			try {
				this.dfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.data = YamlConfiguration.loadConfiguration(dfile);
		saveData();
		saveConfig();
	}
	
	public FileConfiguration getData() {
		return this.data;
	}
	
	public void saveData() {
		try {
			this.data.save(dfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reloadData() {
		this.data = YamlConfiguration.loadConfiguration(dfile);
		IngredientRpg.loadData();
	}
	
	public FileConfiguration getConfig() {
		return this.config;
	}
	
	public void saveConfig() {
		try {
			this.config.save(cfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reloadConfig() {
		this.config = YamlConfiguration.loadConfiguration(cfile);
	}
	
}

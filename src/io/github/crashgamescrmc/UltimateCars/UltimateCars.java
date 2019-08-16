package io.github.crashgamescrmc.UltimateCars;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.milkbowl.vault.economy.Economy;

public class UltimateCars extends JavaPlugin {

	public static JSONObject config = new JSONObject();
	public static JSONObject vehicles = new JSONObject();
	public static UltimateCars plugin;
	public static Economy economy;

	public static final String version = "0.0.1";
	public static final long build = 1;

	@Override
	public void onEnable() {
		plugin = this;
		if (!setupEconomey()) {
			getLogger().severe("No economy found!");
			Bukkit.shutdown();
		}
		loadConfigFile();
		loadVehiclesFile();

		getServer().getPluginManager().registerEvents(new CarController(), this);
		
		getCommand("cars").setExecutor(new UltimateCarsCommandExecutor());

		getLogger().info("Enabled UltimateCars!");
	}

	public static void saveConfigFile() {
		try {
			FileWriter fw = new FileWriter("plugins/UltimateCars/config.json");
			fw.write(config.toJSONString());
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void loadConfigFile() {
		File dir = new File("plugins/UltimateCars");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		try {
			FileReader fw = new FileReader("plugins/UltimateCars/config.json");
			JSONParser parser = new JSONParser();
			try {
				config = (JSONObject) parser.parse(fw);
				fw.close();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			resetConfigData();
			plugin.getLogger().info("config.json not found, generating...");
			saveConfigFile();
		}

	}

	public static void saveVehiclesFile() {
		try {
			FileWriter fw = new FileWriter("plugins/UltimateCars/vehicles.json");
			fw.write(vehicles.toJSONString());
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadVehiclesFile() {
		File dir = new File("plugins/UltimateCars");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		try {
			FileReader fw = new FileReader("plugins/UltimateCars/vehicles.json");
			JSONParser parser = new JSONParser();
			try {
				vehicles = (JSONObject) parser.parse(fw);
				fw.close();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			resetVehiclesData();
			plugin.getLogger().info("vehicles.json not found, generating...");
			saveVehiclesFile();
		}
	}

	@SuppressWarnings("unchecked")
	public static void resetVehiclesData() {
		vehicles.clear();

		JSONObject cars = new JSONObject();

		vehicles.put("cars", cars);
	}

	@SuppressWarnings("unchecked")
	public static void resetConfigData() {
		config.clear();

		config.put("version", version);
		config.put("build", build);
	}

	private boolean setupEconomey() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return economy != null;
	}

}

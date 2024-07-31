package com.brodi.rtpqueue1;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Plugin started successfully");
        getCommand("rtpqueue").setExecutor(new RTPQueueCommand(this));
        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

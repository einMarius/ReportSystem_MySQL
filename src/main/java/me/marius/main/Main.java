//This file was created in 2021

package me.marius.main;

import me.marius.commands.ReportCommand;
import me.marius.config.ConfigManager;
import me.marius.listener.DisconnectListener;
import me.marius.mysql.MySQL;
import me.marius.utils.Utils;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {

    private Main plugin;
    private ConfigManager configManager;
    private MySQL mySQL;
    private Utils utils;
    private ReportCommand reportCommand;
    private DisconnectListener disconnectListener;

    private boolean isrunning;

    public void onEnable() {

        plugin = this;
        configManager = new ConfigManager(this);
        mySQL = new MySQL(this);
        utils = new Utils(this);
        reportCommand = new ReportCommand(this);
        disconnectListener = new DisconnectListener(this);

        isrunning = !isrunning;
            new Thread(() -> {
                while (isrunning){
                    try {
                        Thread.sleep(100);
                    }catch (InterruptedException ex){
                        Thread.currentThread().interrupt();
                            System.out.println("[ReportSystem] Thread was interrupted, Failed to complete operation");
                    }
                    configManager.register();
                    mySQL.connect();
                    mySQL.createTable();

                    isrunning = false;
                }

            }).start();


        getProxy().getPluginManager().registerCommand(this, new ReportCommand(this));
        getProxy().getPluginManager().registerListener(this, new DisconnectListener(this));

        //-------------------------------
        System.out.println("----------[ReportSystem_MySQL]----------");
        System.out.println("Plugin aktiviert");
        System.out.println("Version: 1.0");
        System.out.println("Author: einMarius");
        System.out.println("----------[ReportSystem_MySQL]----------");
        // -------------------------------
    }

    public void onDisable() {

        mySQL.disconnect();

        // -------------------------------
        System.out.println("----------[ReportSystem_MySQL]----------");
        System.out.println("Plugin deaktiviert");
        System.out.println("Version: 1.0");
        System.out.println("Author: einMarius");
        System.out.println("----------[ReportSystem_MySQL]----------");
        // -------------------------------

    }

    public Main getPlugin() { return plugin; }
    public ConfigManager getConfigManager() { return configManager; }
    public MySQL getMySQL(){ return mySQL; }
    public Utils getUtils() { return utils; }
    public ReportCommand getReportCommand() { return reportCommand; }
    public DisconnectListener getDisconnectListener() { return disconnectListener; }

}
package me.marius.utils;

import me.marius.main.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Utils {

    private Main plugin;
    public Utils(Main plugin){ this.plugin = plugin; }
    public void sendTeamHelpMessage(ProxiedPlayer p){
        p.sendMessage(plugin.getConfigManager().prefix + "§7----------= §cHelp §7=----------");
        p.sendMessage(plugin.getConfigManager().prefix + "Benutze /§creport login");
        p.sendMessage(plugin.getConfigManager().prefix + "Benutze /§creport logout");
        p.sendMessage(plugin.getConfigManager().prefix + "Benutze /§creport list");
        p.sendMessage(plugin.getConfigManager().prefix + "Benutze /§creport next");
        p.sendMessage(plugin.getConfigManager().prefix + "Benutze /§creport finish");
        p.sendMessage(plugin.getConfigManager().prefix + "Benutze /§creport accept §7<§cSpieler§7>");
        p.sendMessage(plugin.getConfigManager().prefix + "§7----------= §cHelp §7=----------");
    }

}

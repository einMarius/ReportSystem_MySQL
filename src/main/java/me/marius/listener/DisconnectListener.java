package me.marius.listener;

import me.marius.commands.ReportCommand;
import me.marius.main.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class DisconnectListener implements Listener {

    private Main plugin;
    public DisconnectListener(Main plugin){ this.plugin = plugin; }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent e){
        ProxiedPlayer p = e.getPlayer();

        if(ReportCommand.loggin.contains(p)){
            ReportCommand.loggin.remove(p);
            for(ProxiedPlayer team : ReportCommand.loggin){
                String otherplayerloggout = plugin.getConfigManager().otheruserlogout.replace("%PLAYER%", p.getName());
                team.sendMessage(plugin.getConfigManager().prefix + otherplayerloggout);
            }
        }
        if(ReportCommand.reportmodus.contains(p)){
            ReportCommand.reportmodus.remove(p);
            for(ProxiedPlayer team : ReportCommand.loggin){
                if(!ReportCommand.reportmodus.contains(team)){
                    String reportclose = plugin.getConfigManager().reportclose.replace("%PLAYER%", p.getName());
                    team.sendMessage(plugin.getConfigManager().prefix + reportclose);
                }
            }
        }

        if(plugin.getMySQL().isReportetExisting(p.getUniqueId())){
            plugin.getMySQL().deleteReport(p.getUniqueId().toString());
        }
    }

}

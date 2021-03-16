//This file was created in 2021

package me.marius.commands;

import me.marius.main.Main;
import me.marius.report.ReportCause;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

public class ReportCommand extends Command {

    private Main plugin;
    public ReportCommand(Main plugin) {
        super("report");
        this.plugin = plugin;
    }

    public static ArrayList<ProxiedPlayer> loggin = new ArrayList<>();
    public static ArrayList<ProxiedPlayer> reportmodus = new ArrayList<>();

    private boolean isrunningReportnext;
    private boolean isrunningReportlist;
    private boolean isrunningReportaccept;
    private String cause;

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;

            //Team Report
            if(p.hasPermission("report.team")){
                if(args.length == 1){
                    //REPORT LOGIN
                    if(args[0].equalsIgnoreCase("login")){
                        if(!loggin.contains(p)){
                            for(ProxiedPlayer team : loggin){
                                String otheruserlogin = plugin.getConfigManager().otheruserloggin.replace("%PLAYER%", p.getName());
                                team.sendMessage(plugin.getConfigManager().prefix + otheruserlogin);
                            }
                            loggin.add(p);
                            p.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().loggin);

                        }else
                            p.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().alreadyloggedin);

                    //REPORT LOGOUT
                    } else if(args[0].equalsIgnoreCase("logout")){
                        if(loggin.contains(p)){
                            if(!reportmodus.contains(p)){
                                loggin.remove(p);
                                p.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().logout);
                                for(ProxiedPlayer team : loggin){
                                    String otheruserloggout = plugin.getConfigManager().otheruserlogout.replace("%PLAYER%", p.getName());
                                    team.sendMessage(plugin.getConfigManager().prefix + otheruserloggout);
                                }

                            } else
                                p.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().mustclosereport);
                        }else
                            p.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().alreadyloggedout);
                    //REPORT LIST
                    } else if(args[0].equalsIgnoreCase("list")){
                        if(loggin.contains(p)){
                            if(plugin.getMySQL().getReports().size() > 0){
                                isrunningReportlist = !isrunningReportlist;
                                new Thread(() -> {
                                    while (isrunningReportlist) {
                                        try {
                                            Thread.sleep(250);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }

                                        p.sendMessage("");
                                        p.sendMessage(plugin.getConfigManager().prefix + "----------= §c§lReports §7=----------");
                                        for (String reportedplayer : plugin.getMySQL().getReports()) {
                                            p.sendMessage("§7Spieler: §c" + plugin.getMySQL().getReportedPlayer(reportedplayer) + " §8┃ §7wegen: §c" + plugin.getMySQL().getCause(reportedplayer) +
                                                    " §8┃ §7von: §c" + plugin.getMySQL().getReporter(reportedplayer) + " §8┃ §7ursprünglich auf: §c" +
                                                    plugin.getMySQL().getServer(reportedplayer));
                                        }
                                        p.sendMessage(plugin.getConfigManager().prefix + "----------= §c§lReports §7=----------");

                                        isrunningReportlist = false;
                                    }
                                }).start();
                            }else
                                p.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().noopenreports);
                        }else
                            p.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().mustebeloggedin);

                    //REPORT NEXT
                    } else if(args[0].equalsIgnoreCase("next")){
                        if(loggin.contains(p)){
                            if(!reportmodus.contains(p)){
                                if(plugin.getMySQL().getReports().size() > 0) {
                                    isrunningReportnext = !isrunningReportnext;
                                    new Thread(() -> {
                                        while (isrunningReportnext) {
                                            try {
                                                Thread.sleep(250);
                                            } catch (InterruptedException ex) {
                                                ex.printStackTrace();
                                            }
                                            plugin.getMySQL().getNextReport(p);
                                            reportmodus.add(p);
                                            for(ProxiedPlayer team : loggin){
                                                if(!reportmodus.contains(team)){
                                                    String reportaccept = plugin.getConfigManager().reportbearbeiten.replace("%PLAYER%", p.getName());
                                                    team.sendMessage(plugin.getConfigManager().prefix + reportaccept);
                                                }
                                            }

                                            isrunningReportnext = false;
                                        }
                                    }).start();
                                }else
                                    p.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().noopenreports);
                            }else
                                p.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().mustclosereport);
                        }else
                            p.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().mustebeloggedin);

                    //REPORT FINISH
                    } else if(args[0].equalsIgnoreCase("finish")){
                        if(loggin.contains(p)){
                            if(reportmodus.contains(p)){
                                for(ProxiedPlayer team : loggin){
                                    if (!reportmodus.contains(team)){
                                        String reportclose = plugin.getConfigManager().reportclose.replace("%PLAYER%", p.getName());
                                        team.sendMessage(plugin.getConfigManager().prefix + reportclose);
                                    }
                                }
                                reportmodus.remove(p);
                                p.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().reportclose2);
                            }else
                                p.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().noreport);
                        }else
                            p.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().mustebeloggedin);

                    //TEAM HELPMESSAGE
                    } else
                        plugin.getUtils().sendTeamHelpMessage(p);

                //REPORT ACCEPT <NAME>
                } else if(args.length == 2){
                    if(args[0].equalsIgnoreCase("accept")) {
                        ProxiedPlayer reportet = ProxyServer.getInstance().getPlayer(args[1]);
                        if(loggin.contains(p)) {
                            if(!reportmodus.contains(p)) {
                                isrunningReportaccept = !isrunningReportaccept;
                                new Thread(() -> {
                                    while (isrunningReportaccept){
                                        try {
                                            Thread.sleep(250);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        plugin.getMySQL().acceptReport(reportet, p);
                                        reportmodus.add(p);
                                        for(ProxiedPlayer team : loggin){
                                            if(!reportmodus.contains(team)){
                                                String reportaccept = plugin.getConfigManager().reportbearbeiten.replace("%PLAYER%", p.getName());
                                                team.sendMessage(plugin.getConfigManager().prefix + reportaccept);
                                            }
                                        }

                                        isrunningReportaccept = false;
                                    }
                                }).start();
                            } else
                                p.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().mustclosereport);
                        }else
                            p.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().mustebeloggedin);

                    //TEAM HELPMESSAGE
                    }else
                        plugin.getUtils().sendTeamHelpMessage(p);

                //TEAM HELPMESSAGE
                } else
                    plugin.getUtils().sendTeamHelpMessage(p);

            //SPIELER REPORT
            } else {
                ProxiedPlayer reporter = (ProxiedPlayer) sender;
                if(args.length == 2){
                    ProxiedPlayer reportedplayer = ProxyServer.getInstance().getPlayer(args[0]);
                    cause = args[1];
                    cause = ReportCause.getReportCause(cause);
                    if(!(reportedplayer == null)){
                        //if(!(reportedplayer == reporter)) {
                        if (cause == null) {
                            reporter.sendMessage(plugin.getConfigManager().prefix + "Gültige Reportgründe: " + ReportCause.stream());
                            return;
                        }
                        if(!plugin.getMySQL().isReportetExisting(reportedplayer.getUniqueId())) {

                            plugin.getMySQL().createNewReport(reportedplayer, cause, reporter, reportedplayer.getServer().getInfo().getName());

                            String reportsuccess = plugin.getConfigManager().reportsuccess.replace("%PLAYER%", reportedplayer.getName());
                            p.sendMessage(plugin.getConfigManager().prefix + reportsuccess);
                            for(ProxiedPlayer team : loggin){
                                if(!reportmodus.contains(team)){
                                    String userreport = plugin.getConfigManager().newreport.replace("%PLAYER%", reportedplayer.getName())
                                            .replace("%CAUSE%", cause);
                                    team.sendMessage(plugin.getConfigManager().prefix + userreport);
                                }
                            }
                        } else {
                            String reportsuccess = plugin.getConfigManager().reportsuccess.replace("%PLAYER%", reportedplayer.getName());
                            p.sendMessage(plugin.getConfigManager().prefix + reportsuccess);
                            for(ProxiedPlayer team : loggin){
                                String userreport = plugin.getConfigManager().newreport.replace("%PLAYER%", reportedplayer.getName())
                                        .replace("%CAUSE%", cause);
                                team.sendMessage(plugin.getConfigManager().prefix + userreport);

                            }
                        }
                        //} else
                    }else
                        reporter.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().playernotonline);
                }else
                    reporter.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().wrongargue);
            }
        } else
            sender.sendMessage("§4Must be a player!");

    }
}
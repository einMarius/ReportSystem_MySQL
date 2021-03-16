//This file was created in 2021

package me.marius.config;

import java.io.File;
import java.io.IOException;

import me.marius.main.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ConfigManager {

    private Main plugin;
    public ConfigManager(Main plugin) {
        this.plugin = plugin;
    }

    public String prefix,
                  wrongargue;

    public String host;
    public int port;
    public String database,
                  username,
                  password;

    public String playernotonline,
                  alreadyloggedin,
                  loggin,
                  otheruserloggin,
                  alreadyloggedout,
                  logout,
                  otheruserlogout,
                  mustclosereport,
                  newreport,
                  reportsuccess,
                  mustebeloggedin,
                  isnotreported,
                  noopenreports,
                  noreport,
                  reportclose,
                  reportclose2,
                  reportbearbeiten;

    public String reportacceptname,
                reportacceptgrund,
                reportacceptreporter,
                reportacceptserver;

    public static File file;
    public Configuration config;

    public void register() {

        try{
            if(!plugin.getDataFolder().exists())
                plugin.getDataFolder().mkdir();

            file = new File(plugin.getDataFolder().getParent(), "ReportSystem/config.yml");

            if(!file.exists()){
                file.createNewFile();

                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

                config.set("Einstellungen.Prefix", "&c&lReport &8&l✕ &7");
                config.set("Einstellungen.Falsche Argumentenkette", "Benutze /report <Spieler> <Grund>");

                config.set("MySQL.Host", "localhost");
                config.set("MySQL.Port", 3306);
                config.set("MySQL.Database", "reportsystem");
                config.set("MySQL.Username", "root");
                config.set("MySQL.Password", "pP8nt2J9t5xpdUiN");

                config.set("Report.Spieler nicht online", "Der Spieler ist &cnicht &7online!");
                config.set("Report.Bereits eingeloggt", "Du bist bereits &aeingeloggt&7!");
                config.set("Report.Login", "Du hast dich &aeingeloggt&7.");
                config.set("Report.Login anderes Teammitglied", "%PLAYER% hat sich &aeingeloggt&7.");
                config.set("Report.Bereits ausgeloggt", "Du bist bereits &causgeloggt&7!");
                config.set("Report.Logout", "Du hast dich &causgeloggt&7.");
                config.set("Report.Logout anderes Teammiglied", "%PLAYER% &7hat sich &causgeloggt&7.");
                config.set("Report.Muss Report schliessen", "Du musst zuerst den &cReport &7schließen! ➡ /&creport finish");
                config.set("Report.Neuer Report", "&c%PLAYER% &7wurde für &c%CAUSE% &7gemeldet.");
                config.set("Report.Report erfolgreich", "Du hast &c%PLAYER% &7erfolgreich reportet!");
                config.set("Report.Musst eingeloggt sein", "Dazu musst du dich zuerst &aeinloggen&7!");
                config.set("Report.Spieler wurde nicht reportet", "Der angegebene &cSpieler &7wurde nicht reportet!");
                config.set("Report.Keine Reports offen", "Es gibt keine offenen Reports!");
                config.set("Report.Bearbeitet keinen Report", "Du bearbeitest derzeit keinen Report!");
                config.set("Report.Report geschlossen", "%PLAYER% hat einen Report geschlossen.");
                config.set("Report.Report geschlossen2", "Du hast den Report geschlossen.");
                config.set("Report.Report bearbeiten", "%PLAYER% bearbeitet jetzt einen Report.");

                config.set("Report.Akzeptiert.Name", "Du hast den Report von &c%PLAYER% &7angenommen!");
                config.set("Report.Akzeptiert.Grund", "&7Grund: &c%CAUSE%");
                config.set("Report.Akzeptiert.Reporter", "&7Reporter: &c%PLAYER%");
                config.set("Report.Akzeptiert.Ursprünglicher Server", "&7Ursprünglicher Server: &c%SERVER%");
                saveCfg();
            }
        }catch (IOException ex){
            System.out.println("[ReportSyste] Es gab einen Fehler beim erstellen der Config.yml");
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            //REGISTER
            //Einstellungen
            prefix = config.getString("Einstellungen.Prefix");
            prefix = ChatColor.translateAlternateColorCodes('&', prefix);

            wrongargue = config.getString("Einstellungen.Falsche Argumentenkette");
            wrongargue = ChatColor.translateAlternateColorCodes('&', wrongargue);
            //Ende Einstellunge
            //MySQL
            host = config.getString("MySQL.Host");
            port = config.getInt("MySQL.Port");
            database = config.getString("MySQL.Database");
            username = config.getString("MySQL.Username");
            password = config.getString("MySQL.Password");
            //Ende MySQL
            //Report
            playernotonline = config.getString("Report.Spieler nicht online");
            playernotonline = ChatColor.translateAlternateColorCodes('&', playernotonline);

            alreadyloggedin = config.getString("Report.Bereits eingeloggt");
            alreadyloggedin = ChatColor.translateAlternateColorCodes('&', alreadyloggedin);

            loggin = config.getString("Report.Login");
            loggin = ChatColor.translateAlternateColorCodes('&', loggin);

            otheruserloggin = config.getString("Report.Login anderes Teammitglied");
            otheruserloggin = ChatColor.translateAlternateColorCodes('&', otheruserloggin);

            alreadyloggedout = config.getString("Report.Bereits ausgeloggt");
            alreadyloggedout = ChatColor.translateAlternateColorCodes('&', alreadyloggedout);

            logout = config.getString("Report.Logout");
            logout = ChatColor.translateAlternateColorCodes('&', logout);

            otheruserlogout = config.getString("Report.Logout anderes Teammiglied");
            otheruserlogout = ChatColor.translateAlternateColorCodes('&', otheruserlogout);

            mustclosereport = config.getString("Report.Muss Report schliessen");
            mustclosereport = ChatColor.translateAlternateColorCodes('&', mustclosereport);

            newreport = config.getString("Report.Neuer Report");
            newreport = ChatColor.translateAlternateColorCodes('&', newreport);

            reportsuccess = config.getString("Report.Report erfolgreich");
            reportsuccess = ChatColor.translateAlternateColorCodes('&', reportsuccess);

            mustebeloggedin = config.getString("Report.Musst eingeloggt sein");
            mustebeloggedin = ChatColor.translateAlternateColorCodes('&', mustebeloggedin);

            isnotreported = config.getString("Report.Spieler wurde nicht reportet");
            isnotreported = ChatColor.translateAlternateColorCodes('&', isnotreported);

            noopenreports = config.getString("Report.Keine Reports offen");
            noopenreports = ChatColor.translateAlternateColorCodes('&', noopenreports);

            noreport = config.getString("Report.Bearbeitet keinen Report");
            noreport = ChatColor.translateAlternateColorCodes('&', noreport);

            reportclose = config.getString("Report.Report geschlossen");
            reportclose = ChatColor.translateAlternateColorCodes('&', reportclose);

            reportclose2 = config.getString("Report.Report geschlossen2");
            reportclose2 = ChatColor.translateAlternateColorCodes('&', reportclose2);

            reportbearbeiten = config.getString("Report.Report bearbeiten");
            reportbearbeiten = ChatColor.translateAlternateColorCodes('&', reportbearbeiten);

            //REPORT AKZEPTIERT
            reportacceptname = config.getString("Report.Akzeptiert.Name");
            reportacceptname = ChatColor.translateAlternateColorCodes('&', reportacceptname);

            reportacceptgrund = config.getString("Report.Akzeptiert.Grund");
            reportacceptgrund = ChatColor.translateAlternateColorCodes('&', reportacceptgrund);

            reportacceptreporter = config.getString("Report.Akzeptiert.Reporter");
            reportacceptreporter = ChatColor.translateAlternateColorCodes('&', reportacceptreporter);

            reportacceptserver = config.getString("Report.Akzeptiert.Ursprünglicher Server");
            reportacceptserver = ChatColor.translateAlternateColorCodes('&', reportacceptserver);
            //Ende Report
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCfg() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            System.out.println("Es gab einen Fehler beim Speichern der config.yml!");
        }
    }
}

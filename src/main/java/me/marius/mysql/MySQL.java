package me.marius.mysql;

import me.marius.fetcher.UUIDFetcher;
import me.marius.main.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MySQL {

    private Main plugin;

    public MySQL(Main plugin) {
        this.plugin = plugin;
    }

    private static Connection con;

    private boolean isrunningcreateReport;
    private boolean isrunningdeleteReport;

    public boolean connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + plugin.getConfigManager().host + ":" + plugin.getConfigManager().port + "/" + plugin.getConfigManager().database,
                    plugin.getConfigManager().username, plugin.getConfigManager().password);
            System.out.println("[ReportSystem] Die Verbindung zur MySQL-Datenbank wurde hergestellt!");
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("[ReportSystem] Es gab einen Fehler beim Herstellender Verbindung zur MySQL-Datenbank");
        }
        return false;
    }

    public boolean isConnected() {
        return (con == null ? false : true);
    }

    public void disconnect() {
        try {
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean isReportetExisting(UUID uuid) {
        if(!isConnected())
            if(!connect())
                return false;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Reports WHERE Reportet = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                if(rs.getString("Reportet").equals(uuid.toString()))
                    return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void createTable() {
        if (!isConnected())
            return;

        try {
            PreparedStatement ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS Reports (Zeit DATETIME,Reportet VARCHAR(40),ReportetName VARCHAR(16),Grund VARCHAR(15),Von VARCHAR(40),ReporterName VARCHAR(16),Auf VARCHAR(30))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createNewReport(ProxiedPlayer reportet, String cause, ProxiedPlayer reporter, String reportetserver) {

        isrunningcreateReport = !isrunningcreateReport;

        new Thread(() -> {
            while (isrunningcreateReport) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                if (!isConnected())
                    if (!isReportetExisting(reportet.getUniqueId()))
                        return;
                try {
                    PreparedStatement ps = con.prepareStatement("INSERT INTO reports (Zeit,Reportet,ReportetName,Grund,Von,ReporterName,Auf) VALUES (NOW(),?,?,?,?,?,?)");
                    ps.setString(1, reportet.getUniqueId().toString());
                    ps.setString(2, reportet.getName());
                    ps.setString(3, cause);
                    ps.setString(4, reporter.getUniqueId().toString());
                    ps.setString(5, reporter.getName());
                    ps.setString(6, reportetserver);
                    ps.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                isrunningcreateReport = false;
            }
        }).start();
    }

    public List<String> getReports() {
        if (!isConnected())
                return null;

        List<String> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Reports");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("Reportet"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public String getReportedPlayer(String reportetuuid) {
        if (!isConnected())
            return null;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Reports WHERE Reportet = '" + reportetuuid + "'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("ReportetName");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public String getCause(String reportetuuid) {
        if (!isConnected())
            return null;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Reports WHERE Reportet = ?");
            ps.setString(1, reportetuuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                return rs.getString("Grund");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public String getReporter(String reportetuuid) {
        if (!isConnected())
            return null;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Reports WHERE Reportet = '" + reportetuuid + "'");
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                return rs.getString("ReporterName");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public String getServer(String reportetuuid) {
        if (!isConnected())
            return null;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Reports WHERE Reportet = ?");
            ps.setString(1, reportetuuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                return rs.getString("Auf");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void getNextReport(ProxiedPlayer team){
        if(!isConnected())
            return;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Reports ORDER BY Zeit LIMIT 1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String reportacceptname = plugin.getConfigManager().reportacceptname.replace("%PLAYER%", rs.getString("ReportetName"));
                String reportacceptgrund = plugin.getConfigManager().reportacceptgrund.replace("%CAUSE%", rs.getString("Grund"));
                String reportacceptreporter = plugin.getConfigManager().reportacceptreporter.replace("%PLAYER%", rs.getString("ReporterName"));
                String reportacceptserver = plugin.getConfigManager().reportacceptserver.replace("%SERVER%", rs.getString("Auf"));
                team.sendMessage("");
                team.sendMessage(plugin.getConfigManager().prefix + reportacceptname);
                team.sendMessage(reportacceptgrund);
                team.sendMessage(reportacceptreporter);
                team.sendMessage(reportacceptserver);
                team.sendMessage("");
                deleteReport(rs.getString("Reportet"));
                team.connect(ProxyServer.getInstance().getPlayer(rs.getString("ReportetName")).getServer().getInfo());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return;
    }

    public void acceptReport(ProxiedPlayer reportet, ProxiedPlayer team){
        if(!isConnected())
            if(!isReportetExisting(reportet.getUniqueId())) {
                return;
            }
        try {
            try {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Reports WHERE Reportet = ?");
                ps.setString(1, reportet.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String reportacceptname = plugin.getConfigManager().reportacceptname.replace("%PLAYER%", rs.getString("ReportetName"));
                    String reportacceptgrund = plugin.getConfigManager().reportacceptgrund.replace("%CAUSE%", rs.getString("Grund"));
                    String reportacceptreporter = plugin.getConfigManager().reportacceptreporter.replace("%PLAYER%", rs.getString("ReporterName"));
                    String reportacceptserver = plugin.getConfigManager().reportacceptserver.replace("%SERVER%", rs.getString("Auf"));
                    team.sendMessage("");
                    team.sendMessage(plugin.getConfigManager().prefix + reportacceptname);
                    team.sendMessage(reportacceptgrund);
                    team.sendMessage(reportacceptreporter);
                    team.sendMessage(reportacceptserver);
                    team.sendMessage("");
                    deleteReport(reportet.getUniqueId().toString());
                    team.connect(ProxyServer.getInstance().getPlayer(reportet.getUniqueId()).getServer().getInfo());
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch(NullPointerException ex){
            team.sendMessage(plugin.getConfigManager().prefix + plugin.getConfigManager().isnotreported);
        }
    }

    public void deleteReport(String uuid){
        if(!isConnected())
            return;

        isrunningdeleteReport = !isrunningdeleteReport;
        new Thread(() -> {
            while (isrunningdeleteReport){
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                try {
                    PreparedStatement ps = con.prepareStatement("DELETE FROM Reports WHERE Reportet = ?");
                    ps.setString(1, uuid);
                    ps.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                isrunningdeleteReport = false;
            }
        }).start();

    }
}

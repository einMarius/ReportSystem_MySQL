package me.marius.fetcher;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

public class UUIDFetcher {

    public static String getNameFromUUID(String uuid) {
        String name = null;
        try {
            //hinter uuid .replaceAll("-", "")
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            Scanner jsonScanner = new Scanner(url.openConnection().getInputStream(), "UTF-8");
            String json = jsonScanner.next();
            name = ((JsonObject)(new JsonParser()).parse(json)).get("name").toString();
            jsonScanner.close();
            return name;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public static String getUUID(String name) {
        return getUUIDFromName(name, true, true);
    }

    private static String getUUIDFromName(String name, boolean onlinemode, boolean withSeperators) {
        String uuid = null;
        if (onlinemode) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader((new URL("https://api.mojang.com/users/profiles/minecraft/" + name)).openStream()));
                uuid = ((JsonObject)(new JsonParser()).parse(in)).get("id").toString().replaceAll("\"", "");
                in.close();
            } catch (Exception e) {
                uuid = null;
            }
        } else {
            uuid = null;
        }
        if (uuid != null)
            if (withSeperators) {
                if (!uuid.contains("-"))
                    return uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
            } else {
                uuid = uuid.replaceAll("-", "");
            }
        return uuid;
    }

}

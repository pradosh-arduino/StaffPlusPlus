package net.shortninja.staffplus.core.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.staffplusplus.craftbukkit.api.JsonSenderFactory;
import be.garagepoort.staffplusplus.craftbukkit.common.json.rayzr.JSONMessage;
import be.garagepoort.staffplusplus.craftbukkit.common.json.rayzr.JsonSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

@IocBean
public class JsonSenderService {

    private final JsonSender jsonSender;

    public JsonSenderService() {
        JsonSender resolvedJsonSender = null;
        try {
            resolvedJsonSender = JsonSenderFactory.getSender();
        } catch (Throwable throwable) {
            Bukkit.getLogger().warning("JSON chat messages are not supported by the bundled CraftBukkit adapter for this server version. Falling back to plain chat messages.");
        }
        jsonSender = resolvedJsonSender;
    }

    public void send(JSONMessage jsonMessage, Player... players) {
        if (jsonSender == null) {
            sendPlainMessage(jsonMessage, players);
            return;
        }
        jsonSender.send(jsonMessage, players);
    }

    public void send(JSONMessage jsonMessage, String permission, Player... players) {
        Player[] collect = Arrays.stream(players).filter(p -> p.hasPermission(permission)).toArray(Player[]::new);
        send(jsonMessage, collect);
    }

    private void sendPlainMessage(JSONMessage jsonMessage, Player... players) {
        String plainMessage = jsonMessage.toString();
        for (Player player : players) {
            player.sendMessage(plainMessage);
        }
    }
}

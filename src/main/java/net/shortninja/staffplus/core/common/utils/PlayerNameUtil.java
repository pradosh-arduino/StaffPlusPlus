package net.shortninja.staffplus.core.common.utils;

import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class PlayerNameUtil {

    private PlayerNameUtil() {
    }

    public static Optional<Player> resolveOnlinePlayer(String input) {
        if (input == null) {
            return Optional.empty();
        }

        Player target = Bukkit.getPlayerExact(input);
        if (target != null) {
            return Optional.of(target);
        }

        return Bukkit.getOnlinePlayers().stream()
            .filter(player -> getNickname(player)
                .map(nickname -> nickname.equalsIgnoreCase(input))
                .orElse(false))
            .findFirst();
    }

    public static String getDisplayName(Player player) {
        if (player == null) {
            return null;
        }
        return getNickname(player).orElse(player.getName());
    }

    public static String getDisplayName(SppPlayer player) {
        if (player == null) {
            return null;
        }
        if (player.isOnline()) {
            return getDisplayName(player.getPlayer());
        }
        return player.getUsername();
    }

    public static List<String> getOnlinePlayerDisplayNames() {
        return Bukkit.getOnlinePlayers().stream()
            .map(PlayerNameUtil::getDisplayName)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public static Optional<String> getNickname(Player player) {
        Plugin essentials = Bukkit.getPluginManager().getPlugin("Essentials");
        if (essentials == null || player == null) {
            return Optional.empty();
        }

        try {
            Method getUser = essentials.getClass().getMethod("getUser", Player.class);
            Object user = getUser.invoke(essentials, player);
            if (user == null) {
                return Optional.empty();
            }

            Method getNickname = user.getClass().getMethod("getNickname");
            Object nickname = getNickname.invoke(user);
            if (!(nickname instanceof String) || ((String) nickname).isBlank()) {
                return Optional.empty();
            }

            String strippedNickname = ChatColor.stripColor((String) nickname);
            if (strippedNickname == null || strippedNickname.isBlank()) {
                return Optional.empty();
            }

            return Optional.of(strippedNickname);
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            return Optional.empty();
        }
    }
}

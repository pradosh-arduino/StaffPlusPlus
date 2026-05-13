package net.shortninja.staffplus.core.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.staffplusplus.craftbukkit.api.ProtocolFactory;
import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;
import net.shortninja.staffplus.core.StaffPlusPlus;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Proxy;

@IocBean
public class IProtocolService {

    private final NamespacedKey staffplusplusNbtKey;
    private final IProtocol versionProtocol;

    public IProtocolService() {
        staffplusplusNbtKey = new NamespacedKey(StaffPlusPlus.get(), "protocol_data");
        IProtocol resolvedProtocol;
        try {
            resolvedProtocol = ProtocolFactory.getProtocol();
        } catch (Throwable throwable) {
            Bukkit.getLogger().warning("Protocol adapter is not supported for this server version. Falling back to Bukkit API protocol features.");
            resolvedProtocol = createFallbackProtocol();
        }
        versionProtocol = resolvedProtocol;
    }

    public IProtocol getVersionProtocol() {
        return versionProtocol;
    }

    private IProtocol createFallbackProtocol() {
        return (IProtocol) Proxy.newProxyInstance(
            IProtocol.class.getClassLoader(),
            new Class[]{IProtocol.class},
            (proxy, method, args) -> {
                switch (method.getName()) {
                    case "addNbtString":
                        return addPersistentDataString((ItemStack) args[0], (String) args[1]);
                    case "getNbtString":
                        return getPersistentDataString((ItemStack) args[0]);
                    case "registerCommand":
                        registerCommand((String) args[0], (Command) args[1]);
                        return null;
                    case "unregisterCommand":
                        unregisterCommand((Command) args[1]);
                        return null;
                    default:
                        return getDefaultValue(method.getReturnType());
                }
            });
    }

    private ItemStack addPersistentDataString(ItemStack itemStack, String value) {
        if (itemStack == null) {
            return null;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }

        itemMeta.getPersistentDataContainer().set(staffplusplusNbtKey, PersistentDataType.STRING, value);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private String getPersistentDataString(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return null;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return null;
        }

        return itemMeta.getPersistentDataContainer().get(staffplusplusNbtKey, PersistentDataType.STRING);
    }

    private void registerCommand(String fallbackPrefix, Command command) {
        if (command == null) {
            return;
        }

        CommandMap commandMap = getCommandMap();
        if (commandMap != null) {
            commandMap.register(fallbackPrefix, command);
        }
    }

    private void unregisterCommand(Command command) {
        if (command == null) {
            return;
        }

        CommandMap commandMap = getCommandMap();
        if (commandMap != null) {
            command.unregister(commandMap);
        }
    }

    private CommandMap getCommandMap() {
        try {
            return (CommandMap) Bukkit.getServer().getClass().getMethod("getCommandMap").invoke(Bukkit.getServer());
        } catch (ReflectiveOperationException e) {
            Bukkit.getLogger().warning("Cannot access Bukkit command map while using fallback protocol features.");
            return null;
        }
    }

    private Object getDefaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (returnType == boolean.class) {
            return false;
        }
        if (returnType == char.class) {
            return '\0';
        }
        if (returnType == byte.class) {
            return (byte) 0;
        }
        if (returnType == short.class) {
            return (short) 0;
        }
        if (returnType == int.class) {
            return 0;
        }
        if (returnType == long.class) {
            return 0L;
        }
        if (returnType == float.class) {
            return 0F;
        }
        if (returnType == double.class) {
            return 0D;
        }
        return null;
    }
}

package net.ucrafts.captcha.utils;

import org.bukkit.entity.Player;

import java.net.InetSocketAddress;

public class PlayerUtils
{

    public static String getIP(Player player)
    {
        String ip = null;

        try {
            InetSocketAddress address = player.getAddress();

            if (address != null) {
                ip = address.getHostString();
            }
        } catch (NullPointerException exception) {
            return ip;
        }

        return ip;
    }
}

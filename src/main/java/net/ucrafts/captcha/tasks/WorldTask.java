package net.ucrafts.captcha.tasks;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldTask implements Runnable
{
    @Override
    public void run() {
        World world = Bukkit.getWorld("world");

        if (world != null) {
            world.setThundering(false);
            world.setStorm(false);
            world.setTime(1000);
        }
    }
}

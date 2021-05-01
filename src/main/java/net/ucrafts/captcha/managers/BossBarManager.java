package net.ucrafts.captcha.managers;

import de.leonhard.storage.internal.FlatFile;
import net.ucrafts.captcha.CaptchaPlugin;
import net.ucrafts.captcha.types.ConfigType;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class BossBarManager
{

    private final CaptchaPlugin plugin;
    private final FlatFile config;
    private final HashMap<Player, Float> counter = new HashMap<>();
    private final HashMap<Player, BukkitTask> tasks = new HashMap<>();
    private final HashMap<Player, BossBar> list = new HashMap<>();

    public BossBarManager(CaptchaPlugin plugin)
    {
        this.plugin = plugin;
        this.config = this.plugin.getCfg();
    }

    public void sendProgressBar(Player player)
    {
        this.tasks.put(player, this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> {
            float time = 1;
            float maxTime = this.config.getInt(ConfigType.CAPTCHA_TIMEOUT.toString());

            if (this.counter.containsKey(player)) {
                time = this.counter.get(player) + 1;
            }

            String title = this.config.getString(ConfigType.MESSAGE_BOSS_BAR.toString());
            BossBar bossBar = Bukkit.createBossBar(title, BarColor.RED, BarStyle.SOLID);
            float progress = time / maxTime;

            if (progress > 1.0) {
                progress = 1;
            }

            if (time >= maxTime) {
                player.kickPlayer(this.config.getString(ConfigType.MESSAGE_KICK_MESSAGE.toString()));
                return;
            }

            if (this.list.containsKey(player)) {
                bossBar = this.list.get(player);
            } else {
                bossBar.addPlayer(player);
            }

            bossBar.setTitle(title);
            bossBar.setProgress(progress);

            this.list.put(player, bossBar);
            this.counter.put(player, time);
        }, 0, 20));
    }

    public void clearPlayer(Player player)
    {
        this.counter.remove(player);

        if (this.tasks.containsKey(player)) {
            this.tasks.get(player).cancel();
            this.tasks.remove(player);
        }

        this.list.remove(player);
    }
}

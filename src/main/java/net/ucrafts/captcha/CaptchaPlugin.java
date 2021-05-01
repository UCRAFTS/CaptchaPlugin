package net.ucrafts.captcha;

import de.leonhard.storage.internal.FlatFile;
import net.ucrafts.captcha.listeners.PlayerListener;
import net.ucrafts.captcha.managers.BossBarManager;
import net.ucrafts.captcha.managers.CaptchaManager;
import net.ucrafts.captcha.managers.PlayerManager;
import net.ucrafts.captcha.tasks.WorldTask;
import net.ucrafts.captcha.types.ConfigType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.scheduler.BukkitTask;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;

import static org.bukkit.plugin.java.annotation.plugin.ApiVersion.Target.v1_15;

@Plugin(
        name = "CaptchaPlugin",
        version = "1.0.0"
)
@Author(value = "oDD1 / Alexander Repin")
@Description(value = "Display captcha for player from URI")
@ApiVersion(v1_15)
public class CaptchaPlugin extends JavaPlugin
{

    private final HashSet<BukkitTask> tasks = new HashSet<>();
    private Config config;
    private JedisPool jedis;
    private CaptchaManager captchaManager;
    private BossBarManager bossBarManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable()
    {
        this.config = new Config(this);
        this.config.init();

        this.jedis = this.getJedis();
        this.bossBarManager = new BossBarManager(this);
        this.captchaManager = new CaptchaManager(this, this.jedis);
        this.playerManager = new PlayerManager(this);

        this.registerListeners();
        this.registerTasks();
    }

    public FlatFile getCfg()
    {
        return this.config.getConfig();
    }

    private JedisPool getJedis()
    {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(this.getCfg().getInt(ConfigType.REDIS_POOL_SIZE.toString()));

        return new JedisPool(
                poolConfig,
                this.getCfg().getString(ConfigType.REDIS_HOST.toString()),
                this.getCfg().getInt(ConfigType.REDIS_PORT.toString()),
                this.getCfg().getInt(ConfigType.REDIS_TIMEOUT.toString()),
                this.getCfg().getString(ConfigType.REDIS_PASS.toString())
        );
    }

    public CaptchaManager getCaptchaManager()
    {
        return this.captchaManager;
    }

    public BossBarManager getBossBarManager()
    {
        return this.bossBarManager;
    }

    public PlayerManager getPlayerManager()
    {
        return this.playerManager;
    }

    private void registerListeners()
    {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    private void registerTasks()
    {
        this.tasks.add(
                this.getServer().getScheduler().runTaskTimer(this, new WorldTask(), 0, 20)
        );
    }

    @Override
    public void onDisable()
    {
        for (BukkitTask task : this.tasks) {
            task.cancel();
        }

        this.tasks.clear();
        this.jedis.close();
    }
}

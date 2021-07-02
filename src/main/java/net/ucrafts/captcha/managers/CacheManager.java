package net.ucrafts.captcha.managers;

import de.leonhard.storage.internal.FlatFile;
import net.ucrafts.captcha.CaptchaPlugin;
import net.ucrafts.captcha.types.ConfigType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

public class CacheManager
{

    private final CaptchaPlugin plugin;
    private final JedisPool jedis;
    private final FlatFile config;

    public CacheManager(CaptchaPlugin plugin, JedisPool jedis, FlatFile config)
    {
        this.plugin = plugin;
        this.jedis = jedis;
        this.config = config;
    }

    public boolean isCached(UUID uuid, String ip)
    {
        // DIRTY!
        String IP = ip == null ? "127.0.0.1" : ip;
        boolean isCached = false;
        Jedis j = this.jedis.getResource();
        j.select(this.config.getInt(ConfigType.REDIS_SESSION.toString()));

        if (j.exists(uuid.toString())) {
            isCached = true;
        }

        j.close();

        return isCached;
    }

    public void addCache(UUID uuid, String ip)
    {
        // DIRTY!
        String IP = ip == null ? "127.0.0.1" : ip;

        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            Jedis j = this.jedis.getResource();
            j.select(this.config.getInt(ConfigType.REDIS_SESSION.toString()));
            j.set(uuid.toString(), ip);
            j.expire(uuid.toString(), 604800);
            j.close();
        });
    }
}

package net.ucrafts.captcha.managers;

import com.github.johnnyjayjay.spigotmaps.RenderedMap;
import com.github.johnnyjayjay.spigotmaps.rendering.ImageRenderer;
import de.leonhard.storage.internal.FlatFile;
import net.ucrafts.captcha.CaptchaPlugin;
import net.ucrafts.captcha.types.ConfigType;
import net.ucrafts.captcha.utils.HTTPUtils;
import net.ucrafts.captcha.utils.ImageUtils;
import net.ucrafts.captcha.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

public class CaptchaManager
{

    private final CaptchaPlugin plugin;
    private final FlatFile config;
    private final JedisPool jedis;
    private final BossBarManager bossBarManager;
    private final CacheManager cacheManager;

    public final HashMap<Player, Integer> attempts = new HashMap<>();
    public final HashMap<Player, String> captcha = new HashMap<>();

    public CaptchaManager(CaptchaPlugin plugin, JedisPool jedis)
    {
        this.plugin = plugin;
        this.config = this.plugin.getCfg();
        this.jedis = jedis;
        this.bossBarManager = this.plugin.getBossBarManager();
        this.cacheManager = new CacheManager(this.plugin, this.jedis, this.config);
    }

    public void clearPlayer(Player player)
    {
        this.attempts.remove(player);
        this.captcha.remove(player);
        this.bossBarManager.clearPlayer(player);
    }

    public void loadCaptcha(Player player, boolean isReload)
    {
        player.sendMessage(this.config.getString(ConfigType.MESSAGE_WAITING.toString()));

        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try {
               ItemStack captcha = this.getCaptcha(player);

                if (captcha == null) {
                    throw new RuntimeException("Error create captcha item");
                }

                this.bossBarManager.sendProgressBar(player);

                if (!isReload) {
                    List<String> messages = this.config.getStringList(ConfigType.MESSAGE_MOTD.toString());

                    for (String message : messages) {
                        player.sendMessage(message);
                    }
                }

                player.setItemInHand(captcha);
            } catch (Throwable e) {
                this.plugin.getServer().getScheduler().runTask(
                        this.plugin, () -> player.sendMessage(this.config.getString(ConfigType.MESSAGE_ERROR.toString()))
                );
            }
        });
    }

    private ItemStack getCaptcha(Player player)
    {
        JSONObject json = HTTPUtils.get(this.config.getString(ConfigType.CAPTCHA_API.toString()));

        if (!json.has("code") || !json.has("success") || !json.getBoolean("success")) {
            throw new RuntimeException("Incorrect response");
        }

        String code = json.getString("code");
        Jedis j = this.jedis.getResource();
        j.select(this.config.getInt(ConfigType.REDIS_CAPTCHA.toString()));

        if (!j.exists(code)) {
            throw new RuntimeException("Not found captcha by code");
        }

        String raw = j.get(code);
        j.del(code);
        j.close();

        this.captcha.put(player, code.toLowerCase());

        return this.createItem(player, raw);
    }

    private ItemStack createItem(Player player, String raw)
    {
        try {
            BufferedImage bufferedImage = ImageUtils.base64ToBuffer(raw);

            if (bufferedImage == null) {
                return null;
            }

            ImageRenderer render = ImageRenderer.builder()
                    .addPlayers(player)
                    .image(bufferedImage)
                    .build();

            return RenderedMap.create(render).createItemStack();
        } catch (Throwable e) {
            return null;
        }
    }

    public boolean isNeedCaptcha(Player player)
    {
        String ip = PlayerUtils.getIP(player);

        if (ip == null) {
            return true;
        }

        return !this.cacheManager.isCached(player.getUniqueId(), ip);
    }

    public boolean hasAttempts(Player player)
    {
        if (!this.attempts.containsKey(player)) {
            return true;
        }

        return this.attempts.get(player) <= (this.config.getInt(ConfigType.CAPTCHA_ATTEMPTS.toString()) - 1);
    }

    public boolean isCaptchaValid(Player player, String captcha)
    {
        if (this.captcha.containsKey(player)) {
            if (this.captcha.get(player).equals(captcha.toLowerCase())) {
                this.cacheManager.addCache(player.getUniqueId(), PlayerUtils.getIP(player));
                return true;
            }
        }

        if (this.attempts.containsKey(player)) {
            this.attempts.put(player, this.attempts.get(player) + 1);
        } else {
            this.attempts.put(player, 1);
        }

        return false;
    }
}

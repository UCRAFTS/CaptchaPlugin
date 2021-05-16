package net.ucrafts.captcha.listeners;

import de.leonhard.storage.internal.FlatFile;
import net.ucrafts.captcha.CaptchaPlugin;
import net.ucrafts.captcha.managers.CaptchaManager;
import net.ucrafts.captcha.managers.PlayerManager;
import net.ucrafts.captcha.types.ConfigType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener
{

    private final CaptchaPlugin plugin;
    private final FlatFile config;
    private final CaptchaManager captchaManager;
    private final PlayerManager playerManager;

    public PlayerListener(CaptchaPlugin plugin)
    {
        this.plugin = plugin;
        this.config = this.plugin.getCfg();
        this.captchaManager = this.plugin.getCaptchaManager();
        this.playerManager = this.plugin.getPlayerManager();
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e)
    {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(PlayerChatEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        e.setJoinMessage("");

        Player player = e.getPlayer();

        Bukkit.getOnlinePlayers().forEach((p) -> {
            if (p != player) {
                player.hidePlayer(this.plugin, p);
                p.hidePlayer(this.plugin, player);
            }
        });

        for (int i = 0; i <= 20; i++) {
            player.sendMessage(" ");
        }

        Location location = player.getLocation();
        location.setX(0);
        location.setY(253);
        location.setZ(0);
        location.setYaw(0);
        location.setPitch(90);

        player.teleport(location);
        player.getInventory().clear();
        player.setHealth(1);
        player.setPlayerListName(" ");

        this.captchaManager.updateLoginPerSecond();

        if (this.captchaManager.isNeedCaptcha(player)) {
            this.captchaManager.loadCaptcha(player, false);
        } else {
            this.playerManager.teleport(player);
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerAsyncChat(AsyncPlayerChatEvent e)
    {
        Player player = e.getPlayer();
        String message = e.getMessage();

        Bukkit.getScheduler().runTask(this.plugin, () -> {
            if (this.captchaManager.hasAttempts(player)) {
                if (this.captchaManager.isCaptchaValid(player, message)) {
                    player.sendMessage(this.config.getString(ConfigType.MESSAGE_CAPTCHA_IS_VALID.toString()));

                    this.playerManager.teleport(player);
                } else {
                    if (this.config.getBoolean(ConfigType.CAPTCHA_RELOAD_AFTER_ERROR.toString())) {
                        player.getInventory().clear();
                        this.captchaManager.loadCaptcha(player, true);
                    }

                    player.sendMessage(this.config.getString(ConfigType.MESSAGE_CAPTCHA_IS_INVALID.toString()));
                }
            } else {
                this.playerManager.ban(player);
            }
        });

        e.setMessage("");
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        e.setQuitMessage("");

        this.captchaManager.clearPlayer(e.getPlayer());
    }
}

package net.ucrafts.captcha.managers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.leonhard.storage.internal.FlatFile;
import net.ucrafts.captcha.CaptchaPlugin;
import net.ucrafts.captcha.types.ConfigType;
import org.bukkit.entity.Player;

import java.util.Date;

public class PlayerManager
{

    private final CaptchaPlugin plugin;
    private final FlatFile config;

    public PlayerManager(CaptchaPlugin plugin)
    {
        this.plugin = plugin;
        this.config = this.plugin.getCfg();
    }

    public void teleport(Player player)
    {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(this.config.getString(ConfigType.CAPTCHA_REDIRECT_SERVER.toString()));
        player.sendPluginMessage(this.plugin, "BungeeCord", out.toByteArray());
    }

    public void ban(Player player)
    {
        long seconds = this.config.getLong(ConfigType.CAPTCHA_BAN_TIME.toString()) * 60;
        Date date = new Date(System.currentTimeMillis() + seconds * 1000);

        player.banPlayerFull(this.config.getString(ConfigType.MESSAGE_BAN_MESSAGE.toString()), date);
    }
}

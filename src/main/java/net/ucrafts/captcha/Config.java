package net.ucrafts.captcha;

import de.leonhard.storage.Json;
import de.leonhard.storage.internal.FlatFile;
import net.ucrafts.captcha.types.ConfigType;

import java.util.Arrays;

public class Config
{

    private final FlatFile config;

    public Config(CaptchaPlugin plugin)
    {
        this.config = new Json("config", plugin.getDataFolder().getPath());
    }

    public FlatFile getConfig()
    {
        return this.config;
    }

    public void init()
    {
        this.config.setDefault(ConfigType.MESSAGE_CAPTCHA_IS_VALID.toString(), "§6Система «Я не робот»§8: §aПроверка успешно пройдена!");
        this.config.setDefault(ConfigType.MESSAGE_CAPTCHA_IS_INVALID.toString(), "§cСистема «Я не робот»§8: §7Ошибка, попробуйте еще раз!");
        this.config.setDefault(ConfigType.MESSAGE_WAITING.toString(), "§cСистема «Я не робот»§8: §7Пожалуйста, подождите..");
        this.config.setDefault(ConfigType.MESSAGE_ERROR.toString(), "§cСистема «Я не робот»§8: §7Ошибка обработки запроса! Пожалуйста, перезайдите!");
        this.config.setDefault(ConfigType.MESSAGE_BAN_MESSAGE.toString(), "§cСистема «Я не робот»: §7Вы временно забанены!");
        this.config.setDefault(ConfigType.MESSAGE_KICK_MESSAGE.toString(), "§cСистема «Я не робот»: §7Вы не расшифровали изображение во время!");
        this.config.setDefault(ConfigType.MESSAGE_BOSS_BAR.toString(), "§fСистема «Я не робот»");
        this.config.setDefault(ConfigType.CAPTCHA_API.toString(), "http://127.0.0.1:8081/api/v1/captcha/create");
        this.config.setDefault(ConfigType.CAPTCHA_ATTEMPTS.toString(), 3);
        this.config.setDefault(ConfigType.CAPTCHA_TIMEOUT.toString(), 60);
        this.config.setDefault(ConfigType.CAPTCHA_RELOAD_AFTER_ERROR.toString(), true);
        this.config.setDefault(ConfigType.CAPTCHA_REDIRECT_SERVER.toString(), "server");
        this.config.setDefault(ConfigType.CAPTCHA_BAN_TIME.toString(), 1);
        this.config.setDefault(ConfigType.REDIS_HOST.toString(), "127.0.0.1");
        this.config.setDefault(ConfigType.REDIS_PORT.toString(), 6379);
        this.config.setDefault(ConfigType.REDIS_PASS.toString(), "secret");
        this.config.setDefault(ConfigType.REDIS_TIMEOUT.toString(), 60);
        this.config.setDefault(ConfigType.REDIS_CAPTCHA.toString(), 5);
        this.config.setDefault(ConfigType.REDIS_POOL_SIZE.toString(), 20);
        this.config.setDefault(ConfigType.MESSAGE_MOTD.toString(), Arrays.asList(
                "§cСистема «Я не робот»§8: §7Добро пожаловать!",
                "§cСистема «Я не робот»§8: §7Пожалуйста, расшифруйте текст",
                "§cСистема «Я не робот»§8: §7на карте. У Вас есть",
                "§cСистема «Я не робот»§8: §f60 секунд §7и §f3 попытки"
        ));
    }
}

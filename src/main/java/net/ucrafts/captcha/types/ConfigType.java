package net.ucrafts.captcha.types;

public enum ConfigType
{

    MESSAGE_CAPTCHA_IS_VALID("messages.captchaIsValid"),
    MESSAGE_CAPTCHA_IS_INVALID("messages.captchaIsInvalid"),
    MESSAGE_WAITING("messages.waiting"),
    MESSAGE_ERROR("messages.error"),
    MESSAGE_BAN_MESSAGE("messages.banMessage"),
    MESSAGE_KICK_MESSAGE("messages.kickMessage"),
    MESSAGE_BOSS_BAR("messages.bossBar"),
    CAPTCHA_API("captcha.api"),
    REDIS_HOST("redis.host"),
    REDIS_PORT("redis.port"),
    REDIS_TIMEOUT("redis.timeout"),
    REDIS_PASS("redis.pass"),
    REDIS_CAPTCHA("redis.captcha"),
    REDIS_POOL_SIZE("redis.poolSize"),
    CAPTCHA_ATTEMPTS("captcha.attempts"),
    CAPTCHA_TIMEOUT("captcha.timeout"),
    CAPTCHA_REDIRECT_SERVER("captcha.redirectServer"),
    CAPTCHA_BAN_TIME("captcha.banTime"),
    CAPTCHA_RELOAD_AFTER_ERROR("captcha.reloadAfterError"),
    MESSAGE_MOTD("messages.motd");

    private final String name;

    ConfigType(final String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return this.name;
    }
}

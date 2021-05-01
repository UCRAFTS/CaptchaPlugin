CaptchaPlugin
=
Плагин выдает игроку предмет карты с изображенной каптчей. Игроку необходимо расшифровать изображения в ограниченное время и кол-во попыток.

В случае успеха игрока телепортирует на указанный в конфигурации сервер.

Для работы плагина необходим **Redis** и связка с [CaptchaService](https://github.com/UCRAFTS/CaptchaService)!

## Конфигурация
Идентификатор | Значение
---|---
`redis.host` | IP адрес Redis
`redis.port` | Порт Redis
`redis.pass` | Пароль Redis, при наличии
`redis.timeout` | Максимальное время ожидания в секундах для Redis
`redis.poolSize` | Максимальный размер пула Redis
`redis.captcha` | Индекс базы Redis где хранятся сами каптчи в сыром виде
`captcha.api` | Ссылка на end-point [CaptchaService](https://github.com/UCRAFTS/CaptchaService)
`captcha.attempts` | Кол-во попыток отгадывания пока игрока не забанит
`captcha.timeout` | Время ожидания игрока на сервере
`captcha.redirectServer` | Наименование сервера для последующей телепортации
`captcha.banTime` | Время бана игрока в минутах
`captcha.reloadAfterError` | Перезагружает каптчу для игрока в случае ошибки отгадывания, по умолчанию `true`
`messages.motd` | Массив сообщения приветствия перед началом загрузки каптчи
`messages.bossBar` | Наименование на босс-баре
`messages.kickMessage` | Сообщение кика
`messages.banMessage` | Сообщение бана
`messages.error` | Сообщение об ошибке
`messages.waiting` | Сообщение загрузки каптчи
`messages.captchaIsInvalid` | Сообщение о неудачной попытке
`messages.captchaIsValid` | Сообщение об удачной попытке

Плагин разрабатывался и тестировался на версии **1.16.5** под управлением **PaperSpigot**.
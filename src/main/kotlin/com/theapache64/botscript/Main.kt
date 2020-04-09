package com.theapache64.botscript

import com.google.gson.Gson
import com.theapache64.botscript.model.Config
import com.theapache64.botscript.utils.InputUtils
import com.theapache64.botscript.utils.SimpleCommandExecutor
import java.io.File
import java.lang.IllegalArgumentException

private const val CONFIG_FILE_NAME = "bot-script-config.json"
private const val COMMAND_INIT = "init"
private const val COMMAND_SET_WEB_HOOK_URL = "hook"
private const val OPTION_HOOK_PRODUCTION = "p"
private const val OPTION_HOOK_DEVELOPMENT = "d"
private val gson by lazy { Gson() }

fun main(args: Array<String>) {

    if (args.isNotEmpty()) {
        when (val command = args.first()) {

            COMMAND_INIT -> {
                initBot()
            }

            COMMAND_SET_WEB_HOOK_URL -> {
                setHook(args)
            }

            else -> throw IllegalArgumentException("Invalid command `$command`")
        }

    } else {
        println("Missing arguments")
    }
}

fun setHook(args: Array<String>) {
    if (args.size > 1) {
        val option = args[1]
        val config = getConfig()

        when (option) {

            OPTION_HOOK_DEVELOPMENT -> {
                setWebHookUrl(config.devToken, config.devWebHookUrl)
            }
            OPTION_HOOK_PRODUCTION -> {
                setWebHookUrl(config.prodToken, config.prodWebHookUrl)
            }
            else -> {
                throw IllegalArgumentException("Invalid hook option `$option`")
            }
        }


    } else {
        throw IllegalArgumentException(
            "Hook option missing. Specify `$OPTION_HOOK_DEVELOPMENT` for development and " +
                    "$OPTION_HOOK_DEVELOPMENT for production "
        )
    }
}

fun setWebHookUrl(token: String, url: String) {
    val curl = "curl -s -X POST https://api.telegram.org/bot$token/setWebhook?url=$url"
    println(curl)
    SimpleCommandExecutor.executeCommand(curl, true)
}


fun getConfig(): Config {
    val configFile = File(getConfigPath())
    return gson.fromJson(configFile.readText(), Config::class.java)
}

fun initBot() {

    val devToken = InputUtils.getString("Enter dev-bot token", true)
    val prodToken = InputUtils.getString("Enter prod-bot token", true)
    val devUrl = InputUtils.getString("Enter dev-webhook URL", true)
    val prodUrl = InputUtils.getString("Enter prod-webhook URL", true)
    val config = Config(
        devToken,
        devUrl,
        prodToken,
        prodUrl
    )

    val configJson = gson.toJson(config)
    File(getConfigPath()).writeText(configJson)
    println("👍 Init success")

}

fun getConfigPath(): String = "${System.getProperty("user.dir")}/$CONFIG_FILE_NAME"




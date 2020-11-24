package me.geek.tom.serverutils.bot

import com.mojang.authlib.GameProfile
import com.uchuhimo.konf.Config
import me.geek.tom.serverutils.GeneralSpec
import me.geek.tom.serverutils.bot.impl.DiscordBotConnection
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import org.apache.logging.log4j.LogManager

private val LOGGER = LogManager.getLogger()

interface BotConnection {

    fun connect(server: MinecraftServer)
    fun disconnect()
    fun onChatMessage(user: GameProfile, headOverlay: Boolean, message: String)
    fun onPlayerLeave(player: ServerPlayerEntity)
    fun onPlayerJoin(player: ServerPlayerEntity)

}

class NoopBotConnection : BotConnection {
    override fun connect(server: MinecraftServer) {
        LOGGER.info("Connect")
    }
    override fun disconnect() {
        LOGGER.info("Disconnect")
    }
    override fun onChatMessage(user: GameProfile, headOverlay: Boolean, message: String) {
        LOGGER.info("Chat: ${user.name} $message")
    }
    override fun onPlayerLeave(player: ServerPlayerEntity) { }
    override fun onPlayerJoin(player: ServerPlayerEntity) { }
}

enum class BotType {
    NONE, DISCORD
}

fun loadBot(config: Config): BotConnection {
    return when (config[GeneralSpec.mode]) {
        BotType.NONE -> NoopBotConnection()
        BotType.DISCORD -> DiscordBotConnection(config)
    }
}
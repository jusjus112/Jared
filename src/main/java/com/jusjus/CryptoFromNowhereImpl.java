package com.jusjus;

import com.google.common.collect.Lists;
import com.jusjus.assistant.AssistantController;
import com.jusjus.includes.audio.AudioController;
import com.jusjus.includes.command.CommandManager;
import com.jusjus.includes.command.command.CommandChangelog;
import com.jusjus.includes.command.command.CommandHelp;
import com.jusjus.includes.components.ComponentsController;
import com.jusjus.includes.module.DiscordModule;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import java.util.Arrays;
import java.util.List;
import javax.security.auth.login.LoginException;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

@Getter
public class CryptoFromNowhereImpl {

  private CommandManager commandManager;
  private ComponentsController componentController;
  private AssistantController assistantController;

  private final List<DiscordModule<CryptoFromNowhereImpl>> modules = Lists.newArrayList();

  private static JDA bot;
  @Getter
  private static Guild guild;

  public void initialize() throws LoginException, InterruptedException {
    // Module Initialization
    commandManager = new CommandManager(this);
    componentController = new ComponentsController(this);
    this.assistantController = new AssistantController(this);

    JDABuilder builder = JDABuilder.createDefault("SECRET");
    builder.setAutoReconnect(true);
    builder.setActivity(Activity.watching("you"));
    builder.enableIntents(GatewayIntent.GUILD_VOICE_STATES);
    builder.enableCache(CacheFlag.VOICE_STATE);
    builder.setChunkingFilter(ChunkingFilter.ALL);
//    builder.setMemberCachePolicy(MemberCachePolicy.ALL);

    System.out.println(getModules());

    AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    AudioSourceManagers.registerRemoteSources(playerManager);
    new AudioController(this, playerManager);

    getModules().forEach(discordModule -> {
      // Register event listeners
      Arrays.stream(discordModule.registerEventListeners()).forEachOrdered(
          builder::addEventListeners);

      System.out.println("Registering for " + discordModule.toString());
      // Register commands
      Arrays.stream(discordModule.registerCommands(this.commandManager)).forEachOrdered(discordCommand -> {
        CommandManager.get().addCommand(discordCommand);
      });
    });

    bot = builder.build().awaitReady();
    guild = bot.getGuilds().iterator().next();

    System.out.println("Guilds: " + bot.getGuilds());

    getModules().forEach(discordModule-> {
      discordModule.init(CryptoFromNowhereImpl.this);
    });

//    AudioManager audioManager = guild.getAudioManager();
//    new EchoHandler(audioManager);
  }

}

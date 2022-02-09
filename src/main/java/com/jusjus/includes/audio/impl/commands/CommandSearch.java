package com.jusjus.includes.audio.impl.commands;

import com.jusjus.includes.DiscordRank;
import com.jusjus.includes.audio.AudioController;
import com.jusjus.includes.command.CommandManager;
import com.jusjus.includes.command.object.DiscordCommand;
import com.jusjus.utilities.UtilString;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class CommandSearch extends DiscordCommand {

  private final AudioController audioController;

  public CommandSearch(AudioController audioController, CommandManager commandManager){
    super(commandManager, "Search videos on youtube.",
        DiscordRank.NONE, "!", "search");

    this.audioController = audioController;
  }

  @Override
  public void execute(Member member, MessageChannel channel, Message message, String rawContent,
      String[] args) {

    System.out.println("Play message: " + rawContent);

    try{
      this.audioController.getYoutubeHandler().searchAndPlay(rawContent, message);
    }catch (Exception exception){
      message.reply(exception.getMessage()).queue();
      exception.printStackTrace();
    }
  }
}

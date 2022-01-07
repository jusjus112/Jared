package com.jusjus.includes.audio.impl.commands;

import com.jusjus.includes.audio.AudioController;
import com.jusjus.includes.DiscordRank;
import com.jusjus.includes.command.CommandManager;
import com.jusjus.includes.command.object.DiscordCommand;
import com.jusjus.utilities.UtilString;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class CommandPlay extends DiscordCommand {

  public final static String YOUTUBE_PATTERN = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
  private final AudioController audioController;

  public CommandPlay(AudioController audioController, CommandManager commandManager){
    super(commandManager, "Something...",
        DiscordRank.NONE, "!", "play");

    this.audioController = audioController;
  }

  @Override
  public void execute(Member member, MessageChannel channel, Message message, String rawContent,
      String[] args) {
    String url = UtilString.removeSpaces(rawContent);
    Pattern p = Pattern.compile(YOUTUBE_PATTERN);
    Matcher m = p.matcher(url);

    System.out.println("Play message: " + url);

    if (m.find()){
      try{
        this.audioController.load(message, member, channel, url);
      }catch (Exception exception){
        message.reply(exception.getMessage()).queue();
        exception.printStackTrace();
      }
    }else{
      message.reply("Mate are you dumb or what? You don't know what a youtube link is?").queue();
    }
  }
}

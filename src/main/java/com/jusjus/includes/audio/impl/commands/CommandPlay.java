package com.jusjus.includes.audio.impl.commands;

import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.SearchListResponse;
import com.jusjus.includes.audio.AudioController;
import com.jusjus.includes.DiscordRank;
import com.jusjus.includes.command.CommandManager;
import com.jusjus.includes.command.object.DiscordCommand;
import com.jusjus.includes.components.button.object.DiscordButton;
import com.jusjus.utilities.UtilString;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.interactions.components.Button;

public class CommandPlay extends DiscordCommand {

  public final static String YOUTUBE_PATTERN = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
  private final AudioController audioController;

  public CommandPlay(AudioController audioController, CommandManager commandManager){
    super(commandManager, "Play music through youtube.",
        DiscordRank.NONE, "!", "play");

    this.audioController = audioController;
  }

  @Override
  public void execute(Member member, MessageChannel channel, Message message, String rawContent,
      String[] args) {

    Pattern p = Pattern.compile(YOUTUBE_PATTERN);
    String url = UtilString.removeSpaces(rawContent);
    Matcher m = p.matcher(url);

    System.out.println("Play message: " + rawContent);

    if (m.find()){
      try{
        this.audioController.load(message, url);
      }catch (Exception exception){
        message.reply(exception.getMessage()).queue();
        exception.printStackTrace();
      }
    }else{
      try{
        this.audioController.getYoutubeHandler().searchAndPlay(rawContent, message);
      }catch (Exception exception){
        message.reply(exception.getMessage()).queue();
        exception.printStackTrace();
      }
    }
  }
}

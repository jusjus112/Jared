package com.jusjus.includes.audio.impl.commands;

import com.jusjus.includes.DiscordRank;
import com.jusjus.includes.audio.AudioController;
import com.jusjus.includes.command.CommandManager;
import com.jusjus.includes.command.object.DiscordCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class CommandSkip extends DiscordCommand {

  private final AudioController audioController;

  public CommandSkip(AudioController audioController, CommandManager commandManager){
    super(commandManager, "Something...",
        DiscordRank.NONE, "!", "skip");

    this.audioController = audioController;
  }

  @Override
  public void execute(Member member, MessageChannel channel, Message message, String rawContent,
      String[] args) {

    if (this.audioController.getAudioTracker().skipCurrent()) {
      message.reply("Skipped current song.").queue();
      message.reply(
          "New song: **" + this.audioController.getAudioTracker().getCurrentTrack().track()
              .getInfo().title + "**").queue();
    }else{
      message.reply("I can't skip this track for you, unknown next song or something went wrong.").queue();
    }
  }
}

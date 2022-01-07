package com.jusjus.includes.audio.impl.commands;

import com.jusjus.includes.DiscordRank;
import com.jusjus.includes.audio.AudioController;
import com.jusjus.includes.command.CommandManager;
import com.jusjus.includes.command.object.DiscordCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class CommandListQueue extends DiscordCommand {

  private final AudioController audioController;

  public CommandListQueue(AudioController audioController, CommandManager commandManager){
    super(commandManager, "Something...",
        DiscordRank.NONE, "!", "queue");

    this.audioController = audioController;
  }

  @Override
  public void execute(Member member, MessageChannel channel, Message message, String rawContent,
      String[] args) {

    this.audioController.getAudioTracker().listQueue(channel);
  }
}

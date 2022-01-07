package com.jusjus.includes.audio;

import com.jusjus.CryptoFromNowhereImpl;
import com.jusjus.includes.audio.api.AudioInfo;
import com.jusjus.includes.audio.impl.AudioPlayerSendHandler;
import com.jusjus.includes.audio.impl.AudioTracker;
import com.jusjus.includes.audio.impl.commands.CommandListQueue;
import com.jusjus.includes.audio.impl.commands.CommandPlay;
import com.jusjus.includes.audio.impl.commands.CommandSkip;
import com.jusjus.includes.command.CommandManager;
import com.jusjus.includes.command.object.DiscordCommand;
import com.jusjus.includes.module.DiscordModule;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import java.util.function.Consumer;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AudioController extends DiscordModule<CryptoFromNowhereImpl>  {

  private final AudioPlayer audioPlayer;
  private AudioFrame lastFrame;
  @Getter
  private final AudioTracker audioTracker;
  private final AudioPlayerManager audioPlayerManager;
  private static AudioController instance;

  public static AudioController get(){
    return instance;
  }

  public AudioController(CryptoFromNowhereImpl impl, AudioPlayerManager playerManager){
    super(impl);
    instance = this;

    this.audioPlayerManager = playerManager;
    this.audioPlayer = playerManager.createPlayer();
    this.audioTracker = new AudioTracker(this, audioPlayer);
  }

  public void load(Message message, Member member, MessageChannel channel, String url) {
    getAudioTracker().tryToJoinVoiceChannel(channel, member);

    this.audioPlayerManager.loadItem(url, new AudioLoadResultHandler() {
      @Override
      public void trackLoaded(AudioTrack track) {
        AudioInfo audioInfo = newInfoObject(track, member, channel);
        if (audioTracker.getQueuedTracks().size() > 0)
          message.reply("Added this track to the queue").queue();
        else
          message.reply("Playing this track for you <3").queue();

        audioTracker.queue(audioInfo);
      }

      @Override
      public void playlistLoaded(AudioPlaylist playlist) {
        playlist.getTracks().forEach(track -> {
          AudioInfo audioInfo = newInfoObject(track, member, channel);
          audioTracker.queue(audioInfo);
        });
        message.reply("Added a total of " + playlist.getTracks().size() + " tracks for you.").queue();
        audioTracker.listQueue(message.getChannel());
      }

      @Override
      public void noMatches() {
        message.reply("There are no matches found for your input.").queue();
      }

      @Override
      public void loadFailed(FriendlyException e) {
        message.reply(e.getMessage()).queue();
      }
    });
  }

  public AudioInfo newInfoObject(AudioTrack track, Member member, Channel channel){
    return new AudioInfo() {
      @Override
      public AudioTrack track() {
        return track;
      }

      @Override
      public Member author() {
        return member;
      }

      @Override
      public Channel channel() {
        return channel;
      }

      @Override
      public AudioTrackInfo audioTrackInfo() {
        return track.getInfo();
      }
    };
  }

  @Override
  public void init(CryptoFromNowhereImpl plugin) {
    CryptoFromNowhereImpl.getGuild().getAudioManager().setSendingHandler(new AudioPlayerSendHandler(audioPlayer));
  }

  @Override
  public ListenerAdapter[] registerEventListeners() {
    return new ListenerAdapter[0];
  }

  @Override
  public DiscordCommand[] registerCommands(CommandManager commandManager) {
    return new DiscordCommand[]{
        new CommandPlay(this, commandManager),
        new CommandListQueue(this, commandManager),
        new CommandSkip(this, commandManager)
    };
  }

  @Override
  public void thread(Consumer perTickUpdater) {

  }
}

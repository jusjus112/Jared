package com.jusjus.includes.audio;

import static com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.SearchListResponse;
import com.jusjus.CryptoFromNowhereImpl;
import com.jusjus.includes.audio.api.AudioInfo;
import com.jusjus.includes.audio.impl.AudioPlayerSendHandler;
import com.jusjus.includes.audio.impl.AudioTracker;
import com.jusjus.includes.audio.impl.commands.CommandQueue;
import com.jusjus.includes.audio.impl.commands.CommandPlay;
import com.jusjus.includes.audio.impl.commands.CommandSearch;
import com.jusjus.includes.audio.impl.commands.CommandSkip;
import com.jusjus.includes.audio.youtubesearch.YoutubeHandler;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Emoji;
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
  @Getter
  private final YoutubeHandler youtubeHandler;

  public static AudioController get(){
    return instance;
  }

  public AudioController(CryptoFromNowhereImpl impl, AudioPlayerManager playerManager){
    super(impl);
    instance = this;

    this.audioPlayerManager = playerManager;
    this.audioPlayer = playerManager.createPlayer();
    this.youtubeHandler = new YoutubeHandler(impl, this);
    this.audioTracker = new AudioTracker(this, audioPlayer);
  }

  public void load(Message message, String url) {
    getAudioTracker().tryToJoinVoiceChannel(message.getChannel(), message.getMember());
    System.out.println("Loading: " + url);

    this.audioPlayerManager.loadItem(url, new AudioLoadResultHandler() {
      @Override
      public void trackLoaded(AudioTrack track) {
        System.out.println(track);
        AudioInfo audioInfo = newInfoObject(track, message.getMember(), message.getChannel());
        MessageChannel channel = message.getChannel();
        message.delete().queue();

        if (audioTracker.getQueuedTracks().size() > 0)
          channel.sendMessage(Emoji.fromMarkdown(":yes~3:") + "Adding **" + track.getInfo().title + "** to the queue").queue();
        else
          channel.sendMessage("Playing **" + track.getInfo().title + "**").queue();

        audioTracker.queue(audioInfo);
      }

      @Override
      public void playlistLoaded(AudioPlaylist playlist) {
        playlist.getTracks().forEach(track -> {
          AudioInfo audioInfo = newInfoObject(track, message.getMember(), message.getChannel());
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
        new CommandQueue(this, commandManager),
        new CommandSkip(this, commandManager),
        new CommandSearch(this, commandManager)
    };
  }

  @Override
  public void thread(Consumer perTickUpdater) {

  }
}

package com.jusjus.includes.audio.impl;

import com.jusjus.CryptoFromNowhereImpl;
import com.jusjus.includes.audio.AudioController;
import com.jusjus.includes.audio.api.AudioInfo;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent;
import com.sedmelluq.discord.lavaplayer.player.event.TrackStartEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

public class AudioTracker extends AudioEventAdapter {

  private final AudioPlayer audioPlayer;
  private final Queue<AudioInfo> queue;
  private final AudioController audioController;

  public AudioTracker(AudioController audioController, AudioPlayer audioPlayer){
    this.audioPlayer = audioPlayer;
    this.queue = new LinkedBlockingQueue<>();
    this.audioController = audioController;

    audioPlayer.addListener(this);
  }

  public void queue(AudioInfo info) {
    queue.add(info);

    if (audioPlayer.getPlayingTrack() == null) {
      audioPlayer.playTrack(info.track());
    }
  }

  public AudioInfo getCurrentTrack(){
    return this.queue.element();
  }

  public void skipCurrent(){
    this.audioPlayer.stopTrack();
    this.queue.remove();
    this.audioPlayer.playTrack(this.queue.element().track());
  }

  public boolean tryToJoinVoiceChannel(Channel channel, Member author){
    try{
      if (author.getVoiceState()!= null && author.getVoiceState().inAudioChannel()){
        AudioChannel audioChannel = author.getVoiceState().getChannel();
        CryptoFromNowhereImpl.getGuild().getAudioManager().openAudioConnection(audioChannel);
      }
      return true;
    }catch (NullPointerException exception){
      if (channel instanceof MessageChannel){
        ((MessageChannel) channel).sendMessage(
            "You need to be in a voice channel " + author.getAsMention()
        ).queue();
      }
      return false;
    }
  }

  public void onTrackStart(TrackStartEvent trackStartEvent) {
    AudioInfo audioInfo = queue.element();

    if (tryToJoinVoiceChannel(audioInfo.channel(), audioInfo.author())){

    }
  }

  public void onTrackEnd(TrackEndEvent trackEndEvent) {
    AudioTrack track = trackEndEvent.track;
    queue.remove();

    if (queue.isEmpty()) {
      CryptoFromNowhereImpl.getGuild().getAudioManager().closeAudioConnection();
    }else{
      trackEndEvent.player.playTrack(queue.element().track());
    }
  }

  @Override
  public void onEvent(com.sedmelluq.discord.lavaplayer.player.event.AudioEvent event) {
    if (event instanceof TrackStartEvent){
      onTrackStart((TrackStartEvent) event);
    }else if (event instanceof TrackEndEvent){
      onTrackEnd((TrackEndEvent) event);
    }
  }

  public Set<AudioInfo> getQueuedTracks() {
    return new LinkedHashSet<>(queue);
  }

  public void listQueue(MessageChannel messageChannel){
    StringBuilder builder = new StringBuilder();
    AtomicInteger place = new AtomicInteger(1);
    int limit = 10;

    getQueuedTracks().stream().limit(limit).forEach(info -> {
      int currentPlace = place.getAndIncrement();

      if (currentPlace == 1)
        builder.append("**").append(currentPlace).append(". ")
            .append(info.track().getInfo().title).append("**");
      else
        builder.append("\n").append(currentPlace).append(". ")
            .append(info.track().getInfo().title);

      if (currentPlace == limit){
        builder.append("\n..... and ")
            .append(this.audioController.getAudioTracker().getQueuedTracks().size() - limit)
            .append(" more songs.");
      }
    });
    messageChannel.sendMessage(builder.toString()).queue();
  }

}

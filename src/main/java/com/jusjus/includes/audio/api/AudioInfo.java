package com.jusjus.includes.audio.api;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import java.util.HashSet;
import java.util.Set;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Member;

public interface AudioInfo {

    /**
     *
     * @return
     */
    AudioTrack track();

    /**
     *
     */
    Set<String> skips = new HashSet<>();

    /**
     *
     * @return
     */
    Member author();

    /**
     *
     * @return
     */
    Channel channel();

    /**
     *
     * @return
     */
    AudioTrackInfo audioTrackInfo();

}
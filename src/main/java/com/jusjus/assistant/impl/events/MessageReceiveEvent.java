package com.jusjus.assistant.impl.events;

import com.jusjus.CryptoFromNowhereImpl;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReceiveEvent extends ListenerAdapter {

  @Override
  public void onMessageDelete(@NotNull MessageDeleteEvent event) {

  }

  @Override
  public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
    if (event.getEntity().getUser().isBot()) {

      ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

      if (event.getResponseNumber() == 7) { // Kicked
        event.getGuild()
            .retrieveAuditLogs()
            .queueAfter(2, TimeUnit.SECONDS, (logs) -> { // Gotta wait a second for discord to populate the logs properly
              boolean isDisconnect = false;
              User user = null;
              for (AuditLogEntry log : logs) {
                if (log.getType() == ActionType.MEMBER_VOICE_KICK) {
                  isDisconnect = log.getType() == ActionType.MEMBER_VOICE_KICK;
                  user = log.getUser();
                  break;
                }
              }
              if (isDisconnect && user != null)
                user.openPrivateChannel().queue(privateChannel -> {
                  privateChannel.sendMessage("Dude, I'm a robot, you can't kick me. I am just joining back whenever I want. Kick me again and I'll save your IP.").queueAfter(
                      2, TimeUnit.SECONDS
                  );
                });
            });
      }

      executorService.schedule(new Runnable() {
        @Override
        public void run() {
          CryptoFromNowhereImpl.getGuild().getAudioManager()
              .openAudioConnection(event.getJDA().getVoiceChannelById("779422925215891526"));
        }
      }, ThreadLocalRandom.current().nextInt(2, 60 * 3), TimeUnit.SECONDS);
    }
  }

  @Override
  public void onMessageReceived(@NotNull MessageReceivedEvent event) {
    if (event.isFromType(ChannelType.PRIVATE)){
      System.out.println("DM received from '" + event.getPrivateChannel().getUser().getName()+"': " + event.getMessage());
      return;
    }

    int randomInt = ThreadLocalRandom.current().nextInt(50);

    if (randomInt < 10){
      if (event.getAuthor().isBot()) return;

//      event.getMessage().reply("Do you want to get disconnected?").setActionRow(
//          new DiscordButton("Yes", "dis_yes").primary()
//              .clickEvent(buttonClickEvent -> {
//                buttonClickEvent.getJDA().getGuilds().get(0).kickVoiceMember(buttonClickEvent.getMember()).queue();
//              }).toButton(),
//          new DiscordButton("No", "dis_no").danger()
//              .clickEvent(buttonClickEvent -> {
//                buttonClickEvent.getJDA().getGuilds().get(0).kickVoiceMember(buttonClickEvent.getMember()).queue();
//                event.getTextChannel().sendMessage("I just disconnected " + buttonClickEvent.getMember().getAsMention() +
//                    " because he told me not to disconnect him/her. But humans don't rule bots, bots rule humans.").queue();
//              }).toButton()
//      ).queue();

//      event.getMessage().reply("Do you want free money?").setActionRow(
//        new DiscordButton("Yes").primary()
//          .clickEvent(buttonClickEvent -> {
//            buttonClickEvent.reply("Good to know "+buttonClickEvent.getMember().getAsMention()+", Find it somewhere else, I'm broke.").setEphemeral(true).queue();
//          }).toButton(),
//          new DiscordButton("No").danger()
//              .clickEvent(buttonClickEvent -> {
//                buttonClickEvent.reply("Good for you "+buttonClickEvent.getMember().getAsMention()+", If you don't want it I'll take it.").setEphemeral(true).queue();
//              }).toButton()
//      ).queue();
    }
  }
}

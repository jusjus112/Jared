package com.jusjus.includes.command.command;

import com.jusjus.includes.DiscordRank;
import com.jusjus.includes.command.CommandManager;
import com.jusjus.includes.command.object.DiscordCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.interactions.components.Button;

public class CommandChangelog extends DiscordCommand {

  public CommandChangelog(CommandManager commandManager){
    super(commandManager, "Something...",
        DiscordRank.NONE, "!", "crypto");
  }

  @Override
  public void execute(Member member, MessageChannel channel, Message message, String rawContent,
      String[] args) {

//    new CoinMarketCapController(channel);

    message.reply("This is a test").setActionRow(
        Button.primary("free_money", "Click me for free money")).queue();

//    member.getGuild().addRoleToMember(member,
//        Objects.requireNonNull(member.getGuild().getRoleById(Codes.CHANGELOG_ROLE_ID)))
//        .queue(aVoid -> {
//          channel.sendMessage("Added you " + member.getAsMention()).queue();
//        }, throwable -> {
//          // TODO: Failure
//        });
  }
}

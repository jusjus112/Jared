package com.jusjus.includes.command.command;

import com.jusjus.includes.DiscordRank;
import com.jusjus.includes.components.message.MessageBuilder;
import com.jusjus.includes.components.message.MessageBuilder.SetType;
import com.jusjus.includes.command.CommandManager;
import com.jusjus.includes.command.object.DiscordCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class CommandHelp extends DiscordCommand {

    public CommandHelp(CommandManager commandManager){
        super(commandManager, "Shows a list of all of the available commands!", DiscordRank.NONE, "!", "help");
    }

    @Override
    public void execute(Member member, MessageChannel channel,
        Message message, String rawContent, String[] args){

        MessageBuilder messageBuilder = new MessageBuilder(
            "List of all the commands available!",
            SetType.TITLE
        );

       getCommandManager().getCommands().forEach(discordCommand -> {
            if (discordCommand.isShowInHelp()) {
                messageBuilder.addField(
                    discordCommand.getAliases().get(0) +
                        (discordCommand.getUsage() == null ? "" : " " + discordCommand.getUsage()),
                    discordCommand.getDescription(),
                    false
                );
            }
        });

        messageBuilder.sendMessage(channel);
    }
}

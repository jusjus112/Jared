package com.jusjus.includes.command;

import com.jusjus.includes.components.message.MessageBuilder;
import com.jusjus.includes.command.object.DiscordCommand;
import java.awt.Color;
import java.util.Arrays;
import lombok.EqualsAndHashCode;
import lombok.Value;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
@Value
public class CommandListener extends ListenerAdapter {

    CommandManager commandManager;

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        System.out.println("Receiving slash command");
        for(DiscordCommand command : commandManager.getCommands()) {
            System.out.println("Receiving slash command: " + command);
            if (command.getAliases().contains(event.getCommandString())) {
                assert event.getMember() != null;
                if (event.getMember().hasPermission(command.getPermission())) {
                    command.execute(event.getMember(), event.getChannel(), null, event.getCommandString(), null);
                }
            }else{
                new MessageBuilder("No Permissions!",
                    "You do not have the required permissions to use this command!")
                    .overwriteColor(Color.RED)
                    .reply(event);
            }
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();
        Member member = event.getMember();
        String[] args = content.split(" ");
        String commandInput = args[0];
        // Fixes
        content = content.replaceAll(args[0], "");
        args = ArrayUtils.remove(args, 0); // Remove command from args

        for(DiscordCommand command : commandManager.getCommands()){
            if (command.getFormattedAliases().contains(commandInput)){
                assert member != null;
                if (member.hasPermission(command.getPermission())) {
//                    if (commandManager.checkSpam(member, commandInput)){
//                        channel.sendMessage(
//                            "**Please do NOT spam commands** " + member.getAsMention() + "!"
//                        ).queue();
//                        break;
//                    }
                    try{
//                        if (member.getId().equalsIgnoreCase("212319202185445376")){
//                            message.reply("Sorry, only important people can use this.").queue();
//                            return;
//                        }
                        command.execute(member, channel, message, content, args);
                    }catch (Exception exception){
                        new MessageBuilder("Something went wrong.",
                            "Whoops, I catched an error for command " + command.getAliases().get(0))
                            .addField("Error message:", exception.getMessage() + ".", true)
                            .addField("@ line:", Arrays.toString(
                                Arrays.stream(exception.getStackTrace()).limit(5).toArray()), false)
                            .overwriteColor(Color.RED)
                            .reply(event.getMessage());
                        exception.printStackTrace();
                        break;
                    }
                    break;
                }else{
                    new MessageBuilder("No Permissions!",
                        "You do not have the required permissions to use this command!")
                        .overwriteColor(Color.RED)
                        .reply(message);
                }
            }
        }
    }

}

package com.jusjus.includes.command.object;

import com.jusjus.includes.DiscordRank;
import com.jusjus.includes.command.CommandManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

@Getter
public abstract class DiscordCommand {

    private final String description;
    private final List<String> aliases;
    private final Permission permission;
    private final String prefix;
    @Setter
    private String usage;
    @Setter
    private boolean showInHelp = true;
    private final CommandManager commandManager;

    public DiscordCommand(CommandManager commandManager, String description, String prefix, String... aliases){
        this(commandManager, description, DiscordRank.NONE.getHighestPermission(), prefix, aliases);
    }

    public DiscordCommand(CommandManager commandManager, String description, DiscordRank rank, String prefix, String... aliases){
        this(commandManager, description, rank.getHighestPermission(), prefix, aliases);
    }

    public DiscordCommand(CommandManager commandManager, String description, Permission permission, String prefix, String... aliases){
        this.commandManager = commandManager;
        this.description = description;
        this.permission = permission;
        this.aliases = new ArrayList<>();
        this.prefix = prefix;

        Arrays.asList(aliases).forEach(cmd -> this.aliases.add(prefix+cmd));
    }

    public abstract void execute(Member member, MessageChannel channel,
        Message message, String rawContent, String[] args);

}

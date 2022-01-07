package com.jusjus.includes.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jusjus.CryptoFromNowhereImpl;
import com.jusjus.includes.command.command.CommandChangelog;
import com.jusjus.includes.command.command.CommandHelp;
import com.jusjus.includes.command.object.DiscordCommand;
import com.jusjus.includes.module.DiscordModule;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandManager extends DiscordModule<CryptoFromNowhereImpl> {

    @Getter
    private final List<DiscordCommand> commands;
    private final Map<Member, Integer> spamming;
    private final Map<Member, String> cache;

    private final int maxCommands = 2;

    private static CommandManager instance;

    public static CommandManager get(){
        return instance;
    }

    public CommandManager(CryptoFromNowhereImpl impl){
        super(impl);
        instance = this;

        this.spamming = Maps.newHashMap();
        this.cache = Maps.newHashMap();
        this.commands = Lists.newArrayList();
    }

    @Override
    public void init(CryptoFromNowhereImpl plugin) {

    }

    @Override
    public ListenerAdapter[] registerEventListeners() {
        return new ListenerAdapter[]{
            new CommandListener(this)
        };
    }

    @Override
    public DiscordCommand[] registerCommands(CommandManager commandManager) {
        return new DiscordCommand[]{
            new CommandChangelog(commandManager),
            new CommandHelp(commandManager)
        };
    }

    @Override
    public void thread(Consumer perTickUpdater) {

    }

    public boolean checkSpam(Member member, String command){
        if (isSameMessage(member, command)) {
            if (this.spamming.containsKey(member)) {
                int spamming = this.spamming.get(member);
                if (spamming >= this.maxCommands){
                    if (spamming >= this.maxCommands + 2) {
                        this.spamming.remove(member);
                    }
                    return true;
                }
                this.spamming.put(member, ++spamming);
            } else {
                this.spamming.put(member, 1);
            }
        }
        return false;
    }

    public boolean isSameMessage(Member member, String command){
        if (this.cache.containsKey(member)){
            return this.cache.get(member).contains(command);
        }else{
            this.cache.put(member, command);
        }
        return false;
    }

    public void addCommand(DiscordCommand discordCommand){
        if (!this.commands.contains(discordCommand)) {
            this.commands.add(discordCommand);
        }
    }

}

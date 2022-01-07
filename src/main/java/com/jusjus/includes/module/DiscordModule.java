package com.jusjus.includes.module;

import com.jusjus.CryptoFromNowhere;
import com.jusjus.CryptoFromNowhereImpl;
import com.jusjus.includes.command.CommandManager;
import com.jusjus.includes.command.object.DiscordCommand;
import com.jusjus.includes.module.object.PerTickUpdater;
import java.util.function.Consumer;
import lombok.Getter;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Getter
public abstract class DiscordModule<T> {

    private final T plugin;

    public DiscordModule(T plugin){
        this.plugin = plugin;

        if (plugin instanceof CryptoFromNowhereImpl){
            ((CryptoFromNowhereImpl) plugin).getModules().add(
                (DiscordModule<CryptoFromNowhereImpl>) this);
        }
    }

    public abstract void init(T plugin);

    public abstract ListenerAdapter[] registerEventListeners();

    public abstract DiscordCommand[] registerCommands(CommandManager commandManager);

    public abstract void thread(Consumer<PerTickUpdater> perTickUpdater);

}

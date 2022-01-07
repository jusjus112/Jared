package com.jusjus.assistant;

import com.jusjus.CryptoFromNowhereImpl;
import com.jusjus.assistant.impl.events.MessageReceiveEvent;
import com.jusjus.includes.command.CommandManager;
import com.jusjus.includes.command.object.DiscordCommand;
import com.jusjus.includes.module.DiscordModule;
import com.jusjus.includes.module.object.PerTickUpdater;
import java.util.function.Consumer;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AssistantController extends DiscordModule<CryptoFromNowhereImpl> {

  public AssistantController(CryptoFromNowhereImpl impl){
    super(impl);
  }

  @Override
  public void init(CryptoFromNowhereImpl plugin) {

  }

  @Override
  public ListenerAdapter[] registerEventListeners() {
    return new ListenerAdapter[]{
        new MessageReceiveEvent()
    };
  }

  @Override
  public DiscordCommand[] registerCommands(CommandManager commandManager) {
    return new DiscordCommand[0];
  }

  @Override
  public void thread(Consumer<PerTickUpdater> perTickUpdater) {

  }
}

package com.jusjus.includes.components;

import com.jusjus.CryptoFromNowhereImpl;
import com.jusjus.includes.command.CommandManager;
import com.jusjus.includes.command.object.DiscordCommand;
import com.jusjus.includes.components.button.ButtonController;
import com.jusjus.includes.module.DiscordModule;
import com.jusjus.includes.module.object.PerTickUpdater;
import java.util.function.Consumer;
import lombok.Getter;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Getter
public class ComponentsController extends DiscordModule<CryptoFromNowhereImpl> {

  private static ComponentsController instance;

  private final ButtonController buttonController;

  public static ComponentsController get(){
    return instance;
  }

  public ComponentsController(CryptoFromNowhereImpl impl){
    super(impl);
    instance = this;

    this.buttonController = new ButtonController(impl);
  }

  @Override
  public void init(CryptoFromNowhereImpl plugin) {

  }

  @Override
  public ListenerAdapter[] registerEventListeners() {
    return new ListenerAdapter[0];
  }

  @Override
  public DiscordCommand[] registerCommands(CommandManager commandManager) {
    return new DiscordCommand[0];
  }

  @Override
  public void thread(Consumer<PerTickUpdater> perTickUpdater) {

  }
}

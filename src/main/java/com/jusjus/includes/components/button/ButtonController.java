package com.jusjus.includes.components.button;

import com.google.common.collect.Lists;
import com.jusjus.CryptoFromNowhereImpl;
import com.jusjus.includes.command.CommandManager;
import com.jusjus.includes.command.object.DiscordCommand;
import com.jusjus.includes.components.button.events.ButtonClickListener;
import com.jusjus.includes.components.button.object.DiscordButton;
import com.jusjus.includes.module.DiscordModule;
import com.jusjus.includes.module.object.PerTickUpdater;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Getter
public class ButtonController extends DiscordModule<CryptoFromNowhereImpl> {

  private final List<DiscordButton> buttons;

  public ButtonController(CryptoFromNowhereImpl impl){
    super(impl);

    this.buttons = Lists.newArrayList();
  }

  @Override
  public void init(CryptoFromNowhereImpl plugin) {

  }

  @Override
  public ListenerAdapter[] registerEventListeners() {
    return new ListenerAdapter[]{
        new ButtonClickListener(this)
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

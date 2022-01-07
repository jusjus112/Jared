package com.jusjus.includes.components.button.events;

import com.jusjus.includes.components.button.ButtonController;
import com.jusjus.includes.components.message.MessageBuilder;
import java.awt.Color;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ButtonClickListener extends ListenerAdapter {

  private final ButtonController controller;

  @Override
  public void onButtonClick(@NotNull ButtonClickEvent event) {
    try{
      controller.getButtons().stream()
          .filter(discordButton -> {
            System.out.println(discordButton.getId() + " = " + event.getComponentId());
            return discordButton.getId().equals(event.getComponentId());
          })
          .forEachOrdered(discordButton -> {
            discordButton.getClickEvent().accept(event);
          });
    }catch (Exception exception){
      new MessageBuilder("Something went wrong.",
          "Whoops, I catched an error when "+event.getMember().getAsMention()+" pressed this button.")
          .addField("Error message:", exception.getMessage(), true)
          .addField("@ line:", Arrays.toString(
              Arrays.stream(exception.getStackTrace()).limit(5).toArray()), false)
          .overwriteColor(Color.RED)
          .reply(event.getMessage());
    }
  }

}

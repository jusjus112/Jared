package com.jusjus.includes.components.button.object;

import com.jusjus.includes.components.ComponentsController;
import com.jusjus.utilities.UtilString;
import java.util.function.Consumer;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

@Getter
public class DiscordButton{

  private String id;
  private Consumer<ButtonClickEvent> clickEvent;
  private String text;

  public DiscordButton(String text){
    this.text = text;
    this.id = UtilString.getAlphaNumericString(5);
    ComponentsController.get().getButtonController().getButtons().add(this);
  }

  public DiscordButton(String text, String id){
    this.text = text;
    this.id = id;
    ComponentsController.get().getButtonController().getButtons().add(this);
  }

  public DiscordButtonBuilder primary(){
    return new DiscordButtonBuilder(ButtonStyle.PRIMARY, this);
  }

  public DiscordButtonBuilder success(){
    return new DiscordButtonBuilder(ButtonStyle.SUCCESS, this);
  }

  public DiscordButtonBuilder secondary(){
    return new DiscordButtonBuilder(ButtonStyle.SECONDARY, this);
  }

  public DiscordButtonBuilder danger(){
    return new DiscordButtonBuilder(ButtonStyle.DANGER, this);
  }

  public DiscordButtonBuilder link(String url){
    return new DiscordButtonBuilder(ButtonStyle.DANGER, this);
  }

  public class DiscordButtonBuilder{

    private final ButtonStyle buttonStyle;
    private final DiscordButton discordButton;
    private String url;
    private Emoji emoji;

    public DiscordButtonBuilder(ButtonStyle buttonStyle, DiscordButton discordButton, String url){
      this.buttonStyle = buttonStyle;
      this.discordButton = discordButton;
      this.url = url;
      this.emoji = null;
    }

    public DiscordButtonBuilder(ButtonStyle buttonStyle, DiscordButton discordButton){
      this.buttonStyle = buttonStyle;
      this.discordButton = discordButton;
      this.url = null;
      this.emoji = null;
    }

    public DiscordButtonBuilder emoji(Emoji emoji){
      this.emoji = emoji;
      return this;
    }

    public DiscordButtonBuilder clickEvent(Consumer<ButtonClickEvent> clickEvent){
      DiscordButton.this.clickEvent = clickEvent;
      return this;
    }

    public Button toButton(){
      if (this.buttonStyle == ButtonStyle.LINK) {
        if (this.emoji != null){
          return Button.link(this.url, this.emoji);
        }
        return Button.link(this.url, discordButton.getText());
      }
      if (this.emoji != null){
        return Button.of(this.buttonStyle, discordButton.getId(), this.emoji);
      }
      return Button.of(this.buttonStyle, discordButton.getId(), discordButton.getText());
    }
  }

}

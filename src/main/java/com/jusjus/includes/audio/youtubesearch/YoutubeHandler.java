package com.jusjus.includes.audio.youtubesearch;

import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.jusjus.CryptoFromNowhereImpl;
import com.jusjus.includes.audio.AudioController;
import com.jusjus.includes.command.CommandManager;
import com.jusjus.includes.command.object.DiscordCommand;
import com.jusjus.includes.components.button.object.DiscordButton;
import com.jusjus.includes.module.DiscordModule;
import com.jusjus.includes.module.object.PerTickUpdater;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

public class YoutubeHandler extends DiscordModule<CryptoFromNowhereImpl> {

  private final AudioController audioController;

  public YoutubeHandler(CryptoFromNowhereImpl impl, AudioController audioController){
    super(impl);

    this.audioController = audioController;
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

  private SearchListResponse searchQuery(String search) throws GeneralSecurityException, IOException {
    YouTube youTube = new YouTube.Builder(
        com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport(),
        GsonFactory.getDefaultInstance(), request -> {
    }).setApplicationName("CFN")
        .setGoogleClientRequestInitializer(YouTubeRequestInitializer
            .newBuilder()
            .setKey("AIzaSyCY28J9XzYAw5dcTemsZJXHPh1FJN3rDL0")
            .build())
        .build();

    return youTube.search().list(Collections.singletonList("snippet"))
        .setQ(search)
        .setType(Collections.singletonList("video"))
        .setKey("AIzaSyCY28J9XzYAw5dcTemsZJXHPh1FJN3rDL0")
        .execute();
  }

  public void searchAndPlay(String search, Message message)
      throws GeneralSecurityException, IOException {

    SearchListResponse query = this.searchQuery(search);
    this.audioController.load(message, "https://www.youtube.com/watch?v=" + query.getItems().get(0).getId().getVideoId());
  }

//  public void search(String search, Message message){
//    try{
//
//      List<SearchResult> results = response.getItems();
//      List<Button> buttons = new ArrayList<>();
//      AtomicInteger buttonPlace = new AtomicInteger(0);
//
//      results.forEach(searchResult -> {
//        System.out.println(searchResult.getId().getVideoId());
//        buttons.add(new DiscordButton(buttonPlace + ". " + searchResult.getSnippet().getTitle().substring(0, 15))
//            .danger()
//            .clickEvent(buttonClickEvent -> {
//
//            })
//            .toButton()
//        );
//      });
//
//      StringBuilder builder = new StringBuilder("Found the following videos: \n");
//      AtomicInteger place = new AtomicInteger(1);
//
//      results.forEach(searchResult -> {
//        builder.append("\n").append(place).append(". ")
//            .append(searchResult.getSnippet().getTitle());
//      });
//
//      message.reply(builder.toString()).setActionRow(buttons).queue();
//    }catch (Exception exception){
//      exception.printStackTrace();
//    }
//  }
}

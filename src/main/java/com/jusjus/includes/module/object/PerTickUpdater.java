package com.jusjus.includes.module.object;

import java.util.TimerTask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PerTickUpdater {

  public final PerTickUpdater runsPer;

//  TimerTask timerTask, long delay, long period
//
//  new Timer().scheduleAtFixedRate(timerTask, delay, period);

  public enum tickRate{
    SECOND,
    MINUTE,
    HOURLY,
    DAILY,
    WEEKLY;
  }

}

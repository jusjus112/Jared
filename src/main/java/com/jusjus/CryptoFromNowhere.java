package com.jusjus;

import java.util.Map;
import lombok.Getter;

@Getter
public class CryptoFromNowhere {

  public static void main(String[] args) {
    try {
      new CryptoFromNowhereImpl().initialize();
    } catch (Exception e) {
      System.out.println("Failed to initialize implementation for JDA.");
      e.printStackTrace();
    }
  }


}

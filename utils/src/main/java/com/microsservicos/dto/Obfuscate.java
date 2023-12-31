package com.microsservicos.dto;

final class Obfuscator {
  public static String obfuscate(String txt){
    if (txt.length() <= 5) {
      return txt;
    }

    String start = txt.substring(0, 3);
    String middle = txt.substring(3, txt.length() - 2);
    String end = txt.substring(txt.length() - 2);

    StringBuilder obfuscated = new StringBuilder();
    obfuscated.append(start);
    obfuscated.append(middle.replaceAll("[a-zA-Z0-9]", "*"));
    obfuscated.append(end);

    return obfuscated.toString();
  }
}

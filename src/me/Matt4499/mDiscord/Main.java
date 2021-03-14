package me.Matt4499.mDiscord;

import javax.security.auth.login.LoginException;
import java.awt.Color;

import org.bukkit.plugin.java.JavaPlugin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class Main extends JavaPlugin {

  public static JDA bot;
  public static TextChannel SurvivalChannel;
  public static TextChannel StaffChannel;
  public static String version = "2.0";

  @Override
  public void onEnable() {
    try {
      bot = JDABuilder.createDefault("insert token here").build();
      getServer().getConsoleSender().sendMessage("[mNetwork] [Discord] Version " + version);
      getServer().getConsoleSender().sendMessage("[mNetwork] [Discord] Logging in...");
    } catch (LoginException e) {
      e.printStackTrace();
    }
    bot.addEventListener(new EverythingInOne());
    getServer().getPluginManager().registerEvents(new EverythingInOne(), this);
  }

  @Override
  public void onDisable() {
    getServer().getConsoleSender().sendMessage("[mNetwork] [Discord] Shutting down...");
    Main.SurvivalChannel.sendMessage(":octagonal_sign: Survival is now offline! It should be online again soon!")
        .queue();
    bot.shutdown();
  }

  public static void embedSend(String text, String uuid, Color color) {
      EmbedBuilder eb = new EmbedBuilder();
      eb.setAuthor(text, null, "https://crafatar.com/avatars/" + uuid);
      eb.setColor(color);
      SurvivalChannel.sendMessage(eb.build()).queue();
  }

  public static void staffChatEmbed(String text, String uuid, Color color) {
    EmbedBuilder seb = new EmbedBuilder();
    seb.setAuthor(text, null, "https://crafatar.com/avatars/" + uuid);
    seb.setColor(color);
    StaffChannel.sendMessage(seb.build()).queue();
}
}

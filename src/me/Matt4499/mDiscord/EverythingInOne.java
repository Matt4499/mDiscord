package me.Matt4499.mDiscord;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.github.matt4499.mNetwork.events.MaintenanceModeEvent;
import com.github.matt4499.mNetwork.events.StaffChatEvent;
import com.github.matt4499.mNetwork.utils.Utils;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.Color;

public class EverythingInOne extends ListenerAdapter implements Listener {

  // Discord Stuff

  @Override
  public void onReady(ReadyEvent event) {
    Bukkit.getServer().getConsoleSender().sendMessage("[mNetwork] [Discord] Ready!");
    Main.StaffChannel = Main.bot.getTextChannelById("insert staff channel/logs channel id here");
    Main.SurvivalChannel = Main.bot.getTextChannelById("insert your main chat/relay channel id here");
    if (Main.SurvivalChannel == null) {
      Bukkit.getServer().getConsoleSender().sendMessage("[mNetwork] [Discord] Relay Channel not found!...");
    } else {
      Bukkit.getServer().getConsoleSender().sendMessage("[mNetwork] [Discord] Relay Channel found!");
      Main.SurvivalChannel.sendMessage(":white_check_mark: Server name is now online!").queue();
    }
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    if (!event.getAuthor().isBot()) {
      if (Utils.filter(event.getMessage().getContentStripped())) {
        return;
      }
      if(event.getChannel().getIdLong() == Main.SurvivalChannel.getIdLong()) {
        String user = event.getAuthor().getName();
        String message = event.getMessage().getContentRaw();
        Bukkit.broadcastMessage(ChatColor.BLUE + "[Discord] " + ChatColor.GRAY + user + " > " + message);
      }
    }
  }

  // MC Stuff

  @EventHandler
  public void onLogin(PlayerLoginEvent event) {
    if (event.getPlayer().isBanned()
        || (Bukkit.hasWhitelist() && Bukkit.getWhitelistedPlayers().contains(event.getPlayer()))) {
      return;
    }
    String joinmsg = "{player} has joined.";
    String finaljoinmsg = joinmsg.replace("{player}", event.getPlayer().getName());
    Main.embedSend(finaljoinmsg, event.getPlayer().getUniqueId().toString(), Color.GREEN);
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    String joinmsg = "{player} has left.";
    String finalquitmsg = joinmsg.replace("{player}", event.getPlayer().getName());
    Main.embedSend(finalquitmsg, event.getPlayer().getUniqueId().toString(), Color.RED);
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onChat(AsyncPlayerChatEvent event) {
    if (event.isCancelled() || Utils.filter(event.getMessage())) {
      return;
    }
    String text = event.getMessage();
    String player = ChatColor.stripColor(event.getPlayer().getDisplayName());
    String chatline = player + " > " + text;
    Main.SurvivalChannel.sendMessage(chatline).queue();
  }

  @EventHandler
  public void onStaffChat(StaffChatEvent event) {
    String text = event.getMessage();
    String player = ChatColor.stripColor(event.getPlayer().getDisplayName());
    String chatline = "SC(" + player + ") " + text;
    Main.staffChatEmbed(chatline, event.getPlayer().getUniqueId().toString(), Color.RED);
  }

  @EventHandler
  public void onMaintenanceMode(MaintenanceModeEvent event) {
    String player = ChatColor.stripColor(event.getPlayer().getDisplayName());
    Boolean enabled = event.getWhitelist();
    String onoroff = enabled ? "on" : "off";
    String chatline = "Staff-only was turned " + onoroff + " by " + player;
    Main.SurvivalChannel.sendMessage(chatline).queue();
  }

  @EventHandler
  public void onDeath(EntityDeathEvent event) {
    if (event.getEntity() instanceof Player) {
      if (event.getEntity().getKiller() instanceof Player) {
        String dead = event.getEntity().getName();
        String killer = event.getEntity().getKiller().getName();
        String line = dead + " was killed by " + killer;
        Main.SurvivalChannel.sendMessage(line).queue();
      } else {
        String cause;
        switch (event.getEntity().getLastDamageCause().getCause()) {
        case BLOCK_EXPLOSION:
          cause = "exploded.";
          break;
        case FALL:
          cause = "falling too far and smashed their legs.";
          break;
        case FIRE:
          cause = "burnt to death";
          break;
        case LAVA:
          cause = "burnt to death in lava";
          break;
        case WITHER:
          cause = "withered away.";
          break;
        case FALLING_BLOCK:
          cause = "smashed by a falling block";
          break;
        case FIRE_TICK:
          cause = "burnt to death";
          break;
        case LIGHTNING:
          cause = "struck by lightning.";
          break;
        case DROWNING:
          cause = "drowned to death.";
          break;
        case ENTITY_ATTACK:
          cause = "killed by " + event.getEntity().getKiller().getName();
          break;
        default:
          cause = "killed by an unknown reason. (logged for Matt to add)";
          Bukkit.getConsoleSender().sendMessage(
              "ERROR: unknown death cause: " + event.getEntity().getLastDamageCause().getCause().toString());
          break;
        }
        String dead = event.getEntity().getName();
        String line = dead + " was " + cause;
        Main.embedSend(line, event.getEntity().getUniqueId().toString(), Color.RED);
      }
    }
  }

  // Misc Stuff
  public void log(String text) {
    Bukkit.getConsoleSender().sendMessage(text);
  }

}

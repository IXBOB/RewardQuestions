package com.ixbob.rewardquestions.command;

import com.ixbob.rewardquestions.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class QuesreloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof ConsoleCommandSender console) {
            Main.getInstance().reloadConfig();
            console.sendMessage(Component.text("§a已重新加载插件"));
            return true;
        }
        else if (commandSender instanceof Player player) {
            if (player.hasPermission("RewardQuestions.quesreload")) {
                Main.getInstance().reloadConfig();
                player.sendMessage(Component.text("§a已重新加载插件"));
                return true;
            }
            return false;
        }
        return false;
    }
}

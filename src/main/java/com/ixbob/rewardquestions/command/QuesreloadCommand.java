package com.ixbob.rewardquestions.command;

import com.ixbob.rewardquestions.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class QuesreloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (player.isOp()) {
                Main.getInstance().reloadConfig();
                player.sendMessage("§a已重新加载插件");
            }
        } else if (commandSender instanceof ConsoleCommandSender) {
            Main.getInstance().reloadConfig();
        }
        return true;
    }
}

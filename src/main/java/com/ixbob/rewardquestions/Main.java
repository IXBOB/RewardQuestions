package com.ixbob.rewardquestions;

import com.ixbob.rewardquestions.command.QuesreloadCommand;
import com.ixbob.rewardquestions.event.PlayerChatListener;
import com.ixbob.rewardquestions.task.RandomQuestionTimerRunnable;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    private static Main instance;
    private RandomQuestionTimerRunnable questionTimer;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        saveDefaultConfig();
        reloadConfig();

        questionTimer = new RandomQuestionTimerRunnable();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, questionTimer, getConfig().getInt("first_question_delay"), getConfig().getInt("per_question_time"));

        Objects.requireNonNull(this.getCommand("quesreload")).setExecutor(new QuesreloadCommand());

        registerEvents(
                new PlayerChatListener()
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance() {
        return instance;
    }

    public RandomQuestionTimerRunnable getQuestionTimer() {
        return questionTimer;
    }

    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }
}

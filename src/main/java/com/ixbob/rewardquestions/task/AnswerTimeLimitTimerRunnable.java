package com.ixbob.rewardquestions.task;

import com.ixbob.rewardquestions.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AnswerTimeLimitTimerRunnable implements Runnable{

    private int taskId;
    private int timeLeftTick = 600;
    public boolean expired = false;
    private ArrayList<Player> answeredPlayers = new ArrayList<>();

    @Override
    public void run() {
        timeLeftTick--;
        if (timeLeftTick <= 0) {
            expired = true;
            int answerCorrectPlayerAmount = Main.getInstance().getQuestionTimer().getAnswerRunnable().getAnsweredPlayers().size();
            Bukkit.broadcast(Component.text("§a回答限时已到！共有 §e" + answerCorrectPlayerAmount + " §a人回答正确"));
            cancelTask();
        }
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(taskId);
        expired = true;
    }

    public ArrayList<Player> getAnsweredPlayers() {
        return answeredPlayers;
    }
}

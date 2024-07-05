package com.ixbob.rewardquestions.task;

import com.ixbob.rewardquestions.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class RandomQuestionTimerRunnable implements Runnable {

    private FileConfiguration config;
    private ArrayList<ArrayList<String>> quesMainList;
    private ArrayList<Integer> askedQuesIndexList = new ArrayList<>();
    private Integer askingQuesIndex;
    private ArrayList<String> askingQues;

    private AnswerTimeLimitTimerRunnable answerRunnable;

    @Override
    public void run() {
        this.config = Main.getInstance().getConfig();
        quesMainList = (ArrayList<ArrayList<String>>) config.getList("questions");
        if (quesMainList == null) {
            throw new RuntimeException("Questions cannot be null!");
        }

        //开始新一轮
        if (askedQuesIndexList.size() == quesMainList.size()) {
            askedQuesIndexList.clear();
        }

        int randomIndex;
        //下一轮
        while (true) {
            randomIndex = (int) (Math.random() * quesMainList.size());
            if (!askedQuesIndexList.contains(randomIndex)) {
                askedQuesIndexList.add(randomIndex);
                askingQuesIndex = randomIndex;
                askingQues = quesMainList.get(randomIndex);
                break;
            }
        }

        if (askingQuesIndex == null || askingQues == null) {
            throw new NullPointerException("Questions cannot be null!");
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage("§6有奖问答：§f" + askingQues.get(0) + "  §a在聊天栏中输入你的答案，你有 30秒 时间来回答");
            onlinePlayer.showTitle(Title.title(
                    Component.text("§e有奖问答 " + askingQuesIndex), //title
                    Component.text(askingQues.get(0)), //subtitle
                    Title.Times.times(Ticks.duration(10L), Ticks.duration(160L), Ticks.duration(10L))));
        }

        answerRunnable = new AnswerTimeLimitTimerRunnable();
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), answerRunnable, 0, 1);
        answerRunnable.setTaskId(taskId);

    }

    public AnswerTimeLimitTimerRunnable getAnswerRunnable() {
        return answerRunnable;
    }

    public ArrayList<String> getAskingQues() {
        return askingQues;
    }
}

package com.ixbob.rewardquestions.event;

import com.ixbob.rewardquestions.Main;
import com.ixbob.rewardquestions.task.AnswerTimeLimitTimerRunnable;
import com.ixbob.rewardquestions.task.RandomQuestionTimerRunnable;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class PlayerChatListener implements Listener {

    @SuppressWarnings("unchecked")
    ArrayList<ArrayList<Object>> mustRewardList = (ArrayList<ArrayList<Object>>) Main.getInstance().getConfig().get("must_rewards");
    @SuppressWarnings("unchecked")
    ArrayList<ArrayList<Object>> probRewardList = (ArrayList<ArrayList<Object>>) Main.getInstance().getConfig().get("probability_rewards");


    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        if (Main.getInstance().getQuestionTimer().getAnswerRunnable() == null) {
            return;
        }
        if (!Main.getInstance().getQuestionTimer().getAnswerRunnable().expired) {
            RandomQuestionTimerRunnable QuestionTimer = Main.getInstance().getQuestionTimer();
            AnswerTimeLimitTimerRunnable answerRunnable = QuestionTimer.getAnswerRunnable();

            ArrayList<String> askingQues = QuestionTimer.getAskingQues();

            String correctAns = askingQues.get(1);

            String legacyChatStr = PlainTextComponentSerializer.plainText().serialize(event.message());

            if (correctAns.equals(legacyChatStr)) {
                Player player = event.getPlayer();

                if (answerRunnable.getAnsweredPlayers().contains(player)) {
                    player.sendMessage("§e你已经答对了！");
                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(Component.text("§a你答对了！"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                Bukkit.broadcast(Component.text("§e" + player.getName() + "§a答对了"));

                answerRunnable.getAnsweredPlayers().add(player);

                giveMustRewards(player);
                giveProbRewards(player);

                event.setCancelled(true);
            }
        }
    }

    private void giveMustRewards(Player player) {
        for (ArrayList<Object> rewardObj : mustRewardList) {
            giveRewards(player, rewardObj);
        }
    }

    private void giveProbRewards(Player player) {
        for (ArrayList<Object> rewardObj : probRewardList) {
            double chance = (double) rewardObj.get(1);

            if (new Random().nextDouble() > chance) {
                continue;
            }

            @SuppressWarnings("unchecked")
            ArrayList<Object> innerList = (ArrayList<Object>) rewardObj.get(0);

            giveRewards(player, innerList);
        }
    }

    private String getSubString(String origin){
        return origin.substring(1, origin.length() - 1);
    }

    private void runCommandByConsole(String command) {
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }

    private void giveRewards(Player player, ArrayList<Object> rewardList) {
        int amount = -1;
        String index0_rewardType = (String) rewardList.get(0);
        String index1_amountType = (String) rewardList.get(1);
        int index2 = (int) rewardList.get(2);
        int index3 = rewardList.size() >= 4 ? (int) rewardList.get(3) : -1;

        if (index1_amountType.startsWith("%") && index1_amountType.endsWith("%")) {
            String subString = getSubString(index1_amountType);
            if (subString.equals("amount")) {
                amount = index2;
            } else if (subString.equals("random_amount")) {
                amount = (int) (Math.random() * (index3 - index2 + 1)) + index2;
            }
        }

        if (index0_rewardType.startsWith("%") && index0_rewardType.endsWith("%")) {
            String subString = getSubString(index0_rewardType);
            if (subString.equals("vault_money")) {
                runCommandByConsole("money give " + player.getName() + " " + amount);
            }
        } else {
            Material material = Material.valueOf(index0_rewardType.toUpperCase());
            ItemStack itemStack = new ItemStack(material, amount);
            player.getInventory().addItem(itemStack);
        }
    }
}

package net.hempflingclub.immortality.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.hempflingclub.immortality.Immortality;
import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public final class ImmortalityCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal(Immortality.MOD_ID + ":immortalityInfo").requires((source) -> source.hasPermissionLevel(0)).executes((context -> {
            if (context.getSource().isExecutedByPlayer()) {
                PlayerEntity playerEntity = context.getSource().getPlayer();
                context.getSource().getServer().execute(() -> {
                    assert playerEntity != null;
                    if (ImmortalityStatus.getBonusHearts(playerEntity) != 0) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.bonusHearts", ImmortalityStatus.getBonusHearts(playerEntity)), true);
                    }
                    if (ImmortalityStatus.getNegativeHearts(playerEntity) != 0) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.negativeHearts", ImmortalityStatus.getNegativeHearts(playerEntity)), true);
                    }
                    if (ImmortalityStatus.getRegeneratingHearts(playerEntity) != 0) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.regeneratingHearts", ImmortalityStatus.getRegeneratingHearts(playerEntity)), true);
                    }
                    if (ImmortalityStatus.getBonusArmor(playerEntity) != 0) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.bonusArmor", ImmortalityStatus.getBonusArmor(playerEntity)), true);
                    }
                    if (ImmortalityStatus.getBonusArmorT(playerEntity) != 0) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.bonusArmorT", ImmortalityStatus.getBonusArmorT(playerEntity)), true);
                    }
                });
            } else {
                context.getSource().sendFeedback(Text.translatable("immortality.commands.playerOnly"), false);
            }
            return 1;
        })));
    }
}

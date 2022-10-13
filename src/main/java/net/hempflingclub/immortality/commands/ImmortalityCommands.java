package net.hempflingclub.immortality.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.hempflingclub.immortality.Immortality;
import net.hempflingclub.immortality.util.IPlayerDataSaver;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public final class ImmortalityCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal(Immortality.MOD_ID + ":stats").requires((source) -> source.hasPermissionLevel(0)).executes((context -> {
            if (context.getSource().isExecutedByPlayer()) {
                PlayerEntity playerEntity = context.getSource().getPlayer();
                context.getSource().getServer().execute(() -> {
                    assert playerEntity != null;
                    if (ImmortalityStatus.getBonusHearts(playerEntity) != 0) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.bonusHearts", ImmortalityStatus.getBonusHearts(playerEntity)), false);
                    }
                    if (ImmortalityStatus.getNegativeHearts(playerEntity) != 0) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.negativeHearts", ImmortalityStatus.getNegativeHearts(playerEntity)), false);
                    }
                    if (ImmortalityStatus.getRegeneratingHearts(playerEntity) != 0) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.regeneratingHearts", ImmortalityStatus.getRegeneratingHearts(playerEntity)), false);
                    }
                    if (ImmortalityStatus.getBonusArmor(playerEntity) != 0) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.bonusArmor", ImmortalityStatus.getBonusArmor(playerEntity)), false);
                    }
                    if (ImmortalityStatus.getBonusArmorT(playerEntity) != 0) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.bonusArmorT", ImmortalityStatus.getBonusArmorT(playerEntity)), false);
                    }
                    if (ImmortalityData.getVoidHeart((IPlayerDataSaver) playerEntity)) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.void_heart"), false);
                    }
                    if (ImmortalityData.getLiverImmortality((IPlayerDataSaver) playerEntity)) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.false_immortality"), false);
                    } else if (ImmortalityData.getImmortality((IPlayerDataSaver) playerEntity) && ImmortalityData.getVoidHeart((IPlayerDataSaver) playerEntity) && ImmortalityData.getLiverOnceExtracted((IPlayerDataSaver) playerEntity)) {
                        if (ImmortalityData.getImmortalDeaths((IPlayerDataSaver) playerEntity) >= 30) {
                            context.getSource().sendFeedback(Text.translatable("immortality.commands.trinity"), false);
                        } else {
                            context.getSource().sendFeedback(Text.translatable("immortality.commands.trinity_unfulfilled", (30 - ImmortalityData.getImmortalDeaths((IPlayerDataSaver) playerEntity))), false);
                        }
                    } else if (ImmortalityData.getImmortality((IPlayerDataSaver) playerEntity)) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.immortality"), false);
                    }
                    if (ImmortalityData.getImmortalDeaths((IPlayerDataSaver) playerEntity) > 0) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.prevented_deaths", ImmortalityData.getImmortalDeaths((IPlayerDataSaver) playerEntity)), false);
                    }
                });
            } else {
                context.getSource().sendFeedback(Text.translatable("immortality.commands.playerOnly"), false);
            }
            return 1;
        })));
    }
}

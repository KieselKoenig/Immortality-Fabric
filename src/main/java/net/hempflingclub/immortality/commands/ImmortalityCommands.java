package net.hempflingclub.immortality.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.hempflingclub.immortality.Immortality;
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
                    if (ImmortalityStatus.getLifeElixirHealth(playerEntity) != 0) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.lifeElixirHearts", ImmortalityStatus.getLifeElixirHealth(playerEntity)), false);
                    }
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
                    if (ImmortalityStatus.getVoidHeart(playerEntity)) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.void_heart"), false);
                    }
                    if (ImmortalityStatus.isSemiImmortal(playerEntity)) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.semi_immortality"), false);
                    }
                    if (ImmortalityStatus.getLiverImmortality(playerEntity)) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.false_immortality"), false);
                    } else if (ImmortalityStatus.getImmortality(playerEntity) && ImmortalityStatus.getVoidHeart(playerEntity)) {
                        if (ImmortalityStatus.isTrueImmortal(playerEntity)) {
                            context.getSource().sendFeedback(Text.translatable("immortality.commands.trinity"), false);
                        } else if (!ImmortalityStatus.hasTrueImmortalDeaths(playerEntity)) {
                            context.getSource().sendFeedback(Text.translatable("immortality.commands.immortality"), false);
                            context.getSource().sendFeedback(Text.translatable("immortality.commands.trinity_unfulfilled", ImmortalityStatus.getMissingDeathsToTrueImmortality(playerEntity)), false);
                        } else if (!ImmortalityStatus.canEatLiverOfImmortality(playerEntity)) {
                            context.getSource().sendFeedback(Text.translatable("immortality.commands.immortality"), false);
                            if (ImmortalityStatus.getMissingLiversToEatLiverOfImmortality(playerEntity) > 0) {
                                context.getSource().sendFeedback(Text.translatable("immortality.commands.trinity_unfulfilled_extraction", ImmortalityStatus.getMissingLiversToEatLiverOfImmortality(playerEntity)), false);
                            } else {
                                context.getSource().sendFeedback(Text.translatable("immortality.commands.trinity_unfulfilled_holy_dagger"), false);
                            }
                        }
                    } else if (ImmortalityStatus.getImmortality(playerEntity)) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.immortality"), false);
                        if (ImmortalityStatus.canEatLiverOfImmortality(playerEntity)) {
                            context.getSource().sendFeedback(Text.translatable("immortality.commands.canEatLiver"), false);
                        } else {
                            context.getSource().sendFeedback(Text.translatable("immortality.commands.neededExtractionLivers", ImmortalityStatus.getMissingLiversToEatLiverOfImmortality(playerEntity)), false);
                        }
                    } else {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.not_immortal"), false);
                    }
                    if (ImmortalityData.getImmortalDeaths(ImmortalityStatus.getPlayerComponent(playerEntity)) > 0) {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.prevented_deaths", ImmortalityData.getImmortalDeaths(ImmortalityStatus.getPlayerComponent(playerEntity))), false);
                    }
                });
            } else {
                context.getSource().sendFeedback(Text.translatable("immortality.commands.playerOnly"), false);
            }
            return 1;
        })));
    }
}

package net.hempflingclub.immortality.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.hempflingclub.immortality.Immortality;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

import java.util.Objects;

public final class ImmortalityCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal(Immortality.MOD_ID).requires((source) -> source.hasPermissionLevel(0))
                .executes((context -> {
                    if (context.getSource().isExecutedByPlayer()) {
                        PlayerEntity playerEntity = (context.getSource().getPlayer());
                        context.getSource().getServer().execute(() -> {
                            assert playerEntity != null;
                            playerEntity.sendMessage(Text.translatable("immortality.commands.use_auto_completed_commands"), false);
                        });
                    } else {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.playerOnly"), false);
                    }
                    return 1;
                }))
                .then(CommandManager.literal("stats").executes((context -> {
                    if (context.getSource().isExecutedByPlayer()) {
                        PlayerEntity playerEntity = context.getSource().getPlayer();
                        context.getSource().getServer().execute(() -> {
                            assert playerEntity != null;
                            if (ImmortalityStatus.hasTargetGiftedImmortal(playerEntity)) {
                                if (ImmortalityStatus.getTargetGiftedImmortalLivingEntity(playerEntity) != null) {
                                    LivingEntity soulBondEntity = ImmortalityStatus.getTargetGiftedImmortalLivingEntity(playerEntity);
                                    assert soulBondEntity != null;
                                    context.getSource().sendFeedback(Text.translatable("immortality.status.soulBond", Objects.requireNonNull(soulBondEntity.getCustomName()).getString(), Double.toString(Math.floor(soulBondEntity.getX())), Double.toString(Math.floor(soulBondEntity.getY())), Double.toString(Math.floor(soulBondEntity.getZ())), soulBondEntity.getWorld().getDimensionKey().getValue().toString()), false);

                                } else {
                                    context.getSource().sendFeedback(Text.translatable("immortality.status.soulBond_dead"), false);

                                }
                            }
                            if (ImmortalityStatus.getLifeElixirHealth(playerEntity) != 0) {
                                context.getSource().sendFeedback(Text.translatable("immortality.commands.lifeElixirHearts", ImmortalityStatus.getLifeElixirHealth(playerEntity)), false);
                            }
                            if (ImmortalityStatus.getBonusHearts(playerEntity) != 0) {
                                context.getSource().sendFeedback(Text.translatable("immortality.commands.bonusHearts", ImmortalityStatus.getBonusHearts(playerEntity)), false);
                            }
                            if (ImmortalityStatus.getNegativeHearts(playerEntity) != 0) {
                                if (ImmortalityStatus.isSemiImmortal(playerEntity)) {
                                    context.getSource().sendFeedback(Text.translatable("immortality.commands.negativeHearts_Semi", ImmortalityStatus.getNegativeHearts(playerEntity)), false);
                                } else {
                                    context.getSource().sendFeedback(Text.translatable("immortality.commands.negativeHearts", ImmortalityStatus.getNegativeHearts(playerEntity)), false);
                                }
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
                                context.getSource().sendFeedback(Text.translatable("immortality.commands.needed_successful_lifeElixir", ((20 - ImmortalityStatus.getBonusHearts(playerEntity)) / ImmortalityStatus.lifeElixirHealth)), false);
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
                })))
                .then(CommandManager.literal("summon").executes((context -> {
                    if (context.getSource().isExecutedByPlayer()) {
                        PlayerEntity playerEntity = context.getSource().getPlayer();
                        context.getSource().getServer().execute(() -> {
                            if (ImmortalityStatus.hasTargetGiftedImmortal(playerEntity)) {
                                assert playerEntity != null;
                                playerEntity.sendMessage(Text.translatable("immortality.commands.summoned"), true);
                                LivingEntity summonedEntity = ImmortalityStatus.getTargetGiftedImmortalLivingEntity(playerEntity);
                                assert summonedEntity != null;
                                FabricDimensions.teleport(summonedEntity, (ServerWorld) Objects.requireNonNull(playerEntity).getWorld(), new TeleportTarget(playerEntity.getPos(), Vec3d.ZERO, summonedEntity.getHeadYaw(), summonedEntity.getPitch()));
                                ((ServerWorld) summonedEntity.getWorld()).spawnParticles(ParticleTypes.SOUL, summonedEntity.getX(), summonedEntity.getY(), summonedEntity.getZ(), 64, 0, 5, 0, 1);
                            } else {
                                context.getSource().sendFeedback(Text.translatable("immortality.commands.no_soulbound"), false);
                            }
                        });
                    } else {
                        context.getSource().sendFeedback(Text.translatable("immortality.commands.playerOnly"), false);
                    }
                    return 1;
                })))
        );
    }
}

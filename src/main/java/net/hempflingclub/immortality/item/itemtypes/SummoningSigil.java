package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.entitys.ImmortalEntitys;
import net.hempflingclub.immortality.entitys.ImmortalWither.ImmortalWither;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class SummoningSigil extends Item {
    private static final int MAX_USE_TIME = 96;

    public SummoningSigil(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = (PlayerEntity) user;
        if (playerEntity.getMainHandStack() == stack) {
            super.finishUsing(stack, world, playerEntity);
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!world.isClient()) {
                Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) playerEntity, stack);
                ImmortalWither immortalWither = new ImmortalWither(ImmortalEntitys.ImmortalWither, world);
                immortalWither.setPosition(new Vec3d(user.getX(), user.getY() + 3, user.getZ()));
                immortalWither.setInvulTimer(220);
                immortalWither.setPersistent();
                world.spawnEntity(immortalWither);
                for (PlayerEntity player : Objects.requireNonNull(world.getServer()).getPlayerManager().getPlayerList()) {
                    player.playSound(SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.HOSTILE, 5, 1);
                }
                stack.damage(1, playerEntity, e -> e.sendToolBreakStatus(playerEntity.getActiveHand()));
            }
            if (stack.getDamage() >= stack.getMaxDamage()) {
                stack.decrement(1);
            }
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return MAX_USE_TIME;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("immortality.tooltip.item.summoning_sigil_1").formatted(Formatting.RED));
    }
}

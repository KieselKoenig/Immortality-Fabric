package net.hempflingclub.immortality.event;

import net.hempflingclub.immortality.Immortality;
import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.util.IImmortalityWorldComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class PlayerOnKillEntity {
    public static void initialize() {
        //Initialize Events
        final String targetMobKey = "entity.minecraft.ender_dragon";
        PlayerKillEntityCallback.EVENT.register((playerEntity, killedEntity) -> {
            //Code run when Player Kills Entity
            if (killedEntity.getType().getTranslationKey().equals(targetMobKey)) {
                IImmortalityWorldComponent levelcomponent = IImmortalityWorldComponent.KEY.get(playerEntity.getWorld());
                Identifier[] recipes = new Identifier[2];
                recipes[0] = new Identifier(Immortality.MOD_ID, "void_heart");
                recipes[1] = new Identifier(Immortality.MOD_ID, "holy_dagger");
                playerEntity.unlockRecipes(recipes);
                levelcomponent.setDragonKills(levelcomponent.getDragonKills() + 1);
                if (levelcomponent.getDragonKills() == 1 || levelcomponent.getDragonKills() % 5 == 0) {
                    for (PlayerEntity player : Objects.requireNonNull(playerEntity.getServer()).getPlayerManager().getPlayerList()) {
                        player.sendMessage(Text.translatable("immortality.heart_dropped"));
                    }
                    killedEntity.getWorld().spawnEntity(new ItemEntity(killedEntity.getWorld(), killedEntity.getX(), killedEntity.getY(), killedEntity.getZ(), new ItemStack(ImmortalityItems.HeartOfImmortality)));
                }
            }
        });
    }
}
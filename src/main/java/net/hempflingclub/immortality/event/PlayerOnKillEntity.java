package net.hempflingclub.immortality.event;

import net.hempflingclub.immortality.Immortality;
import net.hempflingclub.immortality.util.IImmortalityWorldComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

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
                for (ServerPlayerEntity player : playerEntity.getWorld().getPlayers()) {
                    player.sendMessage(Text.literal("Mob killed by " +
                            playerEntity.getName().getString() +
                            " Total Kills: " +
                            levelcomponent.getDragonKills()));
                }
            } else {
                for (ServerPlayerEntity player : playerEntity.getWorld().getPlayers()) {
                    player.sendMessage(Text.literal("Mob killed by " + playerEntity.getName().getString() + " But not target Mob"));
                }
            }
        });
    }
}
package net.hempflingclub.immortality;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.hempflingclub.immortality.commands.ImmortalityCommands;
import net.hempflingclub.immortality.event.PlayerOnKillEntity;
import net.hempflingclub.immortality.event.PlayerTickHandler;
import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.statuseffect.ModEffectRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Immortality implements ModInitializer {
    public static final String MOD_ID = "immortality";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ImmortalityItems.registerModItems();
        ServerTickEvents.START_SERVER_TICK.register(new PlayerTickHandler());
        ModEffectRegistry.registerAll();
        PlayerOnKillEntity.initialize();
        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated, e) -> ImmortalityCommands.register(dispatcher)));
    }
}

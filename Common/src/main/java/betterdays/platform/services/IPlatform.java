package betterdays.platform.services;

import net.minecraft.world.level.Level;

import betterdays.wrappers.ServerPlayerWrapper;

public interface IPlatform {

    int getSeasonDay(Level level);

    void tickEffects(ServerPlayerWrapper playerWrapper);

}

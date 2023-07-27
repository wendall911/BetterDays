package betterdays.platform;

import betterdays.message.BetterDaysMessages;
import betterdays.wrappers.ServerLevelWrapper;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import betterdays.platform.services.IPlatform;

public class FabricPlatform implements IPlatform {

    @Override
    public ResourceLocation getResourceLocation(Item item) {
        return Registry.ITEM.getKey(item);
    }

    @Override
    public boolean isModLoaded(String name) {
        return FabricLoader.getInstance().isModLoaded(name);
    }

    @Override
    public boolean isPhysicalClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public void onSleepFinished(ServerLevelWrapper levelWrapper, long time) {
        BetterDaysMessages.onSleepFinishedEvent(levelWrapper.get());
    }

}

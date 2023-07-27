package betterdays.platform.services;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import betterdays.wrappers.ServerLevelWrapper;

public interface IPlatform {

    ResourceLocation getResourceLocation(Item item);

    boolean isModLoaded(String name);

    boolean isPhysicalClient();

    void onSleepFinished(ServerLevelWrapper levelWrapper, long time);

}

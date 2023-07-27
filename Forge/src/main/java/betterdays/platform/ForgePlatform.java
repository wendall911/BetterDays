package betterdays.platform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;

import betterdays.platform.services.IPlatform;
import betterdays.wrappers.ServerLevelWrapper;

public class ForgePlatform implements IPlatform {

    @Override
    public ResourceLocation getResourceLocation(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }

    @Override
    public boolean isModLoaded(String name) {
        return ModList.get().isLoaded(name);
    }

    @Override
    public boolean isPhysicalClient() {
        return FMLLoader.getDist() == Dist.CLIENT;
    }

    @Override
    public void onSleepFinished(ServerLevelWrapper levelWrapper, long time) {
        ForgeEventFactory.onSleepFinished(levelWrapper.get(), time, time);
    }

}

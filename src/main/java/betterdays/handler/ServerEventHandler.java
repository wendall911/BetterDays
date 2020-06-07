package betterdays.handler;

import betterdays.BetterDays;
import betterdays.util.PacketHandlerHelper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BetterDays.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER)
public class ServerEventHandler {

    @SubscribeEvent
    public void onPlayerLogin(final PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof PlayerEntity) {
            PacketHandlerHelper.sendServerConfigValues((ServerPlayerEntity)event.getPlayer());
        }
    }

}


package betterdays.proxy;

import net.minecraftforge.common.MinecraftForge;

import betterdays.event.ServerEventHandler;

public class ServerProxy {

    public ServerProxy() {

        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());

    }

}

package betterdays.proxy;

import betterdays.handler.ServerEventHandler;

import net.minecraftforge.common.MinecraftForge;

public class ServerProxy {

    public ServerProxy() {

        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());

    }

}

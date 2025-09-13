package betterdays.platform;

import betterdays.BetterDays;
import betterdays.platform.services.IClientPlatform;
import betterdays.platform.services.IPlatform;
import betterdays.platform.services.IRegistryFactory;

import technology.roughness.whitenoise.platform.ServicesBase;

public class Services extends ServicesBase {

    public static final IClientPlatform CLIENT_PLATFORM = load(BetterDays.LOGGER, IClientPlatform.class);
    public static final IPlatform PLATFORM = load(BetterDays.LOGGER, IPlatform.class);
    public static final IRegistryFactory REGISTRY_FACTORY = load(BetterDays.LOGGER, IRegistryFactory.class);

}

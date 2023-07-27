package betterdays.platform;

import java.util.ServiceLoader;

import betterdays.BetterDays;
import betterdays.platform.services.IClientPlatform;
import betterdays.platform.services.IPlatform;
import betterdays.platform.services.IRegistryFactory;

public class Services {

    public static final IClientPlatform CLIENT_PLATFORM = load(IClientPlatform.class);
    public static final IPlatform PLATFORM = load(IPlatform.class);
    public static final IRegistryFactory REGISTRY_FACTORY = load(IRegistryFactory.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
            .findFirst()
            .orElseThrow(
                () -> new NullPointerException("Failed to load service for " + clazz.getName()));
        BetterDays.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);

        return loadedService;
    }

}

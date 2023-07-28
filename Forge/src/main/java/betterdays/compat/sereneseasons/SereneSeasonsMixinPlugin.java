package betterdays.compat.sereneseasons;

import betterdays.compat.AbstractMixinPlugin;

import net.minecraftforge.fml.loading.FMLLoader;

public class SereneSeasonsMixinPlugin extends AbstractMixinPlugin {

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (FMLLoader.getLoadingModList().getModFileById("sereneseasonsfix") != null) return false;

        return FMLLoader.getLoadingModList().getModFileById("sereneseasons") != null;
    }

}

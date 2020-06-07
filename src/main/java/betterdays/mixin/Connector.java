package betterdays.mixin;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

import betterdays.BetterDays;

public class Connector implements IMixinConnector {

    @Override
    public void connect() {
        BetterDays.logger.info("Invoking Mixin Connector");
        Mixins.addConfiguration("assets/betterdays/betterdays.mixins.json");
    }

}

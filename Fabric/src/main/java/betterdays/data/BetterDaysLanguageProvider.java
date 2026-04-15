package betterdays.data;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import net.minecraft.core.HolderLookup;

import betterdays.BetterDays;
import betterdays.common.Translations;

public class BetterDaysLanguageProvider extends FabricLanguageProvider {

    protected BetterDaysLanguageProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryFuture) {
        super(dataOutput, "en_us", registryFuture);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder builder) {
        addTranslationTitle(builder, "Better Days");
        addTranslationName(builder, "gui", "GUI"); // Section
        addTranslationDescription(builder, "gui");
        addTranslationName(builder, "clockalignment", "Clock Alignment");
        addTranslationDescription(builder, "clockalignment");
        addTranslationName(builder, "clockscale", "Clock Scale");
        addTranslationDescription(builder, "clockscale");
        addTranslationName(builder, "clockmargin", "Clock Margin");
        addTranslationDescription(builder, "clockmargin");
        addTranslationName(builder, "preventclockwobble", "Prevent Clock Wobble");
        addTranslationDescription(builder, "preventclockwobble");
        addTranslationName(builder, "blacklistdimensions", "Blacklist Dimensions");
        addTranslationDescription(builder, "blacklistdimensions");
        addTranslationName(builder, "time", "Time Settings"); // Section
        addTranslationDescription(builder, "time");
        addTranslationName(builder, "speedmethod", "Speed Method");
        addTranslationDescription(builder, "speedmethod");
        addTranslationName(builder, "dayspeed", "Day Speed");
        addTranslationDescription(builder, "dayspeed");
        addTranslationName(builder, "nightspeed", "Night Speed");
        addTranslationDescription(builder, "nightspeed");
        addTranslationName(builder, "dayspeedminutes", "Day Speed (Minutes)");
        addTranslationDescription(builder, "dayspeedminutes");
        addTranslationName(builder, "nightspeedminutes", "Night Speed (Minutes)");
        addTranslationDescription(builder, "nightspeedminutes");
        addTranslationName(builder, "seasondayminutes", "Season Day Total Minutes");
        addTranslationDescription(builder, "seasondayminutes");
        addTranslationName(builder, "seasonlatitude", "Season Latitude");
        addTranslationDescription(builder, "seasonlatitude");
        addTranslationName(builder, "daystart", "Day Start Time Tick");
        addTranslationDescription(builder, "daystart");
        addTranslationName(builder, "nightstart", "Night Start Time Tick");
        addTranslationDescription(builder, "nightstart");
        addTranslationName(builder, "enableinterpolatedtime", "Enable Interpolated Time");
        addTranslationDescription(builder, "enableinterpolatedtime");
        addTranslationName(builder, "interpolatedtimesmoothingfactor", "Interpolated Time Smoothing Factor");
        addTranslationDescription(builder, "interpolatedtimesmoothingfactor");
        addTranslationName(builder, "interpolatedtimelist", "Interpolated Time Dimension List");
        addTranslationDescription(builder, "interpolatedtimelist");
        addTranslationName(builder, "effects", "Effect Settings"); // Subsection
        addTranslationDescription(builder, "effects");
        addTranslationName(builder, "weathereffect", "Weather Effect");
        addTranslationDescription(builder, "weathereffect");
        addTranslationName(builder, "potioneffect", "Potion Effect");
        addTranslationDescription(builder, "potioneffect");
        addTranslationName(builder, "hungereffect", "Hunger Effect");
        addTranslationDescription(builder, "hungereffect");
        addTranslationName(builder, "blockentityeffect", "Block Entity Effect");
        addTranslationDescription(builder, "blockentityeffect");
        addTranslationName(builder, "sleep", "Sleep Settings"); // Section
        addTranslationDescription(builder, "sleep");
        addTranslationName(builder, "enablesleepfeature", "Enable Sleep Feature");
        addTranslationDescription(builder, "enablesleepfeature");
        addTranslationName(builder, "sleepspeedmax", "Sleep Speed Max");
        addTranslationDescription(builder, "sleepspeedmax");
        addTranslationName(builder, "sleepspeedmin", "Sleep Speed Min");
        addTranslationDescription(builder, "sleepspeedmin");
        addTranslationName(builder, "sleepspeedall", "Sleep Speed All");
        addTranslationDescription(builder, "sleepspeedall");
        addTranslationName(builder, "sleepspeedcurve", "Sleep Speed Curve");
        addTranslationDescription(builder, "sleepspeedcurve");
        addTranslationName(builder, "displaybedclock", "Display Bed Clock");
        addTranslationDescription(builder, "displaybedclock");
        addTranslationName(builder, "ratioplayersforsleep", "Ratio of Players required for Sleep");
        addTranslationDescription(builder, "ratioplayersforsleep");
        addTranslationName(builder, "messages", "Message Settings"); // Section
        addTranslationDescription(builder, "messages");
        addTranslationName(builder, "morning", "Morning Message Settings"); // Subsection
        addTranslationDescription(builder, "morning");
        addTranslationName(builder, "message", "Message");
        addTranslationDescription(builder, "message", "gui.message");
        addTranslationName(builder, "type", "Message Type");
        addTranslationDescription(builder, "type");
        addTranslationName(builder, "target", "Message Target");
        addTranslationDescription(builder, "target", "gui.target");
        addTranslationName(builder, "enterbed", "Enter Bed Message Settings"); // Subsection
        addTranslationDescription(builder, "enterbed");
        addTranslationName(builder, "leavebed", "Leave Bed Message Settings"); // Subsection
        addTranslationDescription(builder, "leavebed");
    }

    private void addTranslationTitle(TranslationBuilder builder, String title) {
        builder.add(BetterDays.MODID + ".configuration.title", title);
    }

    private void addTranslationName(TranslationBuilder builder, String id, String name) {
        builder.add(BetterDays.MODID + ".configuration." + id + ".name", name);
    }

    private void addTranslationDescription(TranslationBuilder builder, String id) {
        builder.add(BetterDays.MODID + ".configuration." + id + ".description", Translations.get(id));
    }

    private void addTranslationDescription(TranslationBuilder builder, String id, String key) {
        builder.add(BetterDays.MODID + ".configuration." + id + ".description", Translations.get(key));
    }

}

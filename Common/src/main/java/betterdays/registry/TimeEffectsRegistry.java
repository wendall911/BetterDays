/*
 * Copyright (C) 2021 Nick Iacullo
 * https://github.com/DuckyCrayfish/hourglass
 * Derived from hourglass
 *
 * This file is part of Hourglass.
 *
 * Better Days is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Better Days is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Better Days.  If not, see <https://www.gnu.org/licenses/>.
 */

package betterdays.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import betterdays.BetterDays;
import betterdays.time.effects.BlockEntityTimeEffect;
import betterdays.time.effects.HungerTimeEffect;
import betterdays.time.effects.PotionTimeEffect;
import betterdays.time.effects.RandomTickSleepEffect;
import betterdays.time.effects.TimeEffect;
import betterdays.time.effects.WeatherSleepEffect;

/**
 * This class registers all of the first-party time effects that come with Better Days.
 */
public class TimeEffectsRegistry {

    /** The resource key for the {@link #TIME_EFFECT_REGISTRY} registry. */
    public static final ResourceKey<Registry<TimeEffect>> KEY = ResourceKey.createRegistryKey(new ResourceLocation(BetterDays.MODID, "time_effect"));

    /** Registry for time effects. See {@link TimeEffect} for details on time effects. */
    public static final RegistryProvider<TimeEffect> TIME_EFFECT_REGISTRY = RegistryProvider.get(KEY, BetterDays.MODID, true);

    public static final RegistryObject<TimeEffect> WEATHER_EFFECT;
    public static final RegistryObject<TimeEffect> RANDOM_TICK_EFFECT;
    public static final RegistryObject<TimeEffect> POTION_EFFECT;
    public static final RegistryObject<TimeEffect> HUNGER_EFFECT;
    public static final RegistryObject<TimeEffect> BLOCK_ENTITY_EFFECT;

    static {
        WEATHER_EFFECT = TIME_EFFECT_REGISTRY.register("weather", WeatherSleepEffect::new);
        RANDOM_TICK_EFFECT = TIME_EFFECT_REGISTRY.register("random_tick", RandomTickSleepEffect::new);
        POTION_EFFECT = TIME_EFFECT_REGISTRY.register("potion", PotionTimeEffect::new);
        HUNGER_EFFECT = TIME_EFFECT_REGISTRY.register("hunger", HungerTimeEffect::new);
        BLOCK_ENTITY_EFFECT = TIME_EFFECT_REGISTRY.register("block_entity", BlockEntityTimeEffect::new);
    }

    public static void init() {}

}

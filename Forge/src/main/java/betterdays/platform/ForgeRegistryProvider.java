/*
 * Copyright (C) 2017-2022 Illusive Soulworks
 * Derived from comforts mod:
 * https://github.com/illusivesoulworks/comforts
 *
 * Better Days is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Better Days is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Better Days.  If not, see <https://www.gnu.org/licenses/>.
 */

package betterdays.platform;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import betterdays.BetterDays;
import betterdays.registry.TimeEffectsRegistry;
import betterdays.time.effects.BlockEntityTimeEffect;
import betterdays.time.effects.BlockEntityTimeEffectForge;
import betterdays.time.effects.HungerTimeEffect;
import betterdays.time.effects.HungerTimeEffectForge;
import betterdays.time.effects.PotionTimeEffect;
import betterdays.time.effects.PotionTimeEffectForge;
import betterdays.time.effects.RandomTickSleepEffect;
import betterdays.time.effects.RandomTickSleepEffectForge;
import betterdays.time.effects.TimeEffect;
import betterdays.time.effects.WeatherSleepEffect;
import betterdays.time.effects.WeatherSleepEffectForge;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import betterdays.registry.RegistryObject;
import betterdays.registry.RegistryProvider;
import betterdays.platform.services.IRegistryFactory;

public class ForgeRegistryProvider implements IRegistryFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> RegistryProvider<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId, boolean makeRegistry) {
        final var containerOpt = ModList.get().getModContainerById(modId);

        if (containerOpt.isEmpty()) {
            throw new NullPointerException("Cannot find mod container for id " + modId);
        }

        final var cont = containerOpt.get();

        if (cont instanceof FMLModContainer fmlModContainer) {
            DeferredRegister<T> register = DeferredRegister.create(resourceKey, modId);

            if (makeRegistry) {
                createRegistry((ResourceKey<? extends Registry<TimeEffect>>) resourceKey, TimeEffect.class, (DeferredRegister<TimeEffect>) register);
            }

            register.register(fmlModContainer.getEventBus());

            return new Provider<>(modId, register, makeRegistry);
        } else {
            throw new ClassCastException("The container of the mod " + modId + " is not a FML one!");
        }
    }

    private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> createRegistry(ResourceKey<? extends Registry<TimeEffect>> resourceKey, Class<TimeEffect> type, DeferredRegister<TimeEffect> register) {
        RegistryBuilder<T> registryBuilder = new RegistryBuilder<T>().setName(TimeEffectsRegistry.KEY.location()).setType(c(TimeEffect.class)).setMaxID(Integer.MAX_VALUE - 1);

        register.makeRegistry(c(type), () -> registryBuilder);

        return registryBuilder;
    }

    @SuppressWarnings("unchecked")
    static <T> Class<T> c(Class<?> cls) {
        return (Class<T>) cls;
    }

    private static class Provider<T> implements RegistryProvider<T> {

        private final String modId;
        private final DeferredRegister<T> registry;
        private final boolean makeRegistry;

        private final Set<RegistryObject<T>> entries = new HashSet<>();
        private final Set<RegistryObject<T>> entriesView = Collections.unmodifiableSet(entries);

        private Provider(String modId, DeferredRegister<T> registry, boolean makeRegistry) {
            this.modId = modId;
            this.registry = registry;
            this.makeRegistry = makeRegistry;
        }

        @Override
        public String getModId() {
            return modId;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier) {
            Supplier<?> forgeSupplier = null;
            ResourceLocation resourceLocation = new ResourceLocation(BetterDays.MODID, name);

            /*
             * This is a product of backporting. Needed to wrap the effects with IForgeRegistryEntry. I'm sure there is
             * a better way of doing this. This is a quick n' dirty to maintain the signature of 1.19+ in case we do
             * future backporting of bug fixes, etc.
             */
            switch (name) {
                case "weather" -> forgeSupplier = () -> new WeatherSleepEffectForge(resourceLocation, WeatherSleepEffect::new);
                case "random_tick" -> forgeSupplier = () -> new RandomTickSleepEffectForge(resourceLocation, RandomTickSleepEffect::new);
                case "potion" -> forgeSupplier = () -> new PotionTimeEffectForge(resourceLocation, PotionTimeEffect::new);
                case "hunger" -> forgeSupplier = () -> new HungerTimeEffectForge(resourceLocation, HungerTimeEffect::new);
                case "block_entity" -> forgeSupplier = () -> new BlockEntityTimeEffectForge(resourceLocation, BlockEntityTimeEffect::new);
            }
            final var obj = registry.<I>register(name, (Supplier<? extends I>) forgeSupplier);
            final var ro = new RegistryObject<I>() {

                @Override
                public ResourceKey<I> getResourceKey() {
                    return obj.getKey();
                }

                @Override
                public ResourceLocation getId() {
                    return obj.getId();
                }

                @Override
                public I get() {
                    return obj.get();
                }

                @Override
                public Holder<I> asHolder() {
                    return obj.getHolder().orElseThrow();
                }

            };
            entries.add((RegistryObject<T>) ro);

            return ro;
        }

        @Override
        public Set<RegistryObject<T>> getEntries() {
            return entriesView;
        }

    }

}

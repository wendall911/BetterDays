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

import net.neoforged.fml.ModList;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import betterdays.registry.RegistryObject;
import betterdays.registry.RegistryProvider;
import betterdays.platform.services.IRegistryFactory;

public class NeoForgeRegistryProvider implements IRegistryFactory {

    @Override
    public <T> RegistryProvider<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId, boolean makeRegistry) {
        final var containerOpt = ModList.get().getModContainerById(modId);

        if (containerOpt.isEmpty()) {
            throw new NullPointerException("Cannot find mod container for id " + modId);
        }

        final var cont = containerOpt.get();

        if (cont instanceof FMLModContainer fmlModContainer) {
            final var register = DeferredRegister.create(resourceKey, modId);

            if (makeRegistry) {
                register.makeRegistry((registryBuilder) -> {new RegistryBuilder(resourceKey);});
            }

            register.register(fmlModContainer.getEventBus());

            return new Provider<>(modId, register, makeRegistry);
        } else {
            throw new ClassCastException("The container of the mod " + modId + " is not a FML one!");
        }
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
            final var obj = registry.<I>register(name, supplier);
            final var ro = new RegistryObject<I>() {

                @Override
                public ResourceKey<I> getResourceKey() {
                    return (ResourceKey<I>) obj.getKey();
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
                    return (Holder<I>) obj;
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

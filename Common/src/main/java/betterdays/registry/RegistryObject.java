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

package betterdays.registry;

import java.util.function.Supplier;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface RegistryObject<T> extends Supplier<T> {

    ResourceKey<T> getResourceKey();

    ResourceLocation getId();

    @Override
    T get();

    Holder<T> asHolder();

}

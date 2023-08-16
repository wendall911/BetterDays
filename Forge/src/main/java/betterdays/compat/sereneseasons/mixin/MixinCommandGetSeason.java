/*
 * Derived from sereeneseasonfix mod
 * https://github.com/123oro321/sereneseasonsfix
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Or_OS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package betterdays.compat.sereneseasons.mixin;

import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import sereneseasons.command.CommandGetSeason;
import sereneseasons.config.ServerConfig;

import betterdays.config.ConfigHandler;

@Mixin(CommandGetSeason.class)
public abstract class MixinCommandGetSeason {

    @Inject(method = "getSeason", at= @At("HEAD"), remap = false, cancellable = true)
    private static void betterdays$getSeason(CommandSourceStack cs, Level level, CallbackInfoReturnable<Integer> cir) throws CommandRuntimeException {
        if (ConfigHandler.Common.sereneSeasonsFix() && !ServerConfig.isDimensionWhitelisted(level.dimension())) {
            cs.sendFailure(new TranslatableComponent("commands.betterdays.getseason.notwhitelisted"));
            cir.setReturnValue(1);
        }
    }
}

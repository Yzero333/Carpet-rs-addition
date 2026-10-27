/*
 * This file is part of the carpet-rs-addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  zero333
 *
 * carpet-rs-addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * carpet-rs-addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with carpet-rs-addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpetrs.addition.mixin;

import carpetrs.addition.settings.CarpetRsSettings;
import carpetrs.addition.util.GlassFamily;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemAxeInstantGlassMixin {

    private static final float RS_INSTANT_MINING_SPEED = 6.0F;

    @Inject(method = "getMiningSpeed", at = @At("HEAD"), cancellable = true)
    private void rsAxeInstantGlass(ItemStack stack, BlockState state,
                                   CallbackInfoReturnable<Float> cir) {
        if (!CarpetRsSettings.rsAxeInstantGlass) {
            return;
        }
        if (stack == null) {
            return;
        }

        if (!stack.isIn(ItemTags.AXES)) {
            return;
        }
        if (!GlassFamily.isGlass(state)) {
            return;
        }
        cir.setReturnValue(RS_INSTANT_MINING_SPEED);
    }
}

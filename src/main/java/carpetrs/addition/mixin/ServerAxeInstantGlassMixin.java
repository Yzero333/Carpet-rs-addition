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
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerAxeInstantGlassMixin {

    @Inject(method = "onPlayerAction", at = @At("HEAD"))
    private void rsBreakGlassOnStart(PlayerActionC2SPacket packet, CallbackInfo ci) {
        if (!CarpetRsSettings.rsAxeInstantGlass) {
            return;
        }
        if (packet.getAction() != PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
            return;
        }
        ServerPlayerEntity player = ((ServerPlayNetworkHandler) (Object) this).player;
        if (player == null) {
            return;
        }
        BlockPos pos = packet.getPos();
        ItemStack held = player.getMainHandStack();

        if (held == null || !held.isIn(ItemTags.AXES)) {
            return;
        }
        BlockState state = player.getEntityWorld().getBlockState(pos);
        if (!GlassFamily.isGlass(state)) {
            return;
        }

        if (!player.canInteractWithBlockAt(pos, 1.0D)) {
            return;
        }
        if (!player.getEntityWorld().canEntityModifyAt(player, pos)) {
            return;
        }
        if (player.isBlockBreakingRestricted(player.getEntityWorld(), pos,
                player.interactionManager.getGameMode())) {
            return;
        }
        player.interactionManager.tryBreakBlock(pos);
    }
}

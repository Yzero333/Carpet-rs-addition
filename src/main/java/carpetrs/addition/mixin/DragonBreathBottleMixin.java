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
import carpetrs.addition.util.DragonAim;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(GlassBottleItem.class)
public abstract class DragonBreathBottleMixin {

    private static final double HEAD_BOX_TOLERANCE = 0.5D;

    private static final double HEAD_PART_TRUST_DISTANCE = 16.0D;

    private static final double HEAD_FALLBACK_RADIUS = 1.2D;

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void rsDragonBreath(World world, PlayerEntity user, Hand hand,
                                CallbackInfoReturnable<ActionResult> cir) {
        if (!CarpetRsSettings.rsNoAiDragonBreath || world.isClient()) {
            return;
        }
        ItemStack held = user.getStackInHand(hand);

        if (held == null || held.getItem() != Items.GLASS_BOTTLE) {
            return;
        }
        EnderDragonEntity dragon = findTargetDragon(world, user);
        if (dragon == null) {
            return;
        }

        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        world.emitGameEvent(user, GameEvent.FLUID_PICKUP, user.getEntityPos());
        if (user instanceof ServerPlayerEntity serverPlayer) {
            Criteria.PLAYER_INTERACTED_WITH_ENTITY.trigger(serverPlayer, held, dragon);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(Items.GLASS_BOTTLE));

        cir.setReturnValue(ActionResult.SUCCESS.withNewHandStack(
                ItemUsage.exchangeStack(held, user, new ItemStack(Items.DRAGON_BREATH))));
    }

    private static EnderDragonEntity findTargetDragon(World world, PlayerEntity user) {
        Vec3d eye = user.getEyePos();
        Vec3d look = user.getRotationVec(1.0F);
        double reach = user.getEntityInteractionRange() + 2.0D;
        Vec3d probe = eye.add(look.multiply(reach));

        Box search = user.getBoundingBox().expand(reach + 12.0D);

        for (Entity candidate : world.getOtherEntities(user, search, e -> e instanceof EnderDragonEntity)) {
            EnderDragonEntity dragon = (EnderDragonEntity) candidate;
            if (!dragon.isAlive()) {
                continue;
            }
            boolean decorative = isDecorative(dragon);
            boolean onHead = decorative && aimsAtHead(dragon, eye, probe, reach);
            if (onHead) {
                return dragon;
            }
        }
        return null;
    }

    private static boolean isDecorative(EnderDragonEntity dragon) {
        if (dragon.isAiDisabled()) {
            return true;
        }
        if (dragon.getFight() == null) {
            return true;
        }
        return dragon.getPhaseManager().getCurrent().getType() == PhaseType.HOVER;
    }

    private static boolean aimsAtHead(EnderDragonEntity dragon, Vec3d eye, Vec3d probe, double reach) {
        Box head = headBox(dragon);
        if (head.contains(eye)) {
            return true;
        }
        Optional<Vec3d> hit = head.raycast(eye, probe);
        if (hit.isEmpty()) {
            return false;
        }
        return eye.squaredDistanceTo(hit.get()) <= reach * reach;
    }

    private static Box headBox(EnderDragonEntity dragon) {
        Box partBox = dragon.head.getBoundingBox();
        Vec3d dragonCenter = new Vec3d(dragon.getX(), dragon.getY(), dragon.getZ());
        if (partBox.getCenter().squaredDistanceTo(dragonCenter)
                <= HEAD_PART_TRUST_DISTANCE * HEAD_PART_TRUST_DISTANCE) {
            return partBox.expand(HEAD_BOX_TOLERANCE);
        }
        Vec3d eyePos = dragon.getEyePos();
        double centerX = DragonAim.headX(dragon.getYaw(), eyePos.x);
        double centerY = eyePos.y;
        double centerZ = DragonAim.headZ(dragon.getYaw(), eyePos.z);
        return new Box(
                centerX - HEAD_FALLBACK_RADIUS, centerY - HEAD_FALLBACK_RADIUS, centerZ - HEAD_FALLBACK_RADIUS,
                centerX + HEAD_FALLBACK_RADIUS, centerY + HEAD_FALLBACK_RADIUS, centerZ + HEAD_FALLBACK_RADIUS);
    }

}

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

package carpetrs.addition.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class GlassFamily {

    private static volatile Set<Identifier> glassIds;

    private static final Set<Class<?>> GLASS_CLASSES = ConcurrentHashMap.newKeySet();
    private static final Set<Class<?>> NON_GLASS_CLASSES = ConcurrentHashMap.newKeySet();

    private GlassFamily() {
    }

    public static boolean isGlass(BlockState state) {
        if (state == null) {
            return false;
        }
        return isGlassBlock(state.getBlock());
    }

    public static boolean isGlassBlock(Block block) {
        if (block == null) {
            return false;
        }
        Identifier id = Registries.BLOCK.getId(block);
        if (looksLikeGlassById(id) && idSet().contains(id)) {
            return true;
        }

        Class<?> clazz = block.getClass();
        if (GLASS_CLASSES.contains(clazz)) {
            return true;
        }
        if (NON_GLASS_CLASSES.contains(clazz)) {
            return false;
        }
        boolean glass = looksLikeGlassByClass(clazz);
        if (glass) {
            GLASS_CLASSES.add(clazz);
        } else {
            NON_GLASS_CLASSES.add(clazz);
        }
        return glass;
    }

    private static Set<Identifier> idSet() {
        Set<Identifier> cached = glassIds;
        if (cached != null) {
            return cached;
        }
        synchronized (GlassFamily.class) {
            if (glassIds == null) {
                Set<Identifier> built = ConcurrentHashMap.newKeySet();
                for (Identifier id : Registries.BLOCK.getIds()) {
                    if (looksLikeGlassById(id)) {
                        built.add(id);
                    }
                }
                glassIds = built;
            }
            return glassIds;
        }
    }

    private static boolean looksLikeGlassById(Identifier id) {
        return id != null && matchesGlassPath(id.getPath());
    }

    static boolean matchesGlassPath(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        return path.startsWith("glass")
                || path.endsWith("_glass")
                || path.endsWith("_glass_pane");
    }

    private static boolean looksLikeGlassByClass(Class<?> blockClass) {
        for (Class<?> c = blockClass; c != null && c != Block.class; c = c.getSuperclass()) {
            if (c.getSimpleName().contains("Glass")) {
                return true;
            }
        }
        return blockClass == Blocks.GLASS.getClass() || blockClass == Blocks.GLASS_PANE.getClass();
    }
}

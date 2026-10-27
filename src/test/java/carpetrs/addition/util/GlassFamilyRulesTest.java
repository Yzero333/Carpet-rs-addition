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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class GlassFamilyRulesTest {

    private static final String[] GLASS = {
            "glass", "glass_pane",
            "white_stained_glass", "orange_stained_glass", "magenta_stained_glass",
            "light_blue_stained_glass", "yellow_stained_glass", "lime_stained_glass",
            "pink_stained_glass", "gray_stained_glass", "light_gray_stained_glass",
            "cyan_stained_glass", "purple_stained_glass", "blue_stained_glass",
            "brown_stained_glass", "green_stained_glass", "red_stained_glass",
            "black_stained_glass",
            "white_stained_glass_pane", "orange_stained_glass_pane", "magenta_stained_glass_pane",
            "light_blue_stained_glass_pane", "yellow_stained_glass_pane", "lime_stained_glass_pane",
            "pink_stained_glass_pane", "gray_stained_glass_pane", "light_gray_stained_glass_pane",
            "cyan_stained_glass_pane", "purple_stained_glass_pane", "blue_stained_glass_pane",
            "brown_stained_glass_pane", "green_stained_glass_pane", "red_stained_glass_pane",
            "black_stained_glass_pane",
            "tinted_glass"
    };

    private static final String[] NOT_GLASS = {
            "glowstone", "sea_lantern", "iron_bars",
            "copper_bars", "oxidized_copper_bars",
            "obsidian", "stone", "sand", "oak_planks"
    };

    public static void main(String[] args) {
        List<String> failures = new ArrayList<>();
        Map<String, Boolean> results = new LinkedHashMap<>();

        for (String path : GLASS) {
            boolean actual = GlassFamily.matchesGlassPath(path);
            results.put(path, actual);
            if (!actual) {
                failures.add("expected match: " + path);
            }
        }
        for (String path : NOT_GLASS) {
            boolean actual = GlassFamily.matchesGlassPath(path);
            results.put(path, actual);
            if (actual) {
                failures.add("expected NO match: " + path);
            }
        }

        results.forEach((path, actual) -> System.out.println(
                (actual ? "match    " : "no-match ") + path));

        if (failures.isEmpty()) {
            System.out.println("PASS  (" + GLASS.length + " glass ids matched, "
                    + NOT_GLASS.length + " non-glass ids rejected)");
            return;
        }
        System.out.println("FAIL  (" + failures.size() + " case(s))");
        failures.forEach(f -> System.out.println("  " + f));
        System.exit(1);
    }

    private GlassFamilyRulesTest() {
    }
}

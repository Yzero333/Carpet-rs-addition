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

package carpetrs.addition.settings;

import carpet.api.settings.Rule;
import carpet.api.settings.RuleCategory;

public final class CarpetRsSettings {

    public static final String RS = "RS";

    public static final String RS_AXE_INSTANT_GLASS = "rsAxeInstantGlass";

    public static final String RS_NO_AI_DRAGON_BREATH = "rsNoAiDragonBreath";

    public static final String[] RULE_NAMES = {
            RS_AXE_INSTANT_GLASS,
            RS_NO_AI_DRAGON_BREATH
    };

    private CarpetRsSettings() {
    }

    @Rule(categories = {RuleCategory.SURVIVAL, RuleCategory.FEATURE, RS})
    public static boolean rsAxeInstantGlass = false;

    @Rule(categories = {RuleCategory.SURVIVAL, RuleCategory.FEATURE, RS})
    public static boolean rsNoAiDragonBreath = false;
}

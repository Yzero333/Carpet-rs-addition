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
 *
 * carpet-rs-addition —— carpet 主 mod 的附属
 */

package carpetrs.addition;

import carpet.CarpetServer;
import net.fabricmc.api.ModInitializer;

public final class CarpetRsAddition implements ModInitializer {

    public static final String MOD_ID = "carpet-rs-addition";
    public static final String MOD_NAME = "Carpet RS Addition";
    public static final String VERSION = "0.1.1-fixed+1.21.11";

    @Override
    public void onInitialize() {
        try {
            boolean alreadyKnown = CarpetServer.extensions.stream()
                    .anyMatch(ext -> ext instanceof CarpetRsExtension);
            if (!alreadyKnown) {
                CarpetServer.manageExtension(new CarpetRsExtension());
            }
        } catch (Throwable t) {

        }
    }
}

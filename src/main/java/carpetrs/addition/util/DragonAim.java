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

public final class DragonAim {

    public static final double HEAD_OFFSET = 6.5D;

    private DragonAim() {
    }

    public static double forwardX(double yawDegrees) {
        return Math.sin(Math.toRadians(yawDegrees));
    }

    public static double forwardZ(double yawDegrees) {
        return -Math.cos(Math.toRadians(yawDegrees));
    }

    public static double headX(double yawDegrees, double baseX) {
        return baseX + forwardX(yawDegrees) * HEAD_OFFSET;
    }

    public static double headZ(double yawDegrees, double baseZ) {
        return baseZ + forwardZ(yawDegrees) * HEAD_OFFSET;
    }
}

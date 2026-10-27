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
import java.util.List;

public final class DragonAimTest {

    private static final double EPS = 1.0e-6D;

    public static void main(String[] args) {
        List<String> failures = new ArrayList<>();

        check(failures, "yaw 0   前向 x", DragonAim.forwardX(0), 0.0D);
        check(failures, "yaw 0   前向 z", DragonAim.forwardZ(0), -1.0D);
        check(failures, "yaw 90  前向 x", DragonAim.forwardX(90), 1.0D);
        check(failures, "yaw 90  前向 z", DragonAim.forwardZ(90), 0.0D);
        check(failures, "yaw 180 前向 x", DragonAim.forwardX(180), 0.0D);
        check(failures, "yaw 180 前向 z", DragonAim.forwardZ(180), 1.0D);
        check(failures, "yaw 270 前向 x", DragonAim.forwardX(270), -1.0D);
        check(failures, "yaw 270 前向 z", DragonAim.forwardZ(270), 0.0D);

        for (double yaw : new double[]{0, 37, 90, 143, 180, 270, -45}) {
            double len = Math.hypot(DragonAim.forwardX(yaw), DragonAim.forwardZ(yaw));
            check(failures, "yaw " + (int) yaw + " 前向长度", len, 1.0D);
        }

        check(failures, "yaw 0   龙头 x", DragonAim.headX(0, 100.0D), 100.0D);
        check(failures, "yaw 0   龙头 z", DragonAim.headZ(0, 100.0D), 100.0D - DragonAim.HEAD_OFFSET);
        check(failures, "yaw 90  龙头 x", DragonAim.headX(90, 100.0D), 100.0D + DragonAim.HEAD_OFFSET);
        check(failures, "yaw 90  龙头 z", DragonAim.headZ(90, 100.0D), 100.0D);
        check(failures, "yaw 180 龙头 z", DragonAim.headZ(180, 0.0D), DragonAim.HEAD_OFFSET);
        check(failures, "yaw 270 龙头 x", DragonAim.headX(270, 0.0D), -DragonAim.HEAD_OFFSET);
        check(failures, "龙头偏移量", DragonAim.HEAD_OFFSET, 6.5D);

        for (double yaw : new double[]{0, 45, 90, 135, 180, 225, 270, 315}) {
            double dx = DragonAim.headX(yaw, 0.0D);
            double dz = DragonAim.headZ(yaw, 0.0D);
            check(failures, "yaw " + (int) yaw + " 龙头水平距离",
                    Math.hypot(dx, dz), DragonAim.HEAD_OFFSET);
        }

        if (failures.isEmpty()) {
            System.out.println("PASS  (DragonAim: 8 朝向分量 + 7 单位长度 + 7 龙头坐标 + 8 龙头水平距离)");
            return;
        }
        System.out.println("FAIL  (" + failures.size() + " case(s))");
        failures.forEach(f -> System.out.println("  " + f));
        System.exit(1);
    }

    private static void check(List<String> failures, String what, double actual, double expected) {
        if (Math.abs(actual - expected) > EPS) {
            failures.add(what + ": expected " + expected + " but was " + actual);
        }
    }

    private DragonAimTest() {
    }
}

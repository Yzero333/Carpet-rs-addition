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

package carpetrs.addition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import carpetrs.addition.settings.CarpetRsSettings;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CarpetRsExtension implements CarpetExtension {

    private static final String ZH_CN = "zh_cn";

    private boolean rulesRegistered;

    @Override
    public void onServerLoaded(MinecraftServer server) {
        if (this.rulesRegistered) {
            return;
        }
        this.rulesRegistered = true;

        SettingsManager mainManager = CarpetServer.settingsManager;
        if (mainManager == null) {
            return;
        }

        try {
            carpet.utils.Translations.updateLanguage();
        } catch (Throwable ignored) {

        }

        try {
            mainManager.parseSettingsClass(CarpetRsSettings.class);
        } catch (Throwable t) {
            return;
        }

        applyPersistedValues(server, mainManager);
    }

    @Override
    public void onServerClosed(MinecraftServer server) {

    }

    private static void applyPersistedValues(MinecraftServer server, SettingsManager manager) {
        try {
            Path configFile = resolveCarpetConfig(server);
            if (configFile == null || !Files.isReadable(configFile)) {
                return;
            }
            List<String> lines = Files.readAllLines(configFile, StandardCharsets.UTF_8);
            for (String raw : lines) {
                String line = raw.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                int space = line.indexOf(' ');
                if (space <= 0) {
                    continue;
                }
                String ruleName = line.substring(0, space).trim();
                String value = line.substring(space + 1).trim();
                if (!isOurRule(ruleName)) {
                    continue;
                }
                applyToField(ruleName, value);
            }
        } catch (Throwable ignored) {

        }
    }

    private static Path resolveCarpetConfig(MinecraftServer server) {
        try {
            Path root = server.getSavePath(net.minecraft.util.WorldSavePath.ROOT);
            if (root != null) {
                Path candidate = root.resolve("carpet.conf");
                if (Files.exists(candidate)) {
                    return candidate;
                }
            }
        } catch (Throwable ignored) {

        }
        try {
            Path runDir = server.getRunDirectory();
            if (runDir != null) {
                return runDir.resolve("carpet.conf");
            }
        } catch (Throwable ignored) {

        }
        return null;
    }

    private static boolean isOurRule(String ruleName) {
        for (String name : CarpetRsSettings.RULE_NAMES) {
            if (name.equals(ruleName)) {
                return true;
            }
        }
        return false;
    }

    private static boolean applyToField(String ruleName, String value) throws Exception {
        Field field = CarpetRsSettings.class.getDeclaredField(ruleName);
        field.setAccessible(true);
        if (field.getType() == boolean.class) {
            field.setBoolean(null, Boolean.parseBoolean(value));
            return true;
        }
        if (field.getType() == int.class) {
            field.setInt(null, Integer.parseInt(value));
            return true;
        }
        if (field.getType() == String.class) {
            field.set(null, value);
            return true;
        }

        return false;
    }

    @Override
    public String version() {
        return CarpetRsAddition.VERSION;
    }

    @Override
    public Map<String, String> canHasTranslations(String language) {
        Map<String, String> translations = new LinkedHashMap<>();
        boolean chinese = ZH_CN.equalsIgnoreCase(language);

        translation(translations, "rule.rsAxeInstantGlass.name",
                chinese ? "斧头秒破玻璃" : "Axe Instantly Breaks Glass");

        translation(translations, "rule.rsAxeInstantGlass.desc",
                chinese
                        ? "使用任意斧头时,（染色）玻璃/玻璃板可秒破（含遮光玻璃）"
                        : "With any axe, (stained) glass / glass panes break instantly (tinted glass included)");

        translation(translations, "rule.rsNoAiDragonBreath.name",
                chinese ? "无AI末影龙取龙息" : "Dragon's Breath from No-AI Dragons");
        translation(translations, "rule.rsNoAiDragonBreath.desc",
                chinese
                        ? "手持空玻璃瓶右键无AI末影龙的头部即可取得龙息"
                        : "Right-click a no-AI Ender Dragon's head with an empty glass bottle to collect Dragon's Breath");

        translation(translations, "category." + CarpetRsSettings.RS, CarpetRsSettings.RS);

        return translations;
    }

    private static void translation(Map<String, String> map, String key, String value) {
        map.put("carpet." + key, value);
        map.put(key, value);
    }
}

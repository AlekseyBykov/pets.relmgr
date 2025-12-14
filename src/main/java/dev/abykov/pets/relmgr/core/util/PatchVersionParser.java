package dev.abykov.pets.relmgr.core.util;

public class PatchVersionParser {

    public static String decrementBaseVersion(String base) {
        int pos = base.lastIndexOf("T");
        if (pos < 0) {
            throw new IllegalArgumentException("Некорректный формат версии: " + base);
        }

        String prefix = base.substring(0, pos + 1);
        int num = Integer.parseInt(base.substring(pos + 1));

        if (num == 0) {
            throw new IllegalArgumentException("Версия кончилась: " + base);
        }

        int prev = num - 1;

        return prefix + (prev < 10 ? "0" + prev : prev);
    }

    public static int extractFixNumber(String tag) {
        try {
            String n = tag.substring(tag.lastIndexOf("_fix") + 4);
            return Integer.parseInt(n);
        } catch (Exception ignored) {
            return -1;
        }
    }
}

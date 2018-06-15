package com.rabihsalamey.gamecharactercreatorv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class CharacterContent {

    /**
     * An array of stat objects.
     */
    public static final List<Stat> STATS = new ArrayList<Stat>();

    /**
     * A map of stat objects, by ID.
     */
    public static final Map<String, Stat> STAT_MAP = new HashMap<String, Stat>();

    static {
        addStat(createStatItem("Warrior"));
        addStat(createStatItem("Mage"));
        addStat(createStatItem("Healer"));
        addStat(createStatItem("Hunter"));
        addStat(createStatItem("Paladin"));

    }

    private static void addStat(Stat stat) {
        STATS.add(stat);
        STAT_MAP.put(stat.id, stat);
    }

    private static Stat createStatItem(String name) {
        return new Stat(name);
    }
    /**
     * A dummy item representing a piece of content.
     */
    public static class Stat {
        public final String id;

        public Stat(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}

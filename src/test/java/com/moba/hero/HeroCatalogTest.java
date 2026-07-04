package com.moba.hero;

import com.moba.combat.BaseStats;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HeroCatalogTest {

    @Test
    void findYornByName() {
        Optional<BaseStats> stats = HeroCatalog.find(Yorn.NAME);

        assertTrue(stats.isPresent());
        assertSame(Yorn.stats(), stats.get());
    }

    @Test
    void findReturnsEmptyForUnknownName() {
        Optional<BaseStats> stats = HeroCatalog.find("NotARealHero");

        assertFalse(stats.isPresent());
    }

    @Test
    void findIsCaseSensitive() {
        assertFalse(HeroCatalog.find("yorn").isPresent());
        assertFalse(HeroCatalog.find("YORN").isPresent());
        assertTrue(HeroCatalog.find(Yorn.NAME).isPresent());
    }

    @Test
    void requireReturnsKnownHero() {
        BaseStats stats = HeroCatalog.require(Yorn.NAME);

        assertSame(Yorn.stats(), stats);
    }

    @Test
    void requireThrowsForUnknownHero() {
        assertThrows(IllegalArgumentException.class, () -> HeroCatalog.require("NotARealHero"));
    }

    @Test
    void requireRejectsNullName() {
        assertThrows(NullPointerException.class, () -> HeroCatalog.require(null));
    }

    @Test
    void findRejectsNullName() {
        assertThrows(NullPointerException.class, () -> HeroCatalog.find(null));
    }

    @Test
    void namesContainsYorn() {
        assertTrue(HeroCatalog.names().contains(Yorn.NAME));
    }

    @Test
    void namesIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class,
                () -> HeroCatalog.names().add("Foo"));
    }
}

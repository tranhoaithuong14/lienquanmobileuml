package com.moba.hero;

import com.moba.combat.Attribute;
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
        Optional<Attribute> attribute = HeroCatalog.find(Yorn.NAME);

        assertTrue(attribute.isPresent());
        assertSame(Yorn.attribute(), attribute.get());
    }

    @Test
    void findReturnsEmptyForUnknownName() {
        Optional<Attribute> attribute = HeroCatalog.find("NotARealHero");

        assertFalse(attribute.isPresent());
    }

    @Test
    void findIsCaseSensitive() {
        assertFalse(HeroCatalog.find("yorn").isPresent());
        assertFalse(HeroCatalog.find("YORN").isPresent());
        assertTrue(HeroCatalog.find(Yorn.NAME).isPresent());
    }

    @Test
    void requireReturnsKnownHero() {
        Attribute attribute = HeroCatalog.require(Yorn.NAME);

        assertSame(Yorn.attribute(), attribute);
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
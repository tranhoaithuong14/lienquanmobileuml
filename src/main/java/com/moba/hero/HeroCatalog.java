package com.moba.hero;

import com.moba.combat.BaseStats;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class HeroCatalog {

    private static final Map<String, BaseStats> TEMPLATES = Map.of(
            Yorn.NAME, Yorn.stats()
    );

    private HeroCatalog() {}

    public static Optional<BaseStats> find(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return Optional.ofNullable(TEMPLATES.get(name));
    }

    public static BaseStats require(String name) {
        return find(name).orElseThrow(() ->
                new IllegalArgumentException("unknown hero: " + name));
    }

    public static Set<String> names() {
        return TEMPLATES.keySet();
    }
}

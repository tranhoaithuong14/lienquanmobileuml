package com.moba.hero;

import com.moba.combat.Attribute;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class HeroCatalog {

    private static final Map<String, Attribute> TEMPLATES = Map.of(
            Yorn.NAME, Yorn.attribute(),
            Toro.NAME, Toro.attribute()
    );

    private HeroCatalog() {}

    public static Optional<Attribute> find(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return Optional.ofNullable(TEMPLATES.get(name));
    }

    public static Attribute require(String name) {
        return find(name).orElseThrow(() ->
                new IllegalArgumentException("unknown hero: " + name));
    }

    public static Set<String> names() {
        return TEMPLATES.keySet();
    }
}
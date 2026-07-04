package com.lqm.hero;

import com.lqm.combat.BaseStats;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Hero catalog — read-only Repository của immutable BaseStats templates (Evans, DDD Ch.6).
 *
 * Đây là in-memory registry cho tất cả hero templates trong game. Không tạo Hero instances ở đây
 * (Hero có identity + mutable state — đó là Entity, không phải Value Object, không cache được).
 *
 * Tại sao Registry thay vì Factory Method / Abstract Factory (GoF Creational):
 * - Hero khác nhau về DATA (số liệu), không khác về BEHAVIOR — Factory Method/Abstract Factory sinh
 *   đối tượng theo CLASS, mà ADR-0001 đã cấm per-hero subclassing (combinatorial explosion).
 * - Map<String, BaseStats> đúng pattern Repository của Evans: "illusion of an in-memory collection".
 * - Behavior khác biệt giữa hero (passive, ultimate) sẽ là Strategy/Specification sau (deferred), không
 *   nhồi vào đây.
 *
 * Thread-safe: BaseStats là record immutable (Bloch Item 17), Map unmodifiable → lookup shareless.
 */
public final class HeroCatalog {

    private static final Map<String, BaseStats> TEMPLATES = Map.of(
            Yorn.NAME, Yorn.stats()
    );

    private HeroCatalog() {}

    /** Lookup BaseStats template theo tên hero (case-sensitive). */
    public static Optional<BaseStats> find(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return Optional.ofNullable(TEMPLATES.get(name));
    }

    /** Lookup hoặc throw — dùng khi caller biết tên hero chắc chắn tồn tại. */
    public static BaseStats require(String name) {
        return find(name).orElseThrow(() ->
                new IllegalArgumentException("unknown hero: " + name));
    }

    /** Tất cả tên hero hiện có trong catalog. */
    public static Set<String> names() {
        return TEMPLATES.keySet();
    }
}

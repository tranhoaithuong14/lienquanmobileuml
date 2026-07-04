package com.moba.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AttributeTest {

    private static Attribute fullAttribute() {
        return Attribute.builder()
                .basic(Basic.of(100f, 0f, 0f, 0f, 0f, CombatResource.MANA, 0f))
                .offensive(Offensive.of(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f))
                .attackRange(AttackRange.MELEE)
                .role(HeroRole.WARRIOR)
                .build();
    }

    @Test
    void recordEqualityHolds() {
        Attribute a = fullAttribute();
        Attribute b = fullAttribute();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void rejectsNullAttackRange() {
        assertThrows(IllegalArgumentException.class, () -> Attribute.builder()
                .basic(Basic.of(100f, 0f, 0f, 0f, 0f, CombatResource.MANA, 0f))
                .offensive(Offensive.of(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f))
                .role(HeroRole.WARRIOR)
                .build());
    }

    @Test
    void rejectsNullRole() {
        assertThrows(IllegalArgumentException.class, () -> Attribute.builder()
                .basic(Basic.of(100f, 0f, 0f, 0f, 0f, CombatResource.MANA, 0f))
                .offensive(Offensive.of(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f))
                .attackRange(AttackRange.MELEE)
                .build());
    }

    @Test
    void rejectsNullBasic() {
        assertThrows(NullPointerException.class, () -> Attribute.builder()
                .offensive(Offensive.of(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f))
                .attackRange(AttackRange.MELEE)
                .role(HeroRole.WARRIOR)
                .build());
    }

    @Test
    void rejectsNullOffensive() {
        assertThrows(NullPointerException.class, () -> Attribute.builder()
                .basic(Basic.of(100f, 0f, 0f, 0f, 0f, CombatResource.MANA, 0f))
                .attackRange(AttackRange.MELEE)
                .role(HeroRole.WARRIOR)
                .build());
    }
}

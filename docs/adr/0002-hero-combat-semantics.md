# 0002 — Hero combat semantics (float HP, maxHp final, heal no-op khi dead, respawn riêng)

## Status

accepted

## Context

Khi deepening `Hero` module để nó đại diện được concept cốt lõi (một Hero trong combat có HP biến động và có thể chết), cần pin xuống 4 quyết định load-bearing về HP semantics.

## Decision

1. **`currentHp` là `float`** — cho phép fractional damage (DoT, lifesteal) và regen mượt. `Enemy.getCurrentHp()` interface cũng trả `float` để khớp (xem consequences).
2. **`maxHp` final, set tại constructor** — đơn giản, đủ cho scope hiện tại. Level up / buff thay đổi maxHp để sau (sẽ cần method `setMaxHp` riêng).
3. **`heal` no-op khi `active=false`** — match game thật: dead hero không thể bị heal. Revival qua method riêng `respawn()`.
4. **`respawn()` method riêng** — match game: trigger bởi respawn timer bên ngoài, không tự động từ heal. Set `active=true`, `currentHp=maxHp`.

## Considered alternatives

- **HP là `int`**: mất precision khi có fractional damage. Cú pháp GoF kinh điển dùng int, nhưng game có DoT/lifesteal nên float đúng với domain hơn.
- **`maxHp` mutable với setter `setMaxHp`**: cần test cho "currentHp clamp về maxHp mới khi maxHp giảm". Quá anticipatory cho scope hiện tại.
- **Heal auto-revive**: không match game thật. Heal trên dead hero là meaningless.
- **Chỉ có `isAlive()`, không có `active` flag riêng**: `isAlive()` vẫn work nhưng thiếu explicit state field, khó debug, không distinguish "dead" vs "untargetable cho lý do khác".

## Consequences

- `Enemy.getCurrentHp()` phải trả `float` (không phải `int`). Strategies đọc float trực tiếp, không qua `Math.round` → không có rounding artifact trong tie-break.
- `Hero.getCurrentHpExact()` method đã bị xóa (đã tồn tại chỉ để bypass `Math.round` — không cần nữa).
- `Hero` constructor throw `IllegalArgumentException` khi `maxHp <= 0`. Tests cho exception path chưa được viết — coverage gap sẽ đóng khi thêm test hygiene.
- Future extensions: damage modifiers (armor/magic resistance) sẽ là composition hoặc method trên Hero (xem learning-records/0004).
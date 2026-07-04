# 0004 — Hero deepening: combat semantics qua 7 TDD cycles

## Status

active

## Evidence

Đã drive 7 cycles TDD thuần sau grilling session, biến Hero từ data holder thành combat module với lifecycle đầy đủ. Toàn bộ progression captured trong `src/test/java/com/lqm/combat/HeroTest.java` (8 tests) và `src/main/java/com/lqm/combat/Hero.java`.

## Decisions đã chốt (từ grilling)

| Decision | Value |
|----------|-------|
| HP type | float (thay vì int) |
| maxHp | final, set tại constructor |
| takeDamage | `float amount` ≥ 0; clamp 0; set active=false nếu HP ≤ 0 |
| heal | `float amount` ≥ 0; clamp maxHp; no-op nếu !active |
| respawn | method riêng; set active=true, currentHp=maxHp |
| isAlive | returns active |

Quyết định dựa trên game thật (Liên Quân): heal không work trên dead hero (game mechanic), revival qua respawn timer bên ngoài (không phải heal auto-revive).

## Implications

### Depth ratio thay đổi

Trước: Hero interface = 4 getters + 1 delegate. Shallow — interface complexity ≈ implementation complexity.
Sau: Hero interface = 5 methods (getPosition, getCurrentHp, getName, takeDamage, heal, respawn, isAlive, selectTarget) hiding meaningful combat logic. Deeper — interface relatively simple, semantics rich.

### Locality gained

HP state (currentHp, maxHp, active) và HP-mutation logic (takeDamage, heal, respawn) đều ở Hero. Future damage modifiers (armor, magic resistance), lifesteal, regen — tất cả sẽ là methods trên Hero hoặc composition qua Hero, không phân tán.

### Test coverage

HeroTest từ 1 test → 8 tests. Mỗi method có ≥ 1 test riêng. Test seam là public interface của Hero. Tests dùng `TestEnemy` factory (đã có sẵn từ refactor trước).

### Cấu trúc TDD progression

7 cycles theo vertical slice:
1. Edge case cơ bản: takeDamage giảm HP
2. Edge case: takeDamage clamp 0
3. State transition: HP=0 → !active → selectTarget=null
4. Positive case: heal tăng HP
5. Edge case (characterization): heal clamp maxHp
6. Dead-state: heal no-op khi dead
7. State transition: respawn restore

### Open questions cho future

- Damage modifiers (armor, magic resistance): nên là field trên Hero hay external module áp vào Hero? Có thể cần Chain of Responsibility pattern.
- Lifesteal: có phải là strategy của attacker không, hay method trên Hero? Có thể là Decorator pattern.
- Regen theo tick: có cần method `regenerate()` riêng hay implicit trong `tick()` của combat loop?

### Architecture note

Workspace vẫn chưa có `CONTEXT.md` (chỉ có `GLOSSARY.md`). Domain terms mới (currentHp, maxHp, takeDamage, heal, respawn, isAlive, active) được add vào GLOSSARY.md. Nếu workspace adopt CONTEXT.md convention sau này, cần recheck vocabulary consistency.
# 0001 — Strategy pattern cho Target Selection

## Status

accepted

## Context

Liên Quân Mobile có cơ chế auto-target khi Hero tự động đánh. Game thật có đúng 2 quy tắc auto-target toàn cục: Nearest (Tulen passive `Lôi điện`) và LowestHP (Butterfly `Ám Sát`, Lorion passive). Cần mô hình hóa bằng Java.

## Decision

Dùng **GoF Strategy pattern**: `TargetSelector` interface với method `select(Enemy attacker, List<Enemy> enemies)`. Concrete strategies: `NearestEnemy`, `LowestHP`. `Hero` (Context) giữ reference tới một `TargetSelector` và ủy quyền cho nó.

Chỉ có **2 strategies** được implement. Per-ability logic (marked target, target lock, skillshot direction) được xác nhận là **out of scope** cho `TargetSelector` interface — chúng là logic riêng của từng skill, có input/output khác (hướng bắn, mark state, AoE radius), và ép vào `TargetSelector` sẽ làm pattern mất gọn.

## Considered alternatives

- **`if/else` chain trong `Hero`**: vi phạm Open/Closed. Mỗi lần thêm tướng mới → sửa `Hero`. Tests trở thành bulk.
- **Inheritance** (`NearestEnemyHero extends Hero`): bùng nổ tổ hợp khi có N chiều biến thể (role × lane × strategy).
- **`switch` trên enum**: vẫn vi phạm O/C. Thêm enum = sửa switch.

## Consequences

- `TargetSelector` là public seam — strategies test được ở pure isolation (NearestEnemyTest, LowestHPTest).
- Thêm strategy mới (FarthestEnemy, MostHP, ...) chỉ cần class mới + implement interface. Zero sửa `Hero`.
- `Hero` chỉ giữ reference, ủy quyền. Strategy logic không leak vào Hero.
- Khi `LowestHP` cần đọc float HP từ enemy, Enemy interface phải trả float (xem ADR-0002).
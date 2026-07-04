# 0003 — TDD progression cho NearestEnemy và LowestHP

## Status

active

## Evidence

Đã drive 6 cycles TDD thuần cho 2 strategy trong workspace. Mỗi cycle = 1 test mới → minimal impl để pass. Toàn bộ progression được thực hiện với seam đã thống nhất trước đó (chỉ test `select()` của 2 strategies, không test Hero Context).

## Implications

### Pattern đã học (TDD loop)

- **RED**: viết test mới — test phải fail. Failure có thể là compile error (như Cycle 1 khi NearestEnemy chưa tồn tại) hoặc assertion fail (như Cycle 2+ khi impl hiện tại không thỏa test mới).
- **GREEN**: viết minimal impl chỉ đủ pass test hiện tại. Đừng viết trước cho test tương lai.
- **REFACTOR**: KHÔNG trong loop. Làm sau, ở review stage.

### Cấu trúc test theo vertical slice

Mỗi strategy được xây qua 3 slice đồng nhất:
1. Edge case: empty list → null (slice 1)
2. Trivial case: 1 enemy → enemy đó (slice 2)
3. Real case: nhiều enemy → so sánh theo tiêu chí của strategy (slice 3)

Cùng interface, cùng skeleton test, khác tiêu chí so sánh. Điều này cho thấy Strategy pattern: thay đổi thuật toán không đổi interface.

### Tie-break policy đã chốt (implicit qua impl)

Cả NearestEnemy và LowestHP đều dùng **strict less-than** (`d < minDistance`, `hp < minHp`). Hệ quả: enemy đầu tiên trong list thắng khi tie. Đây là behavior test ngầm định — chưa có test riêng cho tie-break, nhưng có thể cần nếu muốn document explicit.

### Refactor opportunities (deferred, không trong loop)

- Test helper `enemyAt` bị duplicate giữa NearestEnemyTest và LowestHPTest. Nên extract sang test helper class `TestEnemy` hoặc factory (file `/test/java/com/lqm/test/TestEnemy.java`).
- Logic chọn "min" theo comparator có thể extract sang generic helper (Template Method hoặc Strategy lồng Strategy). Nhưng đây là over-engineering cho 2 strategies; skip.

### Bài học về test design

Tests nên verify **behavior ở public interface**, không verify implementation:
- ✅ `assertSame(near, result)` — quan sát output, không quan tâm NearestEnemy có biến `minDistance` hay không.
- ❌ (Tránh) kiểm tra `selector.getMinDistance()` hoặc tương tự — implement detail coupling.
- Expected values trong test là literal/known-good (`(10, 0)`, `100`) — không phải tautology (không tính lại expected bằng cùng code với impl).

### Lesson 0001 cần sửa sau này?

Lesson 0001 hiện có code snippet cho NearestEnemy và LowestHP. Code thật trong `src/main/java/com/lqm/strategy/` match với snippet (cùng structure, cùng thuật toán). Lesson có thể link tới file code thật thay vì inline snippet — sẽ cải thiện lesson. Chưa ưu tiên.
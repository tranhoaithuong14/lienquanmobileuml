# 0002 — Correction: Target Selection chỉ có 2 quy tắc auto-target toàn cục

## Status

active

## Evidence

User tuyên bố dứt khoát ở session 2026-07-04 sau khi đọc lesson 0001: *"game thật chỉ có 2 phương pháp lựa chọn mục tiêu là: Máu thấp nhất và Gần nhất"*. Khi được hỏi phạm vi, user xác nhận: "Target Selection" trong model của ta = **chỉ 2 quy tắc auto-target toàn cục**, không bao gồm per-ability logic (marked target, target lock, skillshot direction).

## Implications

- Lesson 0001 đã được viết lại: bỏ `MarkedTarget` section, thay bằng "Phạm vi của Target Selector — chỗ Strategy dừng lại" (giải thích tại sao per-ability logic không vừa `TargetSelector` interface).
- Quiz ở lesson 0001 đã được sửa: bỏ 2 câu về `MarkedTarget`, thay bằng câu về edge cases (empty list, tie-break) và câu về ranh giới pattern.
- Research note `.scratch/research/target-selection-options.md` chứa nhiều finding về per-ability logic (marked, lock-on, skillshot) — đây là research hợp lệ về cơ chế game, nhưng KHÔNG map sang `TargetSelector` pattern. Đã thêm correction note ở đầu file.
- Glossary đã đánh dấu `MarkedTarget` là "Out of scope" và giữ nguyên nhưng có note giải thích.
- Đây là correction quan trọng: nếu ta tiếp tục model `MarkedTarget` như một ConcreteStrategy, ta sẽ ép một interface không phù hợp và dạy sai ranh giới của Strategy pattern.
- Zone of proximal development shift: lesson tiếp theo nên tập trung sâu hơn vào 2 strategy hiện có (fallback strategy, tie-break policy, test coverage), chứ không mở rộng sang strategy thứ 3.
- Khi dạy pattern tiếp theo (State, Observer, Command), bài học "đừng ép mọi thứ vào cùng interface" sẽ là nguyên lý nền — record này là evidence quan trọng cho nguyên lý đó.
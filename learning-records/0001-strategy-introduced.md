# 0001 — Strategy pattern giới thiệu qua 3 Target Selectors

## Status

active

## Evidence

User đã chốt ở grilling session trước:
- Đang học GoF pattern đầu tiên là **Strategy**.
- Bài toán cụ thể: **Target Selection** trong combat Liên Quân Mobile.
- Đã có research note về cơ chế chọn mục tiêu thật của game ở `.scratch/research/target-selection-options.md`.

User yêu cầu lesson đầu tiên giải thích `NearestEnemy`, `LowestHP`, `MarkedTarget`.

## Implications

- Lesson đầu tiên phải vừa dạy cấu trúc Strategy pattern (Strategy, ConcreteStrategy, Context, Client), vừa dạy cách áp dụng vào Liên Quân.
- 3 strategies trong lesson này được chọn vì chúng map với primary source thật (Butterfly `Ám Sát` cho LowestHP, Keera/Quillen cho MarkedTarget, và "kẻ địch lân cận" trong Tulen passive cho NearestEnemy).
- Quiz phải có same-length options để build retrieval strength, không phải fluency.

## Open

- Chưa có evidence user thực sự hiểu Strategy (mới chỉ yêu cầu giải thích). Sau lesson này cần quiz kết quả để ghi nhận.
- Chưa rõ user đã biết Java interface chưa — nếu quiz fail ở phần nào, cần lesson phụ.
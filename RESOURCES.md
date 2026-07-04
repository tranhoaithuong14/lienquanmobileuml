# Strategy Pattern Resources

## Knowledge

- **[Refactoring Guru — Strategy](https://refactoring.guru/design-patterns/strategy)**
  Visual UML + Java/Python/C++/TS examples. Dùng cho: giải thích cấu trúc pattern, xem code mẫu ngắn, hình ảnh minh họa trong lesson. High-trust, cộng đồng công nhận rộng.

- **Gamma et al., _Design Patterns: Elements of Reusable Object-Oriented Software_ (1994)** — Chương 5, "Strategy".
  Primary source kinh điển. Dùng cho: định nghĩa canonical, Motivation, Applicability, Known Uses (ví dụ thật như lexers, object layout). Sách gốc, đọc khi cần ground truth.

- **Freeman & Freeman, _Head First Design Patterns_ (2004)** — Chương 1 (Duck Simulator).
  Dùng cho: ví dụ thực tế dễ nhớ (duck behavior thay đổi runtime), cách dạy bằng hình ảnh và đối thoại. Phù hợp khi user cần anchor trực quan.

- **[SourceMaking — Strategy](https://sourcemaking.com/design_patterns/strategy)**
  Tóm tắt có cấu trúc: intent, problem, solution, applicability. Dùng cho: cheat sheet nhanh khi cần recall structure.

- **Research note cục bộ: `.scratch/research/target-selection-options.md`**
  Primary-source research về cơ chế chọn mục tiêu thật của Liên Quân. Dùng cho: map concrete strategies sang cơ chế game thật, just ví dụ thuyết phục.

## Wisdom (Communities)

- **[r/learnprogramming](https://reddit.com/r/learnprogramming)**
  Hỏi đáp về pattern application, code review. Dùng cho: khi áp dụng Strategy vào code thật bị stuck.

- **[r/java](https://reddit.com/r/java)**
  Discussion về Java idiom hiện đại (records, sealed types). Dùng cho: thảo luận cách viết Strategy interface idiomatic Java 17+.

- **Stack Overflow — tag [design-patterns] + [strategy-pattern]**
  Search "strategy pattern Java" cho Q&A chất lượng cao. Dùng cho: edge cases (multi-strategy, thread-safety, null strategy).

## Gaps

- Chưa có tài liệu tiếng Việt chất lượng cao về GoF patterns — nếu user tiếng Anh đọc kém, cần research thêm hoặc viết summary tiếng Việt nội bộ.
- Chưa có benchmark so sánh performance giữa Strategy + interface dispatch vs `if/else` chain — không quan trọng cho lesson đầu nhưng có thể cần nếu mở rộng sau.
# Mission: Strategy Pattern trong Java qua Liên Quân Mobile

## Why

Đang xây dựng một Java teaching artifact mô phỏng các hệ thống của Liên Quân Mobile — bắt đầu với **cơ chế chọn mục tiêu trong combat** của tướng. Strategy pattern là pattern đầu tiên trong GoF cần nắm vững, vì nó xuất hiện ở hầu hết các hệ con của game (AI, combat, item build, target selection). Khi hiểu Strategy, các pattern tiếp theo (State, Observer, Command) sẽ tự nhiên hơn nhiều.

Mục tiêu cuối cùng: đọc thành thạo code Strategy trong Java, nhận diện được chỗ nào trong game thật nên dùng Strategy, và viết được implementation sạch + UML rõ ràng cho teaching artifact.

## Success looks like

- Đọc code Java có Strategy pattern là nhận ra ngay ai là Context, ai là Strategy, ai là ConcreteStrategy — không cần đọc comment.
- Tự viết được 4 concrete `TargetSelector` (`NearestEnemy`, `LowestHP`, `HeroPriority`, `MarkedTarget`) từ đầu, với interface + Context chuẩn GoF.
- Giải thích được vì sao Strategy tốt hơn `if/else` hoặc `switch` trong trường hợp này, và vì sao tốt hơn `inheritance`.
- Vẽ được class diagram (PlantUML) cho Strategy và áp dụng được vào một hệ con khác ngoài target selection.

## Constraints

- Ngôn ngữ giảng dạy: **Tiếng Việt** (người dùng dùng tiếng Việt trong giao tiếp). Thuật ngữ kỹ thuật giữ tiếng Anh.
- Mỗi lesson phải ngắn — working memory rất nhỏ. Ưu tiên 1 thắng rõ ràng mỗi lesson.
- Code minh họa dùng Java 17+ (records, switch expressions) nếu giúp bài gọn hơn, nhưng không bắt buộc — ưu tiên cú pháp GoF kinh điển.
- Mỗi lesson phải có retrieval practice (quiz) để build storage strength, không chỉ fluency.

## Out of scope

- Các GoF pattern khác (State, Observer, Command, ...) — chờ đến sau khi Strategy vững.
- Toàn bộ scope Liên Quân (140 tướng, ranked, monetization, networking) — teaching artifact dùng game làm ngữ cảnh tương tác, không phải đích cuối.
- UML nâng cao (sequence diagram, state machine) — để sau khi class diagram vững.
- Lịch sử/tài liệu gốc của GoF 1994 đọc sâu — đủ để biết đó là primary source, không cần đọc hết.

## Open questions

- User chưa nói rõ họ đã có nền OOP Java đến đâu (mới học Java, hay biết rồi mà chưa quen patterns). Cần xác nhận trong lesson đầu tiên để calibrate.
- User dùng tiếng Việt — chưa rõ trình độ tiếng Anh đọc technical docs (Refactoring Guru, GoF book). Nếu kém thì cần resource tiếng Việt.
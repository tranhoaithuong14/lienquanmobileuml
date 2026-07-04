# Notes

## User preferences (confirmed)

- **Ngôn ngữ giao tiếp**: Tiếng Việt. Thuật ngữ kỹ thuật giữ tiếng Anh.
- **Ngôn ngữ code**: Java (đã chốt ở grilling session trước).
- **Công cụ UML**: PlantUML (đã chốt ở grilling session trước).
- **Pattern đang học**: Strategy — pattern GoF đầu tiên. Chưa mở rộng sang pattern khác.
- **Bài toán cụ thể**: Target Selection trong combat Liên Quân.

## Working assumptions (chưa xác nhận)

- User có nền OOP Java cơ bản — biết class, interface, inheritance. Nếu sai cần quay lại calibrate.
- User chưa học Strategy pattern lần nào. Nếu đã biết rồi, lesson sẽ cần nâng level.
- User không có deadline cụ thể — tốc độ tự điều chỉnh theo phản hồi.

## Open questions

- User tiếng Anh đọc technical docs ở mức nào? Có thể đọc Refactoring Guru không?
- User thích đọc prose giải thích trước, hay thích xem code trước rồi giải thích sau?
- Có muốn làm bài tập Java thật (chạy được, có test) hay chỉ đọc hiểu pattern?

## Known corrections from user (do not re-derive)

- **Target Selection = chỉ 2 quy tắc auto-target toàn cục** (Nearest + Lowest HP). Per-ability logic (marked, lock-on, skillshot) là phạm trù khác. Đây là domain definition do user xác nhận — không cần research lại, không cần hỏi lại.
# Strategy Pattern Glossary

Canonical language for the Strategy pattern teaching workspace. Mọi lesson, code, và reference phải dùng các thuật ngữ này. Nếu thuật ngữ mới xuất hiện trong lesson, nó chỉ được thêm vào đây sau khi user đã dùng đúng trong quiz hoặc bài tập.

## Terms

**Strategy**:
Một interface (hoặc abstract class) định nghĩa *hành vi* mà nhiều thuật toán có thể thay thế cho nhau.
_Avoid_: Algorithm interface, behavior contract

**ConcreteStrategy**:
Một class implement `Strategy`, cung cấp một cách cụ thể để thực hiện hành vi.
_Avoid_: Strategy implementation, behavior variant

**Context**:
Class giữ một reference tới `Strategy` và ủy quyền công việc cho nó. Context không biết ConcreteStrategy cụ thể nào đang được dùng.
_Avoid_: Client, Caller, Holder

**Client**:
Code bên ngoài chọn ConcreteStrategy nào và gắn vào Context (lúc khởi tạo hoặc qua setter).
_Avoid_: User code, Main

**Target Selection**:
Trong game MOBA, quyết định *đánh ai* khi tướng tự động tấn công hoặc dùng skill.
_Avoid_: Targeting, Auto-attack logic

**NearestEnemy**:
ConcreteStrategy chọn mục tiêu theo khoảng cách Euclidean nhỏ nhất từ attacker.
_Avoid_: Closest target strategy

**LowestHP**:
ConcreteStrategy chọn mục tiêu có lượng máu hiện tại thấp nhất.
_Avoid_: Weakest target, Lowest health strategy

**MarkedTarget**:
_(Out of scope.)_ Per-ability logic trong game (Keera, Quillen) — không thuộc `TargetSelector` pattern. Đừng ép vào interface này.
_Avoid_: TargetSelector implementation for marks

**HeroPriority**:
_(Not implemented.)_ Lọc danh sách mục tiêu, chỉ giữ tướng địch, bỏ qua lính và quái rừng. Hiện không dùng trong model vì game thật chỉ có 2 strategy toàn cục.
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

## Combat lifecycle (added 2026-07-04)

**CombatStats**:
HP state machine cho entity có thể chịu damage và chết. Tách ra từ Hero ở review #3 (Hero → Hero + CombatStats composition). Sở hữu maxHp, currentHp, active + các action (takeDamage, heal, respawn, isAlive). Reusable cho tower/creep/summon.
_Avoid_: Health, HpState

**currentHp**:
HP hiện tại của Hero, kiểu `float`. Mutable qua `takeDamage` và `heal`. Floor 0, ceiling maxHp.
_Avoid_: hp, HP

**maxHp**:
Giới hạn trên của currentHp, kiểu `float`. Final, set tại constructor. currentHp khởi đầu = maxHp.
_Avoid_: maxHealth, hpCap

**active**:
`boolean` flag trên Hero. `true` = có thể hành động; `false` = đã chết. Set `false` khi takeDamage đưa currentHp ≤ 0. Set `true` khi `respawn()`. Không thay đổi khi heal — heal chỉ work khi active=true.
_Avoid_: alive, canAct

**isAlive**:
Trả về `true` nếu Hero còn sống. Định nghĩa hiện tại: `currentHp > 0`. Tương đương `active` trong model này.
_Avoid_: isDead (ngược lại)

**takeDamage**:
Phương thức Hero giảm currentHp. Chỉ nhận amount ≥ 0. Floor tại 0. Set `active=false` nếu currentHp chạm 0.
_Avoid_: damage, applyDamage

**heal**:
Phương thức Hero tăng currentHp. Chỉ nhận amount ≥ 0. Ceiling tại maxHp. No-op nếu `active=false` (match game: dead hero không thể bị heal).
_Avoid_: restore, recover

**respawn**:
Phương thức Hero hồi sinh. Set `active=true` và `currentHp = maxHp`. Match game thật: trigger bởi respawn timer bên ngoài, không phải từ heal.
_Avoid_: revive, spawnAgain

## Geometry (added 2026-07-04)

**distanceTo**:
Method trên `Position` trả khoảng cách Euclidean đến một `Position` khác. Symmetric, trả 0 khi cùng vị trí. Math thuộc về Position, không phải của strategy nào.
_Avoid_: getDistance, euclideanDistance

## Strategy helpers (added 2026-07-04)

**MinSelector**:
Static helper trong package `strategy`. Method `minBy(List<T>, ToDoubleFunction<T>)` trả phần tử có score nhỏ nhất. Được dùng bởi NearestEnemy (scorer = distanceTo) và LowestHP (scorer = getCurrentHp). Tie-break: strict less-than → phần tử đầu thắng.
_Avoid_: minByScorer, findMin
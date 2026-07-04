# Liên Quân Mobile — Target Selection & Combat Context

Domain context cho việc redesign hệ thống chọn mục tiêu trong combat và vòng đời HP của Hero trong Liên Quân Mobile, bằng Java + GoF patterns. Đây là một single context — toàn bộ work trong repo thuộc cùng một bounded context.

## Language

### Pattern terms (GoF Strategy)

**Strategy**:
Interface mô tả hành vi có thể thay thế cho nhau bằng các thuật toán khác nhau.
_Avoid_: Algorithm interface, behavior contract

**ConcreteStrategy**:
Class implement `Strategy`, cung cấp một cách cụ thể để thực hiện hành vi đó.
_Avoid_: Strategy implementation, behavior variant

**Context**:
Class giữ reference tới `Strategy` và ủy quyền công việc cho nó. Context không biết ConcreteStrategy cụ thể nào đang được dùng.
_Avoid_: Client, Caller, Holder

**Client**:
Code bên ngoài chọn `ConcreteStrategy` nào và gắn vào `Context` (lúc khởi tạo hoặc qua setter).
_Avoid_: User code, Main

### Target Selection

**Target Selection**:
Quy tắc auto-target toàn cục của Hero trong Liên Quân Mobile — khi Hero tự động đánh, đánh ai. Có đúng 2 quy tắc toàn cục trong game: Nearest và LowestHP. Per-ability logic (marked target, target lock, skillshot direction) là phạm trù khác, không thuộc về `TargetSelector` interface.
_Avoid_: Targeting, Auto-attack logic

**NearestEnemy**:
ConcreteStrategy chọn mục tiêu theo khoảng cách Euclidean nhỏ nhất từ attacker. Trong game thật: Tulen passive `Lôi điện`.
_Avoid_: Closest target strategy

**LowestHP**:
ConcreteStrategy chọn mục tiêu có HP hiện tại thấp nhất. Trong game thật: Butterfly `Ám Sát`, Lorion passive.
_Avoid_: Weakest target, Lowest health strategy

### Combat Lifecycle

**Hero**:
Một tướng trong Liên Quân Mobile. Vừa là attacker (có `TargetSelector` để chọn mục tiêu), vừa là target (implement `Enemy` để bị tướng khác chọn). Sở hữu HP lifecycle (currentHp, maxHp, active state).
_Avoid_: Champion, Character, Avatar

**Enemy**:
Bất kỳ thực thể nào có thể bị chọn làm mục tiêu: Hero, lính, quái rừng. Interface mà strategies đọc từ đó.
_Avoid_: Target, Opponent

**currentHp**:
HP hiện tại của Hero, kiểu `float`. Mutable qua `takeDamage` và `heal`. Floor 0, ceiling maxHp.
_Avoid_: hp, HP

**maxHp**:
Giới hạn trên của `currentHp`, kiểu `float`. Final, set tại constructor. `currentHp` khởi đầu bằng `maxHp`.
_Avoid_: maxHealth, hpCap

**active**:
`boolean` flag trên Hero. `true` = có thể hành động; `false` = đã chết. Set `false` khi `takeDamage` đưa currentHp xuống 0. Set `true` khi `respawn()`. Không thay đổi khi `heal` — heal chỉ work khi `active=true`.
_Avoid_: alive, canAct

**isAlive**:
Trả về `true` nếu Hero còn sống. Trong model này: `currentHp > 0`, tương đương `active`.
_Avoid_: isDead (ngược lại)

**takeDamage**:
Phương thức Hero giảm currentHp. Chỉ nhận amount ≥ 0. Floor tại 0. Set `active=false` nếu currentHp chạm 0.
_Avoid_: damage, applyDamage

**heal**:
Phương thức Hero tăng currentHp. Chỉ nhận amount ≥ 0. Ceiling tại maxHp. No-op nếu `active=false` (match game thật: dead hero không thể bị heal).
_Avoid_: restore, recover

**respawn**:
Phương thức Hero hồi sinh. Set `active=true` và `currentHp=maxHp`. Match game thật: trigger bởi respawn timer bên ngoài, không phải từ heal.
_Avoid_: revive, spawnAgain

### Geometry

**Position**:
Vị trí trong bản đồ game. Java record với `x` và `y` (double).
_Avoid_: Coordinate, Point

**distanceTo**:
Method trên `Position` trả khoảng cách Euclidean đến một `Position` khác. Symmetric, trả 0 khi cùng vị trí. Math thuộc về Position, không phải của strategy nào.
_Avoid_: getDistance, euclideanDistance

### Strategy Helpers

**MinSelector**:
Static helper trong package `strategy`. Method `minBy(List<T>, ToDoubleFunction<T>)` trả phần tử có score nhỏ nhất. Được dùng bởi NearestEnemy (scorer = `distanceTo`) và LowestHP (scorer = `getCurrentHp`). Tie-break: strict less-than → phần tử đầu thắng.
_Avoid_: minByScorer, findMin
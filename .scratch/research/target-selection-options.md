# Liên Quân Mobile — Target Selection Research

**Research date:** 2026-07-04
**Purpose:** feed Strategy-pattern design for target-selection algorithms in a Java redesign of Liên Quân Mobile.
**Method:** primary-source investigation only — official Vietnamese Garena site (`lienquan.garena.vn`), official hero pages ("Học viện" / Academy), and official patch notes ("Cập nhật"). No fan wikis, third-party guides, or AI summaries were used.

> **⚠️ Correction (2026-07-04, user-authoritative):**
> Trong **model thiết kế** của teaching artifact này, "Target Selection" được định nghĩa **hẹp hơn** scope của research này: chỉ là **2 quy tắc auto-target toàn cục** — Máu thấp nhất (`LowestHP`) và Gần nhất (`NearestEnemy`).
>
> Các finding dưới đây về **per-ability logic** (marked target, target lock, skillshot direction, "nearest within range" cho từng skill) là research hợp lệ về cơ chế game thật, nhưng **không thuộc `TargetSelector` interface** — chúng là logic riêng của từng skill, có input/output khác (hướng bắn, mark state, AoE radius), và ép vào `TargetSelector` sẽ làm pattern mất gọn.
>
> Lesson 0001 (`lessons/0001-strategy-target-selection.html`) đã được sửa để phản ánh scope hẹp này. Xem `learning-records/0002-target-selection-correction.md` để biết lý do.

---

## Summary

- Liên Quân exposes **no global "auto-attack targeting mode" toggle** in its public-facing materials; targeting rules are baked into each hero ability text.
- The single most explicit game-wide targeting mechanic the official site uses is **"khóa mục tiêu"** (target lock), referenced in two recent patch notes (Zill, Baldum, Flowborn) and surfaced in skill descriptions like Flowborn's `Nỏ tiễn liên hoàn`.
- Official hero ability text names at least **eight distinct selection rules** observable in the live Vietnamese client: lowest HP, lowest HP with hero priority, nearby AoE, marked target, designated (aimed) target, "first enemy hero hit", directional ("in front"), and "nearest within range".
- "Hero priority" (`ưu tiên tướng`) is the most common official sub-rule layered on top of area rules (Tulen passive, Butterfly ultimate).
- Targeting-state and "cannot be targeted" (`không thể bị chọn làm mục tiêu`) is also an official concept (Keera skill 3, Liliana skill 4, Baldum CC state).

---

## Findings

### Finding 1 — "Khóa mục tiêu" (target lock) is an official, named mechanic

> "Chiêu cuối của Zill có độ ngẫu nhiên quá cao khiến người chơi khó dự đoán vị trí đáp xuống sau khi chiêu cuối kết thúc. Vì vậy, chúng tôi đã tối ưu **logic khóa mục tiêu** của chiêu cuối."
> — Patch notes 04.06.2026 / 10.06.2026, "ĐIỀU CHỈNH GIỮA MÙA PB LỄ HỘI 5v5" (Zill section)

> "Chiêu 3 – Nỏ tiễn liên hoàn: Flowborn bắn 7 loạt nỏ tiễn nhẹ vào khu vực chỉ định (**khóa mục tiêu trong phạm vi**), gây sát thương vật lý và tăng tốc chạy."
> — Flowborn release notes 08.04.2026 (official patch notes "CHI TIẾT BẢN CẬP NHẬT LỄ HỘI 5v5")

**Source:** `https://lienquan.garena.vn/dieu-chinh-giua-mua-pb-le-hoi-5v5-04-06-2026/` and `https://lienquan.garena.vn/chi-tiet-ban-cap-nhat-le-hoi-5v5-08-04-2026/` (official patch notes, accessed 2026-07-04). **Type:** official patch notes.

### Finding 2 — "Mục tiêu chỉ định" (designated / aimed target) is the standard pattern for player-aimed abilities

Many hero abilities resolve onto a "mục tiêu chỉ định" — i.e. the player taps a target location or unit and the skill resolves there. Examples:

- D'Arcy, **Lập phương thứ nguyên**: "D'Arcy tạo ra một Lập phương thứ nguyên tại **điểm đã chọn**" — at the chosen point.
- D'Arcy, **Ma trận thứ nguyên**: "D'Arcry tạo ra một Ma trận thứ nguyên tại **điểm đã chọn**."
- Flowborn, **Nỏ tiễn phá không**: "Flowborn bắn một nỏ tiễn khổng lồ về **mục tiêu chỉ định** (có thể bị tướng địch chặn lại), gây sát thương vật lý và đẩy lùi đơn vị không phải tướng."
- Annette, **Gió xoáy**: "Annette tạo ra một cơn gió lốc gây sát thương phép mỗi 0.25 giây tại **điểm chỉ định**."
- Yena, **Toái nguyệt trảm / Tụ nguyệt trảm**: "Yena lướt theo **hướng chỉ định** vung đao chém địch…" / "vung Nguyệt đao gây sát thương vật lý **theo hướng chỉ định**".
- Airi, **Kiếm Vũ**: "Airi múa kiếm lướt theo **hướng chỉ định** gây sát thương vật lý."
- Aleister, **Ngục tù vĩnh hằng**: targets "kẻ địch" — full skill description confirms aimed cast.

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/darcy/`, `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/yena/`, `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/annette/`, `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/airi/`, `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/aleister/`, plus patch-notes Flowborn page above. **Type:** official hero pages (Academy / "Học viện") + official patch notes.

### Finding 3 — Lowest-HP target rule (with hero priority) — Butterfly ultimate

> "Ám Sát: **Butterfly xuất hiện phía sau kẻ địch có lượng máu thấp nhất (ưu tiên tướng)** và ám sát nạn nhân, gây sát thương vật lý."
> — Butterfly (Sát thủ / Assassin) hero page

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/butterfly/` (official hero page, accessed 2026-07-04). **Type:** official hero ability text.

**Behavior modelled:** pick the enemy with the lowest current HP; if multiple are tied at the lowest HP, break ties in favour of heroes (not minions/monsters).

### Finding 4 — Lowest-HP target rule (no explicit hero priority) — Lorion passive

> "Bí thuật hắc ám: Sau khi Lorion tung chiêu 2 và chiêu 3 trúng mục tiêu, hắn sẽ kích hoạt lôi cầu. Khi lôi cầu bên ngoài cơ thể, **nó sẽ gây sát thương phép lên kẻ địch ít máu nhất trong phạm vi lân cận** và giúp Lorion hồi máu."
> — Lorion (Pháp sư / Mage) hero page

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/lorion/` (official hero page, accessed 2026-07-04). **Type:** official hero ability text.

**Behavior modelled:** pick the enemy with the lowest current HP in a fixed radius around the hero.

### Finding 5 — Nearby / AoE rule with hero priority — Tulen passive

> "Lôi điện (Nội tại): Chiêu thức trúng đích giúp Tulen tích lũy dấu ấn Lôi Điện. Khi đạt tới 5 điểm **Tulen sẽ tạo ra 5 luồng sét quanh bản thân, công kích kẻ địch lân cận (ưu tiên tướng)** gây ra sát thương phép mỗi luồng. … (sét đánh không tấn công các đơn vị quái rừng không trong trạng thái chiến đấu)."
> — Tulen (Pháp sư / Mage) hero page

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/tulen/` (official hero page, accessed 2026-07-04). **Type:** official hero ability text.

**Behavior modelled:** pick up to N enemies in the radius around the hero; if heroes are present, allocate strikes to heroes first; non-combat-state jungle monsters are filtered out entirely.

### Finding 6 — Marked-target rule — Butterfly ultimate follow-up, Keera skill 2, Quillen skill 3

> "Ám Sát (Butterfly): … mục tiêu sẽ **bị đánh dấu**. Khi kết thúc đánh dấu hoặc mục tiêu đã chết, một phần sát thương mà mục tiêu đã chịu sẽ chuyển thành trị liệu cho bản thân."
> "Ác mộng ảo ảnh (Keera): Keera đặt 1 huyễn ảnh tại vị trí đang đứng và nhảy đến điểm chỉ định gây sát thương phép trong phạm vi. **Nạn nhân gần nhất sẽ bị đánh dấu trong 3 giây** đồng thời bị kéo đến gần Keera."
> "Đoản mệnh (Quillen): Quillen lướt đi, khi đến đích lập tức tấn công kẻ địch gây sát thương vật lý và **đánh dấu nó trong 10 giây (chỉ có thể đánh dấu một kẻ địch mỗi lần)**. Mỗi lần Quillen công kích nạn nhân bị đánh dấu sẽ làm chậm tốc chạy trong 1 giây…"

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/butterfly/`, `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/keera/`, `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/quillen/` (official hero pages, accessed 2026-07-04). **Type:** official hero ability text.

**Behavior modelled:** apply a mark to one enemy (closest enemy in Keera's case; the targeted enemy in Quillen's case). Subsequent re-targets return to the same marked unit until the mark expires. Keera's text "Nạn nhân gần nhất" = "nearest victim" — a separate nearest-target rule used to choose the mark.

### Finding 7 — "Tướng địch đầu tiên trúng phải" (first enemy hero hit) — Yorn ultimate

> "Tên thần: Yorn bắn một mũi tên cường hóa đến một **mục tiêu gây sát thương vật lý lên tướng địch đầu tiên trúng phải**, kèm theo lượng máu đã mất của mục tiêu thành sát thương phép thuật (tối đa 1000 sát thương lên quái)."
> — Yorn (Xạ thủ / Marksman) hero page

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/yorn/` (official hero page, accessed 2026-07-04). **Type:** official hero ability text.

**Behavior modelled:** fire a skillshot that travels in a fixed direction and stops / latches onto the **first enemy hero** it intersects (heroes take precedence over minions along the line).

### Finding 8 — Directional / "in front" rule — Yorn passive

> "Vô tận tên (Nội tại): … Yorn bắn ra 7 mũi tên tầm xa **công kích kẻ địch trước mặt** gây sát thương vật lý và 1% máu tối đa kẻ địch sát thương chuẩn mỗi đợt. **Lính, quái và trụ nhận nhiều sát thương vật lý hơn.**"
> — Yorn hero page

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/yorn/`. **Type:** official hero ability text.

**Behavior modelled:** attacks only units positioned in front of the hero (within an implied cone), with a damage modifier that boosts damage against non-hero units (minions, monsters, towers). The damage-modifier note is not itself targeting but is a related piece of targeting context (the game distinguishes heroes from other unit types).

### Finding 9 — "Kẻ địch lân cận" / "trong phạm vi" (nearby / in range) — the generic AoE pattern

Many skills use "lân cận" (nearby / adjacent) or "trong phạm vi" (within range) without specifying a sub-rule — a plain proximity AoE. Examples:

- Yena ultimate, **Mãn nguyệt trảm (Song đao)**: "Yena ghép cặp vũ khí thành Viên đao, gây sát thương vật lý **lên các kẻ địch lân cận**…"
- Yena ultimate, **Hoành nguyệt trảm (Nguyệt đao)**: "Yena tách vũ khí thành cặp Loan đao chém ngang bốn phía, gây sát thương vật lý **lên các nạn nhân lân cận**…"
- Mina, **Lưỡi hái phục hận**: "Mina có tỷ lệ xoay lưỡi hái mỗi khi gánh chịu sát thương, gây sát thương vật lý **lên những kẻ địch lân cận**."
- Chaugnar, **Nước xoáy**: "Chaugnar rung động cả cơ thể gây sát thương phép **lên tất cả kẻ địch lân cận**."

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/yena/`, `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/mina/`, `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/chaugnar/`. **Type:** official hero ability text.

**Behavior modelled:** plain proximity hit — everything within the radius takes damage, no further sub-rule.

### Finding 10 — "Lowest-HP ally in radius" (Lumburr passive)

> "Ỷ mạnh vệ yếu (Nội tại): Lumburr gia tăng giáp vật lý và giáp phép cho bản thân và **đồng đội có lượng máu thấp nhất trong bán kính 5m** xung quanh."
> — Lumburr (Đỡ đòn / Tank) hero page

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/lumburr/` (official hero page, accessed 2026-07-04). **Type:** official hero ability text.

**Behavior modelled:** ally-targeted variant of the lowest-HP rule — relevant if the design needs to model friendly-side selection too.

### Finding 11 — Hero / non-hero distinguished as a sub-rule, plus non-combat-state filter

> "Lôi điện (Tulen nội tại): … **sét đánh không tấn công các đơn vị quái rừng không trong trạng thái chiến đấu**."
> "Vô tận tên (Yorn nội tại): … **Lính, quái và trụ nhận nhiều sát thương vật lý hơn.**"
> "Dây leo (Y'bneth chiêu 2): Mọi chiêu trúng đích **5 lần (tướng tính 2)** giúp chiêu được làm mới và cho phép Y'bneth tung Cây Đổ."

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/tulen/`, `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/yorn/`, `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/ybneth/`. **Type:** official hero ability text.

**Behavior modelled:** the game treats "tướng" (hero), "lính" (minion), "quái" (jungle monster), and "trụ" (tower) as separate unit categories for both targeting and damage-modification purposes. A jungle monster also has an internal "in combat" (trạng thái chiến đấu) flag that disqualifies it from being targeted by certain AoE rules.

### Finding 12 — "Cannot be selected as target" (targeting-state concept)

> "Tam giác quỷ (Keera chiêu 3): … Khi tung chiêu Keera **không thể bị chọn làm mục tiêu** đồng thời biên giới tam giác sẽ khiến kẻ địch chạm vào bị choáng."
> "Biến ảnh hoán hình (Liliana chiêu 4): … Khi lướt Liliana **không thể bị chọn làm mục tiêu**, đồng thời tăng giáp và giáp phép cùng công phép trong 2.5 giây."
> "Baldum chiêu cuối (patch notes 04.06.2026): Trước đây, chiêu cuối của Baldum hất tung theo thời gian cố định, đồng thời trong thời gian đó **kẻ địch sẽ không thể bị chọn làm mục tiêu**. Điều đó khiến việc phối hợp với đồng đội chưa thật sự hiệu quả."

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/keera/`, `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/liliana/`, `https://lienquan.garena.vn/dieu-chinh-giua-mua-pb-le-hoi-5v5-04-06-2026/`. **Type:** official hero page + official patch notes.

**Behavior modelled:** "untargetability" is an official unit state; any target-selection strategy must be able to filter it out (and the targeting pipeline must also be able to *apply* it).

### Finding 13 — Phụ Trợ (Summoner Spells) have their own implicit target categories

The "Phụ Trợ" (Summoner Spells) page lists each spell and what it targets — useful because each spell is a separate in-game targeting rule bundled with the hero:

- **TRỪNG TRỊ** (Smite): "Gây 800 sát thương **lên quái xung quanh** và làm choáng chúng trong 1 giây" — targets jungle monsters in radius (jungle-only).
- **BỘC PHÁ**: "Gây sát thương chuẩn tương đương 16% máu đã mất **của kẻ địch**" — targets enemy units.
- **SUY NHƯỢC**: "Giảm 20% sát thương **của kẻ địch lân cận** gây ra" — targets nearby enemies.
- **NGẤT NGƯ**: "làm choáng **kẻ địch lân cận**" — nearby-enemy stun.
- **CẤP CỨU**: "hồi 15% máu cho bản thân và **những đồng minh xung quanh**" — nearby-ally heal.
- **TỐC BIẾN**: "dịch chuyển trong 1 khoảng cách nhất định **theo 1 hướng cụ thể**" — directional blink (no target unit).

**Source:** `https://lienquan.garena.vn/hoc-vien/phu-tro/` (official Summoner Spells page, accessed 2026-07-04). **Type:** official gameplay documentation.

**Behavior modelled:** summoner spells are themselves targeting rules — by unit category (quái / kẻ địch / đồng minh / hướng) — and they ride on top of the hero's normal selection rules.

### Finding 14 — "Combo order" (auto-attack sequencing) is a target-selection side-effect

> "Nakroth (patch notes 08.04.2026): *Trình tự đòn đánh thường từ nội tại và hiệu ứng quét ngang của chiêu 2 dễ phát sinh những tương tác ngoài dự kiến. Chúng tôi hy vọng cơ chế này sẽ không gây quá nhiều rắc rối, giúp thao tác của Nakroth trở nên mượt mà hơn.* … Nếu cú quét ngang của chiêu 2 được tung ra ở **lần đánh thường thứ tư** thì chiêu quét đó sẽ đồng thời kích hoạt luôn hiệu ứng bổ sung của đòn đánh thường thứ tư."

**Source:** `https://lienquan.garena.vn/chi-tiet-ban-cap-nhat-le-hoi-5v5-08-04-2026/` (official patch notes). **Type:** official patch notes.

**Behaviour note:** the "target" of an auto-attack combo is implicit in the attack sequence — i.e. once a unit is targeted, follow-up auto-attacks in the same sequence continue on that unit until the sequence ends. This is the closest official mention of how hero auto-attack "sticks" to a chosen target.

### Finding 15 — Combination rule: nearby + hero-priority + last-hit-aware — Yorn passive again

Yorn's passive combines a **directional** rule (Finding 8) with **damage modifiers per unit category** and an implicit "shoot at multiple units in the cone":

> "Vô tận tên: … bắn ra 7 mũi tên tầm xa **công kích kẻ địch trước mặt** … **Lính, quái và trụ nhận nhiều sát thương vật lý hơn.**"

The official text does not specify whether Yorn's cone prioritises heroes over minions — it just describes a cone and per-category damage. This is an important gap: many fan discussions assume a default behaviour here, but the official text does not state one.

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/yorn/`. **Type:** official hero ability text.

---

## Gaps & caveats

1. **No public UI screenshot of an in-game "Targeting Mode" setting.** The official "Cẩm nang" (Guides) section on `lienquan.garena.vn/tin-tuc/cam-nang/` is essentially empty (one stale 2024 article on communication features, no settings article). No official guide page describes a global "Attack priority" toggle. The user asked specifically about "targeting modes" as some MOBAs have — no such mode is documented in any primary source consulted. The same applies to "Smart cast / Smart targeting" — there is no official doc page covering it.

2. **Auto-attack default rule is not stated officially.** The closest official mention is the Nakroth combo-order note (Finding 14), which implies auto-attacks stay locked on a unit through a combo, but the **default** rule that picks which unit to start attacking on (e.g. "nearest enemy", "last-hit-able minion") is not stated in any hero page or patch note. This is a real gap — for the Strategy pattern design, this default behaviour has to be inferred from gameplay, not primary docs.

3. **Vietnamese (Garena VN) is the only language channel with current content.** `lienquan.garena.com` (global Garena) failed to load during this research. All quotations here are from the VN client. Game balance and feature labels may differ between regions (Tencent's Honor of Kings / AoV global vs VNG's Liên Quân).

4. **No official developer statement or interview** was located that defines the targeting system abstractly. Every concrete rule in the findings comes from individual hero ability text and patch notes — there is no design-overview document.

5. **"Ưu tiên tướng" wording is consistent** but **never explains the tie-breaker** between multiple heroes (closest? lowest HP? most recently attacked?). The official text stops at "ưu tiên tướng" without further specification. Any Strategy implementation must pick a tie-break rule on its own.

6. **"Kẻ địch đầu tiên trúng phải" (first enemy hero hit, Yorn ult)** is described as if it is a straight skillshot that simply resolves on the first enemy hero it intersects. There is no mention of a target-lock step; it reads as a pure geometry resolution. This is a different rule from "khóa mục tiêu" (Finding 1) — that distinction is important when modelling the strategy.

---

## Sources consulted

All accessed 2026-07-04 unless otherwise noted. Every URL below is on `lienquan.garena.vn` (official Vietnamese Garena channel).

1. `https://lienquan.garena.vn/` — official landing page. Confirms site ownership ("Công ty cổ phần Giải trí và Thể thao Điện tử Việt Nam", G1 licence). Type: official site.
2. `https://lienquan.garena.vn/hoc-vien/tuong-skin/` — hero roster. Used as index to reach individual hero pages. Type: official.
3. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/yena/` — Yena abilities (used for AoE "lân cận" wording). Type: official hero page.
4. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/butterfly/` — Butterfly abilities. Source of Finding 3 (lowest-HP with hero priority) and Finding 6 (marked target). Type: official hero page.
5. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/tulen/` — Tulen abilities. Source of Finding 5 (nearby + hero priority + non-combat filter). Type: official hero page.
6. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/darcy/` — D'Arcy abilities. Source of Finding 2 (designated target). Type: official hero page.
7. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/lauriel/` — Lauriel abilities. Confirms AoE / "vùng hiệu lực" wording patterns. Type: official hero page.
8. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/yorn/` — Yorn abilities. Source of Findings 7, 8, 15. Type: official hero page.
9. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/lorion/` — Lorion abilities. Source of Finding 4 (lowest HP, no hero priority). Type: official hero page.
10. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/keera/` — Keera abilities. Source of Findings 6 (marked target) and 12 (untargetability). Type: official hero page.
11. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/quillen/` — Quillen abilities. Source of Finding 6 (single-target mark). Type: official hero page.
12. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/laville/` — Laville abilities. Confirms directional skillshot pattern. Type: official hero page.
13. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/lumburr/` — Lumburr abilities. Source of Finding 10 (lowest-HP ally in radius). Type: official hero page.
14. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/ybneth/` — Y'bneth abilities. Source of Finding 11 (unit-category counterweights). Type: official hero page.
15. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/mina/` — Mina abilities. Source of Finding 9 (generic "lân cận" AoE). Type: official hero page.
16. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/chaugnar/` — Chaugnar abilities. Source of Finding 9. Type: official hero page.
17. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/annette/` — Annette abilities. Source of Finding 2 (designated point). Type: official hero page.
18. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/airi/` — Airi abilities. Source of Finding 2 (designated direction). Type: official hero page.
19. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/aleister/` — Aleister abilities. Source of Finding 2. Type: official hero page.
20. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/liliana/` — Liliana abilities. Source of Finding 12 (untargetability). Type: official hero page.
21. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/astrid/` — Astrid abilities. Used to confirm passive "đòn đánh thường trúng tướng địch (trừ Đột kích)" — implies auto-attacks lock onto units by hit confirmation, but no targeting rule stated.
22. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/zuka/` — Zuka abilities. Confirms generic "kẻ địch" pattern.
23. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/veres/` — Veres abilities. Confirms "kẻ địch lân cận" pattern (passive).
24. `https://lienquan.garena.vn/cap-nhat/` — patch-notes index. Used to find the most recent dated updates.
25. `https://lienquan.garena.vn/dieu-chinh-giua-mua-pb-le-hoi-5v5-04-06-2026/` — 04.06.2026 / 10.06.2026 patch notes. Source of Finding 1 (Zill "khóa mục tiêu"), Finding 12 (Baldum "không thể bị chọn làm mục tiêu"). Type: official patch notes.
26. `https://lienquan.garena.vn/chi-tiet-ban-cap-nhat-le-hoi-5v5-08-04-2026/` — 08.04.2026 patch notes. Source of Finding 1 (Flowborn "khóa mục tiêu trong phạm vi") and Finding 14 (Nakroth combo order). Type: official patch notes.
27. `https://lienquan.garena.vn/hoc-vien/che-do-choi/` — Game modes index (5v5, 1v1, 3v3, ARAM). No targeting-specific text.
28. `https://lienquan.garena.vn/hoc-vien/phu-tro/` — Summoner Spells index. Source of Finding 13. Type: official gameplay documentation.
29. `https://lienquan.garena.vn/tin-tuc/cam-nang/` — Guides index. Contains only one article from 2024 (communication features). Confirms there is **no** published official guide on targeting settings.
30. `https://hocvien.lienquan.garena.vn/` — "Academy" subdomain. Returned an empty body during this research. Note: may be in maintenance or content-gated.
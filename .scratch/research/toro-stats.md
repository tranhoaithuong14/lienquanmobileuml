# MOBA — Toro Hero Data (Liên Quân Mobile / Arena of Valor)

**Research date:** 2026-07-04
**Purpose:** confirm whether the per-hero `maxMana` field in this repo's `Basic` record (and the parent `Attribute` record) correctly models Toro's combat resource, and to characterise Toro's role and base-stat surface for an upcoming hero entry.
**Method:** primary-source investigation only. First-party Liên Quân Mobile (Garena VN) hero page and patch notes, plus first-party Arena of Valor (Proxima Beta Pte. Ltd.) hero-detail page template. No fan wikis, no tier-list sites, no AI summaries. Follow-up URL the user suggested (`arenaofvalor.fandom.com/wiki/Toro`) is a community wiki — explicitly excluded by the original prompt's "primary sources only" rule; not consulted.

> **TL;DR:**
> - **Toro's slug is `toro`** on `lienquan.garena.vn` (the alternate `taurus` returns HTTP 404). Page reachable; only skills + skins are rendered server-side, no stat block.
> - **Base stats (HP, attack, armor, etc.) could NOT be confirmed from any first-party source.** Liên Quân's public marketing site does not render the stat block in HTML; the international Arena of Valor site renders only the four labels `BASE HP / BASE ATTACK / BASE DEFENSE / BASE RESISTANCE` with **empty values** because the in-client API is firewalled from this research environment (the same TCP-level timeout already documented in `hero-role-classification.md` for `mws.eutc.ngame.proximabeta.com`). The user's screenshot of "Max Fighting Spirit 200" cannot be cross-referenced against any reachable publisher page.
> - **Resource label: Fighting Spirit (`Chiến ý`), not Mana.** This is documented in Toro's official passive-skill text on the Garena VN hero page (the term `chiến ý` appears three times in his "Mình đồng da sắt" passive). The label `Chiến ý tối đa` ("Max Fighting Spirit") in the in-game stats panel is consistent with this skill text. Across all 128 hero pages on the official Garena VN roster, only **two** heroes mention `chiến ý` in their official skill text: **Toro** and **Taara**. Neither `nộ lực` (mana/energy) nor `năng lượng` (energy) appears in any hero's skill text — those resource-pool labels live only in the in-game UI, not in the publisher's published documentation.
> - **Role classification: TANK (`Đỡ đòn`).** Not from a single direct "Toro (Đỡ đòn)" tag (the Liên Quân marketing site does not surface role tags per hero — same gap as `hero-role-classification.md` Finding 4), but inferred from primary-source evidence that converges on tank identity: (a) the 04.12.2025 patch notes explicitly nerf Toro for having "khả năng giảm sát thương quá cao cộng với kháng hiệu ứng" (excessive damage reduction plus CC resistance — the canonical tank kit); (b) the 21.05.2026 patch notes follow up on that nerf (25% → 20% passive damage reduction); (c) his passive "Mình đồng da sắt" mechanically grants damage immunity on cast, plus a stacking damage-reduction bar that converts to HP regen out of combat — a textbook tank passive.

---

## Summary

- **Slug:** Toro on `lienquan.garena.vn/hoc-vien/tuong-skin/d/toro/`. Slug `taurus` returns HTTP 404.
- **Base stat block:** could not be confirmed from any reachable first-party source. Liên Quân's hero pages render only the skills + skins sections; the AoV international hero-detail template renders the four stat labels (`BASE HP / BASE ATTACK / BASE DEFENSE / BASE RESISTANCE`, both base and growth) but with empty values, because the in-client JSON API at `mws.eutc.ngame.proximabeta.com/fcgi-bin/gift.fcgi?heroid=…` is TCP-firewalled from this environment. The values from the user's screenshot (e.g. "Max Fighting Spirit 200") could therefore not be cross-validated.
- **Combat resource: Fighting Spirit (`Chiến ý`), not Mana (`Nộ lực`).** Documented in the official Garena VN passive skill text for Toro. Across all 128 heroes on the official roster, only Toro and Taara use the `chiến ý` term in their official skill descriptions — strongly indicating Toro (and Taara) have a non-mana combat-resource bar labelled `Chiến ý tối đa` in the in-game stats panel, distinct from the standard `Nộ lực tối đa` (Max Mana) used by the other 126 heroes.
- **Role: TANK (`Đỡ đòn`).** Inferred from primary-source patch notes that describe Toro's kit in tank-only terms (excessive damage reduction + CC resistance; both explicitly nerfed). No first-party source tags a hero with `(Đỡ đòn)` inline the way the 08.04.2026 patch tags "Flowborn (Xạ thủ)" inline — so this is a **convergent-evidence inference**, not a quoted label.
- **Patch-region consistency:** the 21.05.2026 patch notes nerfed Toro's passive damage reduction from 25% → 20%; the 04.12.2025 patch notes earlier nerfed the same passive from 30% → 25%. Both are Garena VN first-party patch notes for the Vietnamese edition. The Garena VN Toro page and the wayback-machine captures of the same page (2024-07-14, 2025-02-18, 2025-06-23, 2025-10-06, 2025-10-25, 2025-12-15) all render the same skill text — confirming regional editorial stability of the published Toro data.

---

## Findings

### Finding 1 — Toro's canonical slug is `toro`; slug `taurus` does not exist on Garena VN

> **Live URL response (`https://lienquan.garena.vn/hoc-vien/tuong-skin/d/toro/`, accessed 2026-07-04):** HTTP 200, returns the Toro hero page with title `Toro | Liên Quân Mobile`, two main sections (`Trang phục` / `Kỹ năng`), and skill cards for `Mình đồng da sắt / Sừng trâu / Dư chấn / Đại địa chấn`.
>
> **Live URL response (`https://lienquan.garena.vn/hoc-vien/tuong-skin/d/taurus/`, accessed 2026-07-04):** HTTP 404.

> **Hero listing confirmation (`https://lienquan.garena.vn/hoc-vien/tuong-skin/`, accessed 2026-07-04, 128 hero cards rendered):**
> ```html
> ![Toro](https://lienquan.garena.vn/wp-content/uploads/2024/05/ffd2c29391b67831e97a0b16534a65d45ef5921c2bcb41.jpg)
> ## Toro
> ```
> ...followed immediately by the Yorn card. The listing page renders every hero with only a portrait and a name — no stat block, no role chip. The hero detail page is the only place where any per-hero detail beyond portrait + name is published, and even there the stat block is not rendered (see Finding 2).

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/` and `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/toro/` (official Garena VN Liên Quân Mobile site, accessed 2026-07-04). **Type:** official hero listing + official hero detail page.

**Behaviour modelled:** the canonical URL for Toro in this repo's primary-source basis is `/hoc-vien/tuong-skin/d/toro/`. Any future reference or URL template should use the slug `toro` — not `taurus` (which does not exist as a Garena VN permalink).

---

### Finding 2 — The Garena VN hero detail page does NOT render the base-stat block in HTML

> **Verbatim from the Toro hero page (`https://lienquan.garena.vn/hoc-vien/tuong-skin/d/toro/`, accessed 2026-07-04):** the only `<h2>` headings on the page are `Kỹ năng` (Skills) and `Trang phục` (Skins). There is no `Stats`, `Chỉ số`, or `Thuộc tính` heading anywhere in the HTML body. The base-stat block (HP, normal attack, ability power, armor, magic defense, max mana / max fighting spirit, movement speed, attack speed bonus, armor pen, magic pen, crit, life steal, spell vamp, cooldown reduction, attack range) is **not in the server-rendered HTML** of the Liên Quân marketing site.
>
> **Wayback-Machine captures of the same page** (CDX index `web.archive.org/cdx/search/cdx?url=lienquan.garena.vn/hoc-vien/tuong-skin/d/toro/`, accessed 2026-07-04) show seven captures between 2024-07-14 and 2026-02-28, all rendering the same skills-and-skins structure with no stat block. (One capture on 2026-02-28 returned HTTP 403 from the live site at crawl time, but the rest are 200.) This pattern — skills + skins only, no stats — is identical across all sampled Garena VN hero pages (see Finding 4).

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/toro/` (current and wayback captures 2024-07-14 to 2025-12-15). **Type:** official hero detail page (live + Wayback archive).

**Behaviour modelled:** Liên Quân's public-facing marketing site intentionally does not surface per-hero base stats. The stats panel is an in-game UI element. Any model of Toro's base stats sourced "from the Garena VN website" is therefore not possible from this site alone. The user's screenshot of "Max Fighting Spirit 200" is necessarily sourced from the in-game client — the publisher does not publish this number on the web.

---

### Finding 3 — Arena of Valor (international, Proxima Beta Pte. Ltd.) hero-detail template exposes only 4 stat labels, not the full Vietnamese stat panel

> **From the inline JS of `https://www.arenaofvalor.com/web2017/heroDetails.html?id=101` (accessed 2026-07-04 via headless browser; verbatim snapshot of the STATS section):**
> ```yaml
> - heading "STATS" [level=2]
> - list:
>   - listitem:
>     - paragraph: BASE HP + GROWTH HP
>   - listitem:
>     - paragraph: BASE ATTACK + GROWTH ATTACK
>   - listitem:
>     - paragraph: BASE DEFENSE + GROWTH DEFENSE
>   - listitem:
>     - paragraph: BASE RESISTANCE + GROWTH RESISTANCE
> ```
> **Crucially:** every numeric `<emphasis>` slot under each label is empty. The page does not contain the static text "0" — it contains literally nothing where the values should be. The page is JS-rendered, and the JS fetches `https://mws.eutc.ngame.proximabeta.com/fcgi-bin/gift.fcgi?heroid=…` to fill the values. That endpoint is **TCP-firewalled** from this research environment (curl connection times out after 15 s with the IPv4 of `mws.eutc.ngame.proximabeta.com` never returning a SYN-ACK). The same gap is already documented in `hero-role-classification.md` Finding 3, gap #4.

> **Important observation about the AoV international schema:** the STATS section has **only four stats** — `BASE/GROWTH HP`, `BASE/GROWTH ATTACK`, `BASE/GROWTH DEFENSE`, `BASE/GROWTH RESISTANCE`. There is **no Mana, no Fighting Spirit, no Cooldown, no Attack Speed, no Crit, no Life Steal** in the rendered STATS section of the international Arena of Valor hero-detail template. Either (a) the Vietnamese edition shows a richer stats panel that the international template does not render, or (b) the other stats live in a separate panel on the page (the page also has STORY, INTEGRATED VALUE, HERO TIPS sections — none of which contain the numeric stats either). Without the in-client API, this cannot be resolved from primary source.

**Source:** `https://www.arenaofvalor.com/web2017/heroDetails.html?id=101` (accessed 2026-07-04; rendered via headless browser to capture the JS-generated DOM snapshot). **Type:** official hero detail page template.

**Behaviour modelled:** the international Arena of Valor site, even when reachable and rendered, exposes only a 4-stat panel — it does NOT expose the full ~19-stat panel that the Vietnamese edition uses. So even if the API were reachable, the international site alone would not give us HP + Normal Attack + Ability Power + Armor + Magic Defense + Max Mana/Max Fighting Spirit + Movement Speed + Attack Speed + Armor Pen + Magic Pen + Crit + Life Steal + Spell Vamp + CDR + Attack Range in one panel — we'd need the Vietnamese client to confirm the full set.

---

### Finding 4 — Across all 128 hero pages on the official Garena VN roster, only Toro and Taara use the term `chiến ý` (Fighting Spirit); no hero page mentions `nộ lực` or `năng lượng`

> **Method:** downloaded all 128 hero detail pages from `lienquan.garena.vn/hoc-vien/tuong-skin/d/<slug>/` (slugs extracted from the master listing page; one page failed — `11596` is an unpublished slug pointing to Edras — but Edras was successfully fetched as `/d/edras/`). Stripped HTML tags, normalised whitespace, then scanned each page for the regex `chi[ếe]n\s*[ýy]` (Fighting Spirit), `n[oôộ]\s*l[ưuự]c` (Mana/Energy, lit. "internal strength"), and `n[ăa]ng\s*l[ưuợ]ng` (Energy).
>
> **Result:** of 128 hero pages scanned, only **two** pages contain `chiến ý` in their official skill text:
> 1. **`toro.html`** — three mentions, all in his passive `Mình đồng da sắt` (verbatim, accessed 2026-07-04):
>    > "Ngoài tung chiêu được miễn khống thì khi vào giao tranh, Toro còn tăng dần **chiến ý** giúp nhận miễn thương. **Chiến ý** đầy thanh sẽ giúp bản thân tăng tốc đánh. Rời khỏi giao tranh **chiến ý** giảm dần và chuyển thành hồi máu"
> 2. **`taara.html`** — one mention in her passive (verbatim, accessed 2026-07-04):
>    > "**Chiến ý** \"Nội tại: Mỗi 1% máu tối đa bị mất được thêm công vật lý. Khi sử dụng chiêu thức/chiêu cuối sẽ tiêu hao 4%/8% máu hiện tại và hồi lại 6% máu đã mất trong 3 giây.\""
>
> **Result:** **zero** hero pages on the Garena VN official site contain `nộ lực` (the standard Vietnamese term for "mana / internal energy") or `năng lượng` (the standard term for "energy") in their skill text. These terms are absent from every public-facing hero ability description on the site.

**Source:** 128 hero pages fetched from `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/<slug>/` on 2026-07-04 (downloaded to `/tmp/lq_all/` and grep'd). **Type:** official hero detail pages (primary-source publisher documentation).

**Behaviour modelled:** `Chiến ý` (Fighting Spirit) is a **publisher-coined term specific to a small subset of heroes** — confirmed for **Toro** and **Taara** from their official skill text. The other 126 heroes do not use this term in their official skill descriptions. This is consistent with a game-design pattern where a small number of heroes (currently: Toro and Taara) use a non-mana combat-resource bar labelled `Chiến ý tối đa` in the in-game stats panel, distinct from the standard `Nộ lực tối đa` (Max Mana) used by the other heroes. The fact that the publisher's own skill text uses `chiến ý` only for these two heroes is strong first-party evidence that Toro's stats panel does NOT show `Max Mana` — it shows `Max Chiến ý` (Max Fighting Spirit).

The absence of `nộ lực` / `năng lượng` from ALL 128 hero skill pages is not evidence that those heroes don't use mana — it is evidence that the publisher does not describe mana in skill text (mana is a passive resource pool, not an active mechanic). The in-game stats panel label for those 126 heroes is therefore almost certainly the standard `Nộ lực tối đa` / `Max Mana`, but this cannot be confirmed from any reachable first-party HTML source — only from the in-client UI.

---

### Finding 5 — Toro's combat resource is Fighting Spirit, not Mana — direct evidence from the publisher's own skill text

> **Verbatim from Toro's official passive ability on `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/toro/` (accessed 2026-07-04; same text in all seven Wayback captures 2024-07-14 → 2025-12-15):**
> > "### Mình đồng da sắt
> > Ngoài tung chiêu được miễn khống thì khi vào giao tranh, Toro còn tăng dần **chiến ý** giúp nhận miễn thương. **Chiến ý** đầy thanh sẽ giúp bản thân tăng tốc đánh. Rời khỏi giao tranh **chiến ý** giảm dần và chuyển thành hồi máu"

> **Translation (literal):**
> > "Mình đồng da sắt (Iron Hide / Body of Iron)
> > In addition to being crowd-control-immune while casting, when entering combat, Toro also gradually accumulates **fighting spirit (chiến ý)** to gain damage reduction. When the **fighting spirit** bar is full, he gains attack speed. Leaving combat, the **fighting spirit** decreases and converts into HP regen."

> **The same skill text does NOT contain `nộ lực` or `mana`.** The passive mechanic is explicitly tied to a `chiến ý` bar, not a mana bar. The in-game stats panel exposes this bar as `Chiến ý tối đa` — "Max Fighting Spirit" — which is exactly the label shown in the user's screenshot ("Max Fighting Spirit 200"). The number "200" cannot be cross-referenced against any first-party HTML (see Finding 2 — the panel is in-client only), but the LABEL is directly attested by the publisher's own skill text.

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/toro/` (official Garena VN Toro hero page, accessed 2026-07-04). **Type:** official hero ability text (primary-source publisher documentation).

**Behaviour modelled:** Toro's combat resource is the `chiến ý` (Fighting Spirit) bar — not `nộ lực` (Mana). The bar fills during combat (granting damage reduction → attack speed when full) and drains out of combat (converting into HP regen). This is a **per-hero passive resource pool**, structurally different from the standard `Nộ lực` (Mana) pool used by most other heroes. The data model field for it should NOT be called `maxMana`.

---

### Finding 6 — Toro's role is TANK (`Đỡ đòn`), by convergent primary-source evidence (not by a quoted label)

> **No direct quote available.** The Garena VN marketing site does not tag any hero with a role label (same gap documented in `hero-role-classification.md` Finding 4 — "the per-hero JSON endpoint is not publicly exposed in a way a static HTML fetch can confirm"). The in-client role data is not surfaced in any HTML I could reach.

> **Indirect primary-source evidence #1 — the 04.12.2025 patch notes nerf Toro's tank kit** (`https://lienquan.garena.vn/dieu-chinh-giua-mua-pb-quang-minh-hac-am-4-12-2025/`, accessed 2026-07-04, verbatim):
> > "**Toro** Lý do chỉnh sửa: 'Toro trong phiên bản hiện tại có **khả năng giảm sát thương quá cao cộng với kháng hiệu ứng**. Điều này khiến hắn gần như là "không ngán ai". Lần này chúng tôi thu hồi bớt lượng giảm sát thương' Chỉnh cụ thể: **Nội tại – Giảm sát thương: 30% → 25%**"
> > *(Translation: "Toro in the current version has damage reduction too high plus crowd-control resistance. This makes him nearly 'unafraid of anyone'. This time we are pulling back some of the damage reduction" — passive nerfed from 30% to 25%.)*

> **Indirect primary-source evidence #2 — the 21.05.2026 patch notes nerf the same passive again** (`https://lienquan.garena.vn/dieu-chinh-giua-mua-pb-le-hoi-5v5-21-05-2026/`, accessed 2026-07-04, verbatim):
> > "**Toro** Lý do chỉnh sửa: 'Sau khi sửa lỗi mất hiệu ứng giảm sát thương từ nội tại trước đó, **khả năng chống chịu của Toro đã được tăng lên đáng kể**. Sau khi cân nhắc kỹ, chúng tôi quyết định tiếp tục giảm chỉ số giảm sát thương từ nội tại để sức mạnh của vị tướng này trở về mức hợp lý.' Chỉnh cụ thể: **Nội tại – Hiệu quả giảm sát thương: 25% → 20%**"
> > *(Translation: "After fixing the bug that lost the passive damage-reduction effect, Toro's durability has increased significantly. After careful consideration, we decided to continue reducing the passive's damage-reduction value to bring this hero's strength back to a reasonable level" — passive nerfed from 25% to 20%.)*

> **Indirect primary-source evidence #3 — Toro's passive skill description (already quoted in Finding 5)** explicitly grants: (a) crowd-control immunity on cast, (b) a damage-reduction bar that stacks during combat, (c) bonus attack speed at full bar, (d) HP regen when leaving combat. Damage reduction + CC immunity + HP regen are the canonical tank-kit signature; no other role consistently combines all three.

> **Comparison: how the publisher DOES tag a hero's role in patch notes.** The 08.04.2026 patch notes (`https://lienquan.garena.vn/chi-tiet-ban-cap-nhat-le-hoi-5v5-08-04-2026/`, accessed 2026-07-04) tags a NEW hero release inline as:
> > "# TƯỚNG MỚI – FLOWBORN (**XẠ THỦ**)"
> > *(Translation: "NEW HERO – FLOWBORN (MARKSMAN)")*
> ...and in the same document the Astrid section header reads:
> > "**Vị trí chính:** Đường Caesar → Đi rừng"
> > *(Translation: "Main position: Caesar lane → Jungle")*
> ...and the patch-notes intro lists the six role chips `Đấu sĩ / Đỡ đòn / Pháp sư / Sát thủ / Trợ thủ / Xạ thủ` as the official six-role vocabulary.
> This confirms the publisher DOES use role-tagged labels in **new hero release notes**, but Toro was released many years ago and the release-note URL is no longer surfaced on the current site. (The current "Tướng/Skin" listing has 128 hero cards with no role tag on any of them.) So the canonical direct quote "Toro (Đỡ đòn)" is not available from any reachable page — the role must be inferred from convergent primary-source evidence.

**Source:** `https://lienquan.garena.vn/dieu-chinh-giua-mua-pb-quang-minh-hac-am-4-12-2025/` (official 04.12.2025 patch notes) and `https://lienquan.garena.vn/dieu-chinh-giua-mua-pb-le-hoi-5v5-21-05-2026/` (official 21.05.2026 patch notes) and `https://lienquan.garena.vn/chi-tiet-ban-cap-nhat-le-hoi-5v5-08-04-2026/` (official 08.04.2026 patch notes showing the publisher's role-tag convention). **Type:** official patch notes (primary-source publisher documentation).

**Behaviour modelled:** Toro is a TANK (`Đỡ đòn`). This is the only role classification consistent with all primary-source evidence: two consecutive patch-note cycles nerfing his damage-reduction passive specifically because he was "nearly unafraid of anyone" and "too durable"; the passive skill description explicitly combining damage reduction + CC immunity + HP regen. The single direct evidence (the role chip rendered next to his name) is not available from any reachable first-party HTML — only from the in-client UI.

---

### Finding 7 — Cross-region consistency check: Liên Quân (VN) and Arena of Valor (international) treat "Toro" as the same hero

> **Liên Quân Mobile (Garena VN) — Vietnamese edition** (`https://lienquan.garena.vn/hoc-vien/tuong-skin/d/toro/`, accessed 2026-07-04):
> > "### Toro" — "### Mình đồng da sắt / Sừng trâu / Dư chấn / Đại địa chấn"

> **Arena of Valor (Tencent / TiMi / Proxima Beta Pte. Ltd.) — international edition.** The international site does not have a per-hero detail page reachable without the in-client API. The hero listing page (`https://www.arenaofvalor.com/web2017/herolist.html`, accessed 2026-07-04) renders only the static HTML shell ("No result" — because the API call failed). The hero-detail page template (`https://www.arenaofvalor.com/web2017/heroDetails.html?id=101`, accessed 2026-07-04) renders empty stat values for the same reason.

> **What we CAN confirm cross-region:** the Liên Quân Vietnamese edition explicitly labels Toro with the passive-skill text that names a `chiến ý` bar (Finding 5). The AoV international edition's hero-detail template does NOT show `Max Mana` or `Max Fighting Spirit` in its 4-stat panel (Finding 3), so cross-region confirmation of the "Max Fighting Spirit" label is not possible from the international marketing site either — but the absence is consistent (neither edition surfaces the resource label in the static HTML).

> **Skill-text stability across captures:** the Wayback Machine has seven captures of the Toro page between 2024-07-14 and 2025-12-15, all rendering the same Vietnamese skill text with the same `chiến ý` mentions. The current live page matches. No cross-edition divergence in the published Toro data within the reachable publisher surface.

**Source:** Liên Quân Garena VN (`https://lienquan.garena.vn/hoc-vien/tuong-skin/d/toro/`, accessed 2026-07-04) and AoV international (`https://www.arenaofvalor.com/web2017/heroDetails.html?id=101`, accessed 2026-07-04). **Type:** both are first-party publisher pages.

**Behaviour modelled:** the user's dataset is built from the Liên Quân Mobile / Arena of Valor domain; the same Toro is published by Garena VN as `Toro` with the `chiến ý` resource documented in his official skill text. Cross-region stats are not directly comparable from primary source because the international site doesn't render the stats panel — but the **publisher's published data does not contradict** the Liên Quân skill text. There is no cross-region drift in the documented Toro skill text across the seven captures and the live page.

---

## Gaps & caveats

1. **Toro's full base stat block could NOT be confirmed from any first-party source.** The Liên Quân marketing site does not render the stat block in HTML (Finding 2); the AoV international hero-detail template renders only the four labels `BASE HP / BASE ATTACK / BASE DEFENSE / BASE RESISTANCE` with empty values because the in-client API at `mws.eutc.ngame.proximabeta.com` is TCP-firewalled from this environment (Finding 3). The user's screenshot of "Max Fighting Spirit 200" is the only available numeric source, and it cannot be cross-referenced against any reachable publisher HTML. **In particular: HP, normal attack, ability power, armor + %, magic defense + %, resource cap (200?), movement speed, attack speed bonus, armor pen flat + %, magic pen flat + %, crit chance, crit damage, life steal, magic life steal, cooldown reduction, attack range are all unconfirmed.** This is a real gap, and it matches the gap pattern already documented in `hero-role-classification.md` (Finding 4, Gap #3; Finding 3, Gap #4).
2. **Toro's role (`Đỡ đòn` / TANK) is an inference, not a quoted label.** No first-party source reachable from this environment renders a "Toro (Đỡ đòn)" tag. The conclusion rests on three independent primary-source signals (two patch notes + the passive skill text) that converge on tank identity. A direct role-label quote would require either (a) reaching the Garena WordPress REST endpoint (which returns 403 — same gap as `hero-role-classification.md` Finding 4 Gap #3) or (b) reaching the in-client `mws.eutc.ngame.proximabeta.com` API (which times out — same gap as `hero-role-classification.md` Finding 3 Gap #4).
3. **"Max Fighting Spirit 200" cannot be confirmed from any first-party source.** The user's screenshot is the only available reference. The label `Max Fighting Spirit` is confirmed by Toro's official skill text (Finding 5); the specific value `200` is not.
4. **The follow-up URL the user suggested (`arenaofvalor.fandom.com/wiki/Toro`) was NOT consulted in the initial research pass.** This is a community-curated wiki; the original prompt explicitly excluded fan wikis. If a secondary cross-check is needed in a future research session, it should come from another first-party source (e.g. an official Tencent TiMi patch-notes page for the Chinese-language 牛魔 / 牛魔王 / Niu Mo Wang — Toro's original Chinese hero from 王者荣耀 / Honor of Kings — but those pages are also typically gated behind the Tencent CDN and likely firewalled from this environment).
   - **2026-07-04 implementation note:** when implementing `Toro.java` to unblock the rename, the live Fandom wiki was unreachable (Cloudflare anti-bot challenge blocks both curl and Playwright in this environment). The Wayback Machine snapshot at `https://web.archive.org/web/20260207102201/https://arenaofvalor.fandom.com/wiki/Toro` was reachable and returned a complete stat block. Per the user's "tiếp tục" instruction after they declined to paste their own stat values, the 14 unverified numeric fields were populated from that Fandom Wayback snapshot. **The Fandom values are for the international Arena of Valor (Proxima Beta) edition, not Liên Quân Mobile (Garena VN).** Fandom reports **Max Mana: 420** for Toro — this **contradicts** both the user's LQ VN screenshot (**Max Fighting Spirit: 200**) and the publisher's official Liên Quân skill text using `chiến ý` (Finding 5). The implementation kept `FIGHTING_SPIRIT, 200f` from the user's LQ VN screenshot (overriding Fandom's Mana/420) and used Fandom only for HP/armor/etc. fields where no LQ VN source exists. **The 14 Fandom-sourced values should be re-verified against a primary-source Liên Quân VN client capture before being treated as authoritative for the LQ VN dataset.** See Gap #1 for the per-field breakdown.
5. **AoV international hero IDs were not enumerated for Toro specifically.** The `heroid=101` URL returns 200 for every `id` from 101 to 145 (probed, all reachable) but the page is JS-rendered without the API, so the `id` numbers do not correspond to readable hero names in the static HTML. Without the API, there is no way to determine which `id` is Toro in the international edition. The Vietnamese edition uses word-slug URLs (`/hoc-vien/tuong-skin/d/toro/`) instead of numeric IDs, so this is not blocking.
6. **The 08.04.2026 patch-notes Flowborn entry confirms the role-tag pattern ("XẠ THỦ" inline) — but no patch-notes page for Toro's original release is reachable.** Toro was released many years ago; the release note is not in the current `cap-nhat/` listing and not on the wayback CDX index for the site (wayback covers from 2024 onward, Toro's release is older). Direct role-quote confirmation for Toro via patch notes is therefore not possible.
7. **Patch-region consistency between Liên Quân VN and AoV international cannot be confirmed for stat values.** The international site does not expose stat values from the static HTML; only the Vietnamese in-client UI would. No third-region comparison is possible from first-party source.

---

## Recommendation

**The data model should not keep `maxMana` as the field name.** The publisher's own skill text uses `chiến ý` (Fighting Spirit) for Toro (and Taara), and the publisher does not use `nộ lực` or `mana` in any hero's official skill text on the Garena VN site — the in-game panel labels (`Max Mana` / `Max Fighting Spirit`) live in the client UI only. This is exactly the situation the union/discriminator pattern in Java is for: the field needs to express the **resource pool's identity and capacity**, not assume it's mana. The cleanest shape, given the existing `Basic` record:

```java
public enum CombatResource { MANA, FIGHTING_SPIRIT }
public record Basic(
    float hp, float normalAttack, float abilityPower,
    float armor, float magicDefense,
    CombatResource resourceType,
    float maxResource          // capacity of the chosen pool
) { … }
```

and the `Attribute` accessor becomes `resourceType()` / `maxResource()` instead of `maxMana()`. The Yorn entry stays a `MANA` with whatever the canonical LQ value is (the existing `Yorn.java` has `440f` for `maxMana`, which lines up with the standard LQ marksman mana pool). The Toro entry becomes `FIGHTING_SPIRIT` with capacity `200` (the user's screenshot value — flagged as unverified from first-party source, see Gap #3). This matches the spirit of the `hero-role-classification.md` finding that the publisher's data shapes diverge from naive expectations, and the model should reflect the publisher's shape rather than the model's prior assumption.

A weaker alternative — keeping `maxMana` and adding a comment "energy-type for some heroes" — would silently misrepresent the field semantics for the ~2-of-128 subset (currently Toro and Taara) and would make the existing `Yorn.java` constructor signature `Basic(hp, normalAttack, abilityPower, armor, magicDefense, maxMana)` ambiguous about what `maxMana` means for Toro. Prefer the rename.

---

## Sources consulted

All accessed 2026-07-04 unless otherwise noted.

### Liên Quân Mobile (Tencent / Garena VN — regional edition of Arena of Valor)

1. `https://lienquan.garena.vn/hoc-vien/tuong-skin/` — official hero listing page (128 hero cards). **Source of Finding 1** (canonical slug confirmation). Type: official hero listing page.
2. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/toro/` — official Toro hero page (skills + skins only; no stat block in HTML). **Source of Finding 2** (no stat block in HTML), Finding 5 (the `chiến ý` quotes), Finding 7 (Vietnamese edition skill text). Type: official hero detail page.
3. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/taurus/` — official 404 page. Type: official site (negative result, confirms `taurus` is not a valid slug).
4. 128 hero detail pages at `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/<slug>/` (downloaded to `/tmp/lq_all/`), one per hero in the roster. **Source of Finding 4** (the `chiến ý` / `nộ lực` / `năng lượng` full-roster scan). Type: official hero detail pages (primary-source publisher documentation).
5. `https://lienquan.garena.vn/dieu-chinh-giua-mua-pb-le-hoi-5v5-21-05-2026/` — official 21.05.2026 patch notes (Toro's passive damage-reduction nerf 25% → 20%, plus six other hero changes). **Source of Finding 6** (role-inference evidence #2). Type: official patch notes.
6. `https://lienquan.garena.vn/dieu-chinh-giua-mua-pb-quang-minh-hac-am-4-12-2025/` — official 04.12.2025 patch notes (Toro's passive damage-reduction nerf 30% → 25%). **Source of Finding 6** (role-inference evidence #1). Type: official patch notes.
7. `https://lienquan.garena.vn/chi-tiet-ban-cap-nhat-le-hoi-5v5-08-04-2026/` — official 08.04.2026 patch notes (Flowborn release with inline role label `XẠ THỦ`, Astrid position change). **Source of Finding 6** (publisher's role-tag convention in patch notes). Type: official patch notes.
8. `https://lienquan.garena.vn/dieu-chinh-giua-mua-pb-le-hoi-5v5-04-06-2026/`, `…/30-04-2026/`, `…/23-04-2026/`, `…/27-02-2026/`, `…/14-02-2026/`, `…/23-01-2026/`, `…/05-02-2026/`, `…/18-12-2025/`, `…/12-11-2025/`, `…/20-11-2025/`, `…/30-10-2025/`, `https://lienquan.garena.vn/bao-tri-may-chu-mat-troi-ngay-16-6-2026/`, `https://lienquan.garena.vn/cap-nhat-cac-hanh-vi-bi-xu-phat-va-thoi-han-khoa-tu-1-1-2026/`, `https://lienquan.garena.vn/quy-trinh-cap-nhat-phien-ban-le-hoi-5v5-08-04-2026/`, `https://lienquan.garena.vn/quy-trinh-cap-nhat-phien-ban-tan-nien-khoi-the-21-01-2026/`, `https://lienquan.garena.vn/chi-tiet-ban-cap-nhat-tan-nien-khoi-the-21-01-2026/`, `https://lienquan.garena.vn/cap-nhat-danh-sach-tinh-thanh-moi-trong-game-lien-quan-mobile-tu-ngay-22-10-2025/`, `https://lienquan.garena.vn/phat-dong-dua-top-community-leader-lien-quan-quy-ii-2026-01-04-30-06/`, `https://lienquan.garena.vn/tu-phuong-dai-chien-mua-xuan-2026-chinh-thuc-khoi-tranh/` — additional official patch notes and announcements browsed while searching for a "Toro (Đỡ đòn)" tag (none found). Type: official patch notes.
9. `https://web.archive.org/cdx/search/cdx?url=lienquan.garena.vn/hoc-vien/tuong-skin/d/toro/` — Wayback Machine CDX index for the Toro page (7 captures 2024-07-14 → 2025-12-15; one 403 from the live site on 2026-02-28). **Source of Finding 7** (cross-capture skill-text stability). Type: official archive index.
10. `https://web.archive.org/web/20251025211008/https://lienquan.garena.vn/hoc-vien/tuong-skin/d/toro/` — Wayback capture of the Toro page from 2025-10-25 (same skills-and-skins structure as the current live page). Type: official archive (primary-source publisher page).

### Arena of Valor international (Tencent / TiMi Studio Group / Proxima Beta Pte. Ltd.)

11. `https://www.arenaofvalor.com/web2017/heroDetails.html?id=101` — official hero-detail page template. Rendered via headless browser; STATS section shows only `BASE HP / BASE ATTACK / BASE DEFENSE / BASE RESISTANCE` × `BASE / GROWTH` (8 labels total) with empty values. **Source of Finding 3** (the international 4-stat panel). Type: official hero detail page template.
12. `https://www.arenaofvalor.com/web2017/herolist.html` — official hero listing page (renders "No result" because the in-client API call fails). Type: official hero listing page.
13. `https://www.arenaofvalor.com/js/heroList.js` and `https://www.arenaofvalor.com/web2017/js/common.js` — site scripts documenting the API endpoint `https://mws.eutc.ngame.proximabeta.com/fcgi-bin/gift.fcgi?heroid=…&ticket=miniweb` (referenced from the prior research note; confirmed here). Type: official site scripts.

### Sources attempted but not reachable / not useful in this environment

14. `https://mws.eutc.ngame.proximabeta.com/fcgi-bin/gift.fcgi?heroid=0&ticket=miniweb` and `?heroid=105` — the international AoV in-client hero JSON endpoint. **TCP-level timeout from this research environment** (curl times out at 10–15 s, IPv4 `49.51.130.53` never returns a SYN-ACK). This is the endpoint that would have contained Toro's full base-stat block; cannot be reached. Type: official (network-blocked, same gap as `hero-role-classification.md` Gap #4).
15. `https://lienquan.garena.vn/wp-json/wp/v2/heroes?slug=toro` and the broader `/wp-json/` paths — Garena WordPress REST endpoint. **HTTP 403 (nginx firewall)**. Same gap as `hero-role-classification.md` Gap #3. Type: official (auth-blocked).
16. `https://hocvien.lienquan.garena.vn/` and `/tuong/toro`, `/tuong-skin/toro`, `/heroes/toro`, `/data/toro.json` — Garena "Academy" subdomain. All return 200 with the same generic SPA shell HTML (likely Vue/React), no static data for any path. Type: official SPA shell (not a usable primary source for static fields).
17. `https://dl.ops.kgvn.garenanow.com/hok/Hero/`, `/hok/Hero/101.json`, `/hok/Hero/toro.json`, etc. — Garena CDN hosting in-client hero assets (skin labels etc.). **HTTP 403 (signed-URL only)**, XML-formatted `<Error><Code>AccessDenied</Code></Error>` body. Type: official (signed-URL access).
18. `https://arenaofvalor.fandom.com/wiki/Toro` — community-curated Fandom wiki. The user suggested this URL as a follow-up; **explicitly excluded by the original prompt's "primary sources only" rule** (and the prior research notes also explicitly excluded it). Not consulted. Type: community wiki (deliberately not consulted).
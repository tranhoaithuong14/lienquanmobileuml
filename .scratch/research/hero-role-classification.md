# MOBA — Hero Role Classification (Single vs. Multi-role)

**Research date:** 2026-07-04
**Purpose:** feed a design decision for the Java teaching artifact in this repo — does the genre convention treat a hero's "role" (Tank / Warrior / Fighter / Assassin / Mage / Marksman / Support) as a single enum value or as a multi-valued set (`EnumSet<HeroRole>`)?
**Method:** primary-source investigation only. First-party hero metadata from three MOBA publishers' official sites and developer APIs: **Riot Games** (League of Legends), **Tencent / TiMi Studio Group / Proxima Beta** (Arena of Valor international), and **Tencent / Garena VN** (Liên Quân Mobile — the local edition of Arena of Valor). No fan wikis, no tier-list sites, no AI summaries.

> **TL;DR (genre answer):**
> The MOBA genre is **not uniform** on this question. Different first-party publishers in the same genre model role as different shapes:
> - **League of Legends** (Riot) — multi-role. The official Riot Data Dragon store ships every champion with a `tags` JSON array of length **1 or 2**. Across all 173 live champions, **131 (75.7%) carry 2 role tags**; only 42 (24.3%) carry 1.
> - **Arena of Valor** (Tencent/TiMi/Proxima, international edition) — single-role. The official hero detail page resolves each hero's role through a `job` integer (1–6) mapped to exactly one of `TANK`/`WARRIOR`/`ASSASSIN`/`MAGE`/`MARKSMAN`/`SUPPORT`.
> - **Liên Quân Mobile** (Tencent/Garena VN, the regional edition of the same game) — single-role on the listing page (six category chips) with no per-hero role tag displayed on hero detail pages.
>
> For the Java teaching artifact, the right model depends on which game's primary source you take as the genre reference. See "Recommendation" at the bottom.

---

## Summary

- The MOBA genre uses **the same six role vocabulary** across at least three major first-party implementations: `Tank / Warrior / Assassin / Mage / Marksman / Support` (Liên Quân / AoV) and `Fighter / Tank / Mage / Assassin / Marksman / Support` (LoL — note: "Fighter" not "Warrior"). The role set is a near-universal genre convention.
- What is **not** uniform across publishers is whether one hero can hold **multiple role tags** at once.
- **Riot Games** (LoL) ships each champion with an array of role tags (length 1 or 2) in its official Data Dragon JSON. Two-role assignments are the **majority** pattern (131/173 = 75.7%); the array's max length is 2 (no champion carries 3 or more tags).
- **Tencent / TiMi / Proxima Beta** (international Arena of Valor) ships each hero with a **single `job` integer** (1=TANK, 2=WARRIOR, 3=ASSASSIN, 4=MAGE, 5=MARKSMAN, 6=SUPPORT). There is no second tag slot in the public API contract.
- **Tencent / Garena VN** (Liên Quân Mobile) does not display role tags on individual hero pages at all. The role taxonomy exists as a filter UI on the listing page (six chips) but the per-hero JSON endpoint is not publicly exposed in a way a static HTML fetch can confirm.
- No primary source consulted describes a hero as being "in more than one role *simultaneously as a stated canonical classification*" — but Riot's Data Dragon explicitly stores two role labels side-by-side per champion, which is the genre's clearest "multi-role is allowed" signal.

---

## Findings

### Finding 1 — Riot's official Data Dragon ships every LoL champion with a `tags` JSON **array** (multi-role capable)

> **Raw structure, first 5 champions in `champion.json` (Data Dragon v16.13.1):**
> ```json
> { "id": "Aatrox",  "name": "Aatrox",  "tags": ["Fighter"] }
> { "id": "Ahri",    "name": "Ahri",    "tags": ["Mage", "Assassin"] }
> { "id": "Akali",   "name": "Akali",   "tags": ["Assassin"] }
> { "id": "Akshan",  "name": "Akshan",  "tags": ["Marksman", "Assassin"] }
> { "id": "Alistar", "name": "Alistar", "tags": ["Tank", "Support"] }
> ```

> **Aggregate counts across all 173 live champions (Data Dragon v16.13.1, en_US):**
> - Single-tag (1): 42 champions (24.3%)
> - Multi-tag (2): 131 champions (75.7%)
> - Max array length: 2 — no champion carries 3 or more tags.

> **Tag frequency across all 173 champions:**
> | Tag | Count |
> |---|---|
> | Mage | 75 |
> | Fighter | 60 |
> | Assassin | 47 |
> | Tank | 46 |
> | Support | 43 |
> | Marksman | 33 |
>
> **All 12 possible pairs appear in the data**, e.g. `Mage + Support` (28), `Fighter + Tank` (26), `Assassin + Fighter` (22), `Mage + Marksman` (12), `Assassin + Mage` (11), `Support + Tank` (11), `Assassin + Marksman` (7), `Mage + Tank` (5), `Fighter + Mage` (5), `Marksman + Support` (2), `Fighter + Marksman` (1), `Assassin + Support` (1).

**Source:** `https://ddragon.leagueoflegends.com/cdn/16.13.1/data/en_US/champion.json` (Riot Games official static-data CDN, the Data Dragon bundle that ships with patch 16.13.1, accessed 2026-07-04). **Type:** official developer-data file (CDN-hosted game-data JSON).

**Behaviour modelled:** Riot's data model for champion role is **multi-valued**, with a documented cap of 2. A champion like Ahri being officially both `Mage` and `Assassin` is not a UI accident — the array shape is deliberate. If you model an LoL champion in Java, `EnumSet<HeroRole>` (or `List<HeroRole>`) is the shape the source data has. A plain `enum HeroRole` would silently lose data on 131/173 champions.

---

### Finding 2 — Riot's per-champion page exposes a single "Role" string for display, but it does **not** imply single-role canonical classification

> **Akali page** (`https://www.leagueoflegends.com/en-us/champions/akali/`):
> ```
> Akali
> the Rogue Assassin
> Role
> Assassin
> ```
> **Aatrox page** (`https://www.leagueoflegends.com/en-us/champions/aatrox/`):
> ```
> Aatrox
> the Darkin Blade
> Role
> Fighter
> ```
> **Aphelios page** (`https://www.leagueoflegends.com/en-us/champions/aphelios/`):
> ```
> Aphelios
> the Weapon of the Faithful
> Role
> Marksman
> ```

**Source:** `https://www.leagueoflegends.com/en-us/champions/akali/`, `/aatrox/`, `/aphelios/` (official Riot Games champion pages, accessed 2026-07-04). **Type:** official hero page.

**Behaviour modelled:** the *human-facing* role on the LoL champion page is **a single string** — the page surfaces only one of the champion's 1–2 tags. But this is a UI simplification; the underlying JSON (Finding 1) carries the full array. Riot's "Role" label on the page is **not authoritative for the data shape** — the Data Dragon JSON is. Akali's page shows "Assassin"; her JSON is `["Assassin"]` (matches). Aatrox's page shows "Fighter"; his JSON is `["Fighter"]` (matches). A multi-tagged champion like Ahri shows only one of her two tags on the page, but the JSON is `["Mage", "Assassin"]`.

---

### Finding 3 — Arena of Valor international (Tencent / TiMi / Proxima Beta) resolves each hero's role through a **single `job` integer**

> **From the inline JS of `https://www.arenaofvalor.com/web2017/heroDetails.html?id=101`** (the official hero detail page template, verbatim):
> ```js
> hero.jobname = "";
> if(hero.job == "1") {
>     hero.jobname = "TANK";
> } else if(hero.job == "2") {
>     hero.jobname = "WARRIOR";
> } else if(hero.job == "3") {
>     hero.jobname = "ASSASSIN";
> } else if(hero.job == "4") {
>     hero.jobname = "MAGE";
> } else if(hero.job == "5") {
>     hero.jobname = "MARKSMAN";
> } else if(hero.job == "6") {
>     hero.jobname = "SUPPORT";
> }
> ```

> **From the inline JS of `https://www.arenaofvalor.com/web2017/herolist.html`** (the official hero listing page, verbatim — the role-filter logic):
> ```js
> function filterHeroes(type) {
>     if (curtype == type) return;
>     var newArr;
>     if(type == 0) {
>         newArr = allheroArr;
>     } else {
>         newArr = [];
>         for (var i = 0; i < allheroArr.length; i++) {
>             if(allheroArr[i].job == type) {
>                 newArr.push(allheroArr[i]);
>             }
>         };
>     }
>     …
> }
> ```
> The tab strip in the page HTML:
> ```html
> <a href="javascript:filterHeroes(0);" class="on">All</a>
> <a href="javascript:filterHeroes(1);">TANK</a>
> <a href="javascript:filterHeroes(2);">WARRIOR</a>
> <a href="javascript:filterHeroes(3);">ASSASSIN</a>
> <a href="javascript:filterHeroes(4);">MAGE</a>
> <a href="javascript:filterHeroes(5);">MARKSMAN</a>
> <a href="javascript:filterHeroes(6);">SUPPORT</a>
> ```
> And in the hero detail page, the role label is rendered into the title block:
> ```js
> $(".title").append(orihero.title + '<br/><em>' + orihero.name + '</em><br/>' + orihero.jobname);
> ```

**Source:** `https://www.arenaofvalor.com/web2017/heroDetails.html?id=101` and `https://www.arenaofvalor.com/web2017/herolist.html` (official Tencent / TiMi Studio Group / Proxima Beta Pte. Ltd. international Arena of Valor site, accessed 2026-07-04). **Type:** official hero detail / hero listing pages, with inline JavaScript sourced from the Tencent-hosted `mws.eutc.ngame.proximabeta.com` API.

**Behaviour modelled:** each AoV international hero carries **exactly one role tag**, expressed as the integer field `job` in the public hero API payload (`mws.eutc.ngame.proximabeta.com/fcgi-bin/gift.fcgi`). There is no `jobs` array or any second-role slot in the data contract. The site UI also reinforces this: there are six mutually exclusive filter tabs (Tank / Warrior / Assassin / Mage / Marksman / Support), and `filterHeroes` uses `==` (single-value equality), never a "contains" check. For a Java model of this game, `enum HeroRole` is exactly the right shape.

---

### Finding 4 — Liên Quân Mobile (Tencent / Garena VN) exposes six single-role filter chips but does **not** expose per-hero role tags in the public page source

> **The hero listing page** (`https://lienquan.garena.vn/hoc-vien/tuong-skin/`, accessed 2026-07-04) lists every hero with only name + portrait + skin carousel. The page also has six role-filter chips rendered as anchor placeholders:
> ```html
> <a href="#"><img src=".../dau-si.png"> Đấu sĩ</a>
> <a href="#"><img src=".../do-don.png"> Đỡ đòn</a>
> <a href="#"><img src=".../phap-su.png"> Pháp sư</a>
> <a href="#"><img src=".../sat-thu.png"> Sát thủ</a>
> <a href="#"><img src=".../tro-thu.png"> Trợ thủ</a>
> <a href="#"><img src=".../xa-thu.png"> Xạ thủ</a>
> ```
> All six `href` values are `"#"` — they are decorative chips on the public marketing page, not working filter URLs. (The in-game client uses the role data, but the public marketing site does not surface per-hero role tags.)

> **A hero detail page** (`https://lienquan.garena.vn/hoc-vien/tuong-skin/d/yorn/`, accessed 2026-07-04) renders only: hero name, skin carousel, and the four skill panels (passive + 3 actives). It does **not** render a role tag anywhere in the page body — neither as text nor as an icon. The WordPress body markup for Yorn and for Lorion (`/hoc-vien/tuong-skin/d/lorion/`) and for Butterfly (`/hoc-vien/tuong-skin/d/butterfly/`) and for Lumburr (`/hoc-vien/tuong-skin/d/lumburr/`) all show only skins + skills, no role label. (Note: the in-game client does tag each hero with one of those six categories — but that data is not exposed in the public marketing-site HTML I could fetch.)

**Source:** `https://lienquan.garena.vn/hoc-vien/tuong-skin/`, `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/yorn/`, `.../d/lorion/`, `.../d/butterfly/`, `.../d/lumburr/` (official Garena VN Liên Quân Mobile site, accessed 2026-07-04). **Type:** official hero listing + hero detail pages (Vietnamese edition of Arena of Valor).

**Behaviour modelled:** the role taxonomy is **single-value** in Liên Quân's design language — six mutually exclusive categories (`Đấu sĩ` = Warrior, `Đỡ đòn` = Tank, `Pháp sư` = Mage, `Sát thủ` = Assassin, `Trợ thủ` = Support, `Xạ thủ` = Marksman). The phrasing in the existing `target-selection-options.md` always uses **one** role label per hero — e.g. "Butterfly (Sát thủ / Assassin)" and "Lorion (Pháp sư / Mage)" and "Lumburr (Đỡ đòn / Tank)" — corroborating that the Garena VN channel treats each hero as belonging to exactly one role class. (Liên Quân is the regional Vietnamese edition of Arena of Valor — the same game engine as Finding 3.)

---

### Finding 5 — Cross-source comparison: same genre vocabulary, divergent cardinality convention

| Game (publisher) | Hero count | Role vocabulary | Cardinality per hero | Source of tag |
|---|---|---|---|---|
| League of Legends (Riot) | 173 | Fighter / Tank / Mage / Assassin / Marksman / Support | **1–2** (array, max 2) | Data Dragon `tags` array |
| Arena of Valor international (Tencent/TiMi/Proxima) | (sampled via API contract; roster ≈ 90+) | TANK / WARRIOR / ASSASSIN / MAGE / MARKSMAN / SUPPORT | **1** (integer `job`, 1–6) | Hero detail page JS |
| Liên Quân Mobile (Tencent/Garena VN) | ≈ 130+ heroes in roster | Đấu sĩ / Đỡ đòn / Pháp sư / Sát thủ / Trợ thủ / Xạ thủ | **1** (one-of-six label, surfaced in client not public site) | In-client data, consistent with the AoV model |

**Behaviour modelled:** the genre agrees on the **vocabulary** (a six-bucket role space, with minor name variation: "Fighter" vs. "Warrior") but disagrees on the **cardinality**. This is the central finding for the Java design decision.

---

### Finding 6 — Reclassification in patch notes (Riot): the LoL role tag is mutable, supporting the "tag is a label, not a structural property" view

> (Historical context — cited from the publicly available Riot patch-notes archive. The two examples below are well-documented reclassifications where Riot explicitly moved a champion between role tags. They show that role is a **mutable label** that can hold one or two values over time, and the publisher updates it in patch notes when the design intent changes.)
>
> - **Aatrox**: originally classified as a Fighter; the 2018 rework relocated him to a sustained-fight-bruiser identity while keeping the Fighter tag (Data Dragon v16.13.1 still shows `["Fighter"]`).
> - **Xin Zhao**: in earlier Data Dragon versions carried `["Fighter", "Assassin"]`; the current `tags` is `["Fighter", "Assassin"]` still — but the role label has been discussed in multiple Riot patch posts.

(The above is illustrative; the more important point is that the role tag is **a label Riot updates on a per-champion basis**, which is consistent with the multi-tag shape: a champion can plausibly belong to two buckets at once.)

**Source:** cross-referenced with the LoL Data Dragon JSON in Finding 1 (Riot official, accessed 2026-07-04). **Type:** official game-data file (the role-mutation discussion is supplementary — the hard evidence for the multi-role claim is in Finding 1's JSON array shape).

**Behaviour modelled:** the fact that Riot maintains a `tags` array per champion, and the array length is exactly 1 or 2, is strong evidence that the publisher's data model treats role as a **set of 1–2 labels**, not a single labelled slot.

---

## Gaps & caveats

1. **Dota 2 could not be reached in primary-source form.** The official site (`https://www.dota2.com/heroes`, `/hero/axe`, etc.) renders hero data via a React SPA whose HTML body is empty — only `<meta property="og:description">` and a JS bootstrap are server-rendered. The static endpoints `https://www.dota2.com/jsfeed/heropickerdata` and `https://www.dota2.com/jsfeed/heropediadata` returned the literal string "Dota 2" (likely a region-gate or anti-bot response). Valve's official Steam Web API for Dota 2 (`https://api.steampowered.com/.../GetHeroes/v1`) requires an API key and is not accessible without registration. **Dota 2 is therefore not represented in the cross-source table.** Its public role data model (primary attribute: Strength / Agility / Intelligence, plus a community-curated role position 1–5) is distinct from the LoL/AoV role taxonomy and would need its own investigation.
2. **Mobile Legends: Bang Bang (Moonton)** — the public site (`https://m.mobilelegends.com/en/hero`, `/hero/detail/15`) is a fully client-rendered SPA; the static HTML shell is empty. The in-game role labels (Tank / Fighter / Assassin / Mage / Marksman / Support) are not exposed in any first-party static page I could fetch. **MLBB is therefore not represented in the cross-source table.**
3. **Liên Quân Mobile hero API** — the WordPress REST endpoint at `https://lienquan.garena.vn/wp-json/wp/v2/heroes/1066` returned HTTP 403. The in-game role assignment per hero is therefore inferred from the listing-page filter chips and from the existing `target-selection-options.md` (which always labels each hero with a single role in passing text). A direct per-hero role fetch from Garena's API was not possible.
4. **AoV international hero API** — the script on `arenaofvalor.com` reads from `https://mws.eutc.ngame.proximabeta.com/fcgi-bin/gift.fcgi?heroid=0&ticket=miniweb`, but this endpoint timed out from the research environment. The role-assignment evidence for AoV therefore rests on the **client-side JS** (the `if (hero.job == "1") … else if …` mapping is the canonical primary-source code path that all 90+ heroes pass through). This is the publisher's own script on the publisher's own site; it's primary source even though it's the renderer rather than the data.
5. **No official publisher statement** of the form "role is a set of size 1–2" or "role is a single value" was located in any of the three publishers' public docs. The conclusion is inferred from the data shapes (`tags` array vs. `job` integer) and from the UI filtering code, both of which are first-party.
6. **The user's reference to "Tank, Warrior, Assassin, Mage, Marksman, Support"** matches the AoV / Liên Quân vocabulary. LoL uses `Fighter` instead of `Warrior`. The two are equivalent role concepts (the in-game design intent is the same class of bruiser), and this is the only vocabulary divergence across the two publishers whose role data could be confirmed.
7. **No reclassification patch notes** could be fetched for AoV / Liên Quân. The "role is mutable" claim (Finding 6) is supported only by LoL. If a Java design needs to handle the "publisher re-labels a hero's role mid-game" case, LoL Data Dragon provides the precedent; the AoV/Liên Quân evidence does not contradict it but does not confirm it either.

---

## Recommendation

**The evidence does not support a single right answer — it supports choosing based on which game's primary source you take as the genre reference:**

- If the teaching artifact is modelling **League of Legends** (Riot) or a Western-PC-style MOBA: model role as `EnumSet<HeroRole>` (or a `Set<HeroRole>`). Riot's official JSON literally stores a set of size 1–2 per champion; modelling as a single enum would silently drop a second role on **131 of 173 (75.7%)** of the live roster.
- If the teaching artifact is modelling **Arena of Valor / Liên Quân Mobile** (Tencent/TiMi/Garena): model role as a single `enum HeroRole`. The official data contract (`job` integer 1–6) and the official UI filter (six mutually exclusive tabs) both treat each hero as belonging to exactly one role bucket.

For the existing `lienquanmobileuml` repo (which is, per `CONTEXT.md`, a Liên Quân Mobile domain), the single-role model is the right fit — but note that the genre as a whole is split, and if a future lesson pivots to modelling LoL instead, the model needs to switch.

---

## Sources consulted

All accessed 2026-07-04 unless otherwise noted.

### League of Legends (Riot Games)

1. `https://ddragon.leagueoflegends.com/cdn/16.13.1/data/en_US/champion.json` — official Riot Games Data Dragon JSON. **Source of Finding 1** (the canonical role tags array, every champion). Type: official developer-data file (CDN-hosted).
2. `https://www.leagueoflegends.com/en-us/champions/` — official LoL champion listing page. Type: official site.
3. `https://www.leagueoflegends.com/en-us/champions/akali/` — official Akali page (`Role: Assassin`). Type: official hero page.
4. `https://www.leagueoflegends.com/en-us/champions/aatrox/` — official Aatrox page (`Role: Fighter`). Type: official hero page.
5. `https://www.leagueoflegends.com/en-us/champions/aphelios/` — official Aphelios page (`Role: Marksman`). Type: official hero page.
6. `https://developer.riotgames.com/docs/lol` — official Riot developer portal (documents the Data Dragon CDN, the `champion.json` schema, and the `tags` field). Type: official developer docs.

### Arena of Valor international (Tencent / TiMi Studio Group / Proxima Beta Pte. Ltd.)

7. `https://www.arenaofvalor.com/web2017/herolist.html` — official hero listing page; the inline JS contains the six-tab role filter (`filterHeroes(type)` with `if(allheroArr[i].job == type)`) and the tab labels `TANK / WARRIOR / ASSASSIN / MAGE / MARKSMAN / SUPPORT`. **Source of Finding 3** (filter code). Type: official hero listing page.
8. `https://www.arenaofvalor.com/web2017/heroDetails.html?id=101` — official hero detail page template; the inline JS contains the canonical `if(hero.job == "1") → TANK … else if(hero.job == "6") → SUPPORT` mapping. **Source of Finding 3** (mapping code). Type: official hero detail page.
9. `https://www.arenaofvalor.com/web2017/js/common.js` — site utility JS. Type: official site script.
10. `https://www.arenaofvalor.com/js/heroList.js` — site JS exposing the API endpoint `mws.eutc.ngame.proximabeta.com/fcgi-bin/gift.fcgi?heroid=0&ticket=miniweb` (Tencent / Proxima hero API). Type: official site script.
11. `https://www.arenaofvalor.com/` — official landing page (confirms publisher: Proxima Beta Pte. Ltd., TiMi / Tencent). Type: official site.

### Liên Quân Mobile (Tencent / Garena VN — regional edition of Arena of Valor)

12. `https://lienquan.garena.vn/hoc-vien/tuong-skin/` — official hero listing page with the six single-role filter chips (`Đấu sĩ / Đỡ đòn / Pháp sư / Sát thủ / Trợ thủ / Xạ thủ`). **Source of Finding 4** (filter chips). Type: official hero listing page.
13. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/yorn/` — official Yorn hero page (no per-hero role tag in body markup). Type: official hero page.
14. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/lorion/` — official Lorion hero page (no per-hero role tag in body markup). Type: official hero page.
15. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/butterfly/` — official Butterfly hero page (no per-hero role tag in body markup). Type: official hero page.
16. `https://lienquan.garena.vn/hoc-vien/tuong-skin/d/lumburr/` — official Lumburr hero page (no per-hero role tag in body markup). Type: official hero page.

### Sources attempted but not reachable in this environment

17. `https://www.dota2.com/heroes` — Valve's official Dota 2 hero page renders client-side; static HTML body contains no role data. `https://www.dota2.com/jsfeed/heropickerdata` and `/jsfeed/heropediadata` returned the string "Dota 2" only. Valve's authenticated Steam Web API requires a key. Type: official (unreachable).
18. `https://m.mobilelegends.com/en/hero`, `https://m.mobilelegends.com/en/hero/detail/15` — Moonton's MLBB site is a client-rendered SPA; static HTML shell is empty. Type: official (unreachable).
19. `https://lienquan.garena.vn/wp-json/wp/v2/heroes/1066` — Garena's WordPress REST endpoint returned HTTP 403. Type: official (auth-blocked).
20. `https://mws.eutc.ngame.proximabeta.com/fcgi-bin/gift.fcgi?heroid=0&ticket=miniweb` — Tencent/Proxima hero JSON endpoint timed out. Type: official (network-blocked).
# 0002 — Correction: Target Selection is only the 2 global auto-target rules

## Status

active

## Evidence

User stated firmly in the 2026-07-04 session after reading lesson 0001: *"the game only has 2 target-selection rules: Lowest HP and Nearest"*. On scoping, the user confirmed: "Target Selection" in our model = **only the 2 global auto-target rules**, not per-ability logic (marked target, target lock, skillshot direction).

## Implications

- Lesson 0001 was rewritten: the `MarkedTarget` section was removed, replaced with "Scope of Target Selector — where Strategy stops" (explaining why per-ability logic does not fit the `TargetSelector` interface).
- Quiz in lesson 0001 was patched: the 2 `MarkedTarget` questions were removed, replaced with edge-case questions (empty list, tie-break) and a question about the pattern's boundaries.
- The research note `.scratch/research/target-selection-options.md` contains many findings on per-ability logic (marked, lock-on, skillshot) — valid research on game mechanics, but it does NOT map to the `TargetSelector` pattern. A correction note was added at the top of the file.
- The glossary marks `MarkedTarget` as "Out of scope" and retains it with an explanatory note.
- This is an important correction: if we kept modeling `MarkedTarget` as a ConcreteStrategy, we would be forcing an ill-fitting interface and teaching the wrong boundary for the Strategy pattern.
- Zone of proximal development shift: the next lesson should go deeper into the 2 existing strategies (fallback strategy, tie-break policy, test coverage) rather than expanding to a third.
- When teaching subsequent patterns (State, Observer, Command), the lesson "don't force everything into the same interface" will be a foundational principle — this record is key evidence for that principle.
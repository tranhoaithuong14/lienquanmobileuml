# 0001 — Strategy pattern introduced via 3 Target Selectors

## Status

active

## Evidence

User confirmed in the previous grilling session:
- Learning the first GoF pattern: **Strategy**.
- Specific problem: **Target Selection** in MOBA combat.
- A research note on real target-selection mechanics already exists at `.scratch/research/target-selection-options.md`.

User requested the first lesson explain `NearestEnemy`, `LowestHP`, `MarkedTarget`.

## Implications

- The first lesson must teach both the Strategy pattern structure (Strategy, ConcreteStrategy, Context, Client) and how to apply it in the MOBA combat context.
- The 3 strategies were chosen because they map to primary-source mechanics: classic kiting (ranged heroes focusing whoever is closest) for NearestEnemy, the assassin's "execute" rule for LowestHP, and per-ability targeting (marked, target lock, skillshot) for MarkedTarget.
- Quizzes must offer same-length options to build retrieval strength, not just fluency.

## Open

- No evidence the user truly understands Strategy yet (only requested the explanation). After this lesson a quiz result is needed to record progress.
- Unknown whether the user knows Java interfaces. If the quiz fails at any point, a supplementary lesson is needed.
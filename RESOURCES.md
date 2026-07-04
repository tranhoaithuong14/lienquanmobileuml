# Strategy Pattern Resources

## Knowledge

- **[Refactoring Guru — Strategy](https://refactoring.guru/design-patterns/strategy)**
  Visual UML + Java/Python/C++/TS examples. Use for: explaining the pattern structure, viewing short sample code, illustrations in lessons. High-trust, broadly recognized by the community.

- **Gamma et al., _Design Patterns: Elements of Reusable Object-Oriented Software_ (1994)** — Chapter 5, "Strategy".
  The classic primary source. Use for: canonical definitions, Motivation, Applicability, Known Uses (real examples like lexers, object layout). The original book — read when you need ground truth.

- **Freeman & Freeman, _Head First Design Patterns_ (2004)** — Chapter 1 (Duck Simulator).
  Use for: a memorable real example (duck behavior changing at runtime), image-and-dialogue teaching style. Good anchor when the user needs a visual reference.

- **[SourceMaking — Strategy](https://sourcemaking.com/design_patterns/strategy)**
  Structured summary: intent, problem, solution, applicability. Use for: a quick cheat sheet to recall structure.

- **Local research note: `.scratch/research/target-selection-options.md`**
  Primary-source research on the actual target-selection mechanics of a real MOBA. Use for: mapping concrete strategies to real game mechanics, persuasive examples.

## Wisdom (Communities)

- **[r/learnprogramming](https://reddit.com/r/learnprogramming)**
  Q&A on pattern application and code review. Use for: when stuck applying Strategy to real code.

- **[r/java](https://reddit.com/r/java)**
  Discussion of modern Java idioms (records, sealed types). Use for: writing idiomatic Java 17+ Strategy interfaces.

- **Stack Overflow — tags [design-patterns] + [strategy-pattern]**
  Search "strategy pattern Java" for high-quality Q&A. Use for: edge cases (multi-strategy, thread-safety, null strategy).

## Gaps

- No high-quality Strategy-pattern resources written in Vietnamese — if the user needs Vietnamese support, internal Vietnamese summaries need to be authored.
- No benchmark comparing Strategy + interface dispatch vs `if/else` chain performance — not important for the first lesson, but may matter later.

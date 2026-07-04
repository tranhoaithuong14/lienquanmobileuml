# Mission: Strategy Pattern in Java through a MOBA combat simulation

## Why

Building a Java teaching artifact that simulates MOBA combat systems — starting with the **hero auto-targeting** mechanic. The Strategy pattern is the first GoF pattern to master, because it shows up across most game subsystems (AI, combat, item builds, target selection). Once Strategy is internalized, subsequent patterns (State, Observer, Command) feel much more natural.

End goal: read Strategy pattern code in Java fluently, recognize where it should be applied in real game code, and write clean implementations with clear UML diagrams for the teaching artifact.

## Success looks like

- Read any Java code that uses the Strategy pattern and immediately recognize the Context, Strategy, and ConcreteStrategy roles — without reading comments.
- Write the 4 canonical `TargetSelector` implementations (`NearestEnemy`, `LowestHP`, `HeroPriority`, `MarkedTarget`) from scratch, with idiomatic interface + Context per GoF.
- Explain why Strategy beats `if/else` chains or `switch` statements in this case, and why it beats inheritance.
- Draw a class diagram (PlantUML) for Strategy and apply it to a different subsystem outside target selection.

## Constraints

- **Communication language**: English (the user prefers it for the technical surface). Technical terms stay in English.
- **Code language**: Java (locked in at the previous grilling session).
- **UML tool**: PlantUML (locked in at the previous grilling session).
- Each lesson must be short — working memory is tiny. One clear win per lesson.
- Code samples use Java 17+ features (records, switch expressions) if they make examples tighter, but not required — prefer classic GoF syntax.
- Each lesson must include retrieval practice (quiz) to build storage strength, not just fluency.

## Out of scope

- Other GoF patterns (State, Observer, Command, …) — wait until Strategy is solid.
- Full MOBA scope (140 heroes, ranked, monetization, networking) — the teaching artifact uses the genre as interaction context, not as the end goal.
- Advanced UML (sequence diagrams, state machines) — defer until class diagrams are solid.
- Deep reading of the 1994 GoF book — enough to recognize it as a primary source, not cover-to-cover.

## Open questions

- User's Java/OOP background is unknown (new to Java, or already familiar but not with patterns). Needs to be confirmed in the first lesson to calibrate.
- The teaching context uses English now; whether the user is comfortable reading English technical docs (Refactoring Guru, GoF book) needs a check. If not, Vietnamese resources needed.
